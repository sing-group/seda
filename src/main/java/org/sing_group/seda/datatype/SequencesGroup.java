package org.sing_group.seda.datatype;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.sing_group.seda.gui.reformat.LineBreakType;

public interface SequencesGroup {

  public static final String PROPERTY_LINE_BREAK_OS = "sequence.group.linebreak";
  public static final String DEFAULT_LINE_BREAK_OS = LineBreakType.defaultType().getLineBreak();

  public static SequencesGroup of(String name, Sequence... sequences) {
    return DatatypeFactory.getDefaultDatatypeFactory().newSequencesGroup(name, sequences);
  }

  public static SequencesGroup of(String name, Map<String, Object> properties, Sequence... sequences) {
    return DatatypeFactory.getDefaultDatatypeFactory().newSequencesGroup(name, properties, sequences);
  }

  public String getName();

  public Stream<Sequence> getSequences();

  public int getSequenceCount();

  public Map<String, Object> getProperties();

  @SuppressWarnings("unchecked")
  public default <T> Optional<T> getProperty(String key) {
    return Optional.ofNullable((T) this.getProperties().get(key));
  }

  public default Sequence getSequence(int index) {
    return this.getSequences()
      .skip(index)
    .findFirst()
    .orElseThrow(() -> new IndexOutOfBoundsException("Invalid sequence index"));
  }

  public default Sequence getSequence(String name) {
    return this.getSequences()
      .filter(sequence -> sequence.getName().equals(name))
    .findFirst()
    .orElseThrow(() -> new IndexOutOfBoundsException("No sequence found with name: " + name));
  }
}
