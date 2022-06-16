/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2020 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.seda.core.rename;

import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.plugin.spi.DefaultTransformationValidation;
import org.sing_group.seda.plugin.spi.Validation;
import org.sing_group.seda.util.StringUtils;

@XmlRootElement
public class FieldSplitRenamer extends AbstractHeaderRenamer {

  public enum Mode {
    KEEP, REMOVE;

    @Override
    public String toString() {
      return StringUtils.capitalize(super.toString());
    }
  }

  @XmlElement
  private String fieldDelimiter;
  @XmlElement
  private String joinDelimiter;
  @XmlElement
  private Mode mode;
  @XmlElement
  private List<Integer> fields;

  public FieldSplitRenamer() {
    super(HeaderTarget.ALL);
  }

  public FieldSplitRenamer(
    HeaderTarget target, String fieldDelimiter, String joinDelimiter, Mode mode, List<Integer> fields
  ) {
    super(target);

    this.fieldDelimiter = fieldDelimiter;
    this.joinDelimiter = joinDelimiter;
    this.mode = mode;
    this.fields = fields;
  }

  @Override
  public SequencesGroup rename(SequencesGroup sequences, DatatypeFactory factory) {
    List<Sequence> renamedSequences = new LinkedList<>();

    for (int i = 0; i < sequences.getSequenceCount(); i++) {
      Sequence original = sequences.getSequence(i);

      String partToRename = getRenamePart(original);

      String[] parts = partToRename.split(Pattern.quote(fieldDelimiter));

      List<String> newFields = new LinkedList<>();

      if (this.mode.equals(Mode.REMOVE)) {
        for (int field = 0; field < parts.length; field++) {
          if (!fields.contains(field)) {
            newFields.add(parts[field]);
          }
        }
      } else if (this.mode.equals(Mode.KEEP)) {
        for (int field : this.fields) {
          if (field < parts.length) {
            newFields.add(parts[field]);
          }
        }
      } else {
        throw new IllegalStateException("Unknown mode " + this.mode);
      }

      String renamedPart = newFields.stream().collect(joining(joinDelimiter));

      renamedSequences.add(renameSequence(original, renamedPart, factory));
    }

    return buildSequencesGroup(sequences.getName(), sequences.getProperties(), renamedSequences, factory);
  }

  public String getFieldDelimiter() {
    return fieldDelimiter;
  }

  public String getJoinDelimiter() {
    return joinDelimiter;
  }

  public List<Integer> getFields() {
    return fields;
  }

  public Mode getMode() {
    return mode;
  }

  @Override
  public Validation validate() {
    List<String> errors = new ArrayList<String>(super.validate().getValidationErrors());

    if (this.fieldDelimiter == null) {
      errors.add("The field delimiter can't be null.");
    }
    if (this.joinDelimiter == null) {
      errors.add("The join delimiter can't be null.");
    }
    if (this.mode == null) {
      errors.add("The mode can't be null.");
    }
    if (this.fields == null) {
      errors.add("The fields list can't be null.");
    } else if (this.fields.isEmpty()) {
      errors.add("The fields list can't be empty.");
    }

    return errors.isEmpty() ? new DefaultTransformationValidation() : new DefaultTransformationValidation(errors);
  }
}
