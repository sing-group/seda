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
package org.sing_group.seda.gui.configuration_panel.isoforms;

import static java.util.Arrays.asList;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.DEFAULT_REMOVE_ISOFORM_HEADER_DELIMITER;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.DEFAULT_REMOVE_ISOFORM_HEADER_PREFIX;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.DEFAULT_REMOVE_ISOFORM_HEADER_SUFFIX;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.DEFAULT_REMOVE_ISOFORM_HEADER_TARGET;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.PARAM_HEADER_TARGET_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.PARAM_HEADER_TARGET_HELP_GUI;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.PARAM_REMOVE_ISOFORM_HEADER_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RemoveIsoformsSedaPluginInfo.PARAM_REMOVE_ISOFORM_HEADER_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.*;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.seda.core.rename.EmptySequenceHeadersJoiner;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.SequenceHeadersJoiner;

public class RemovedIsoformHeadersConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private static final String DEFAULT_DELIMITER = DEFAULT_REMOVE_ISOFORM_HEADER_DELIMITER;
  private static final String DEFAULT_PREFIX = DEFAULT_REMOVE_ISOFORM_HEADER_PREFIX;
  private static final String DEFAULT_SUFFIX = DEFAULT_REMOVE_ISOFORM_HEADER_SUFFIX;

  public static final String DESCRIPTION_ADD_HEADERS = PARAM_REMOVE_ISOFORM_HEADER_HELP_GUI;
  public static final String DESCRIPTION_HEADER_TARGET = PARAM_HEADER_TARGET_HELP_GUI;

  public static final boolean DEFAULT_ADD_REMOVED_ISOFORM_HEADERS = false;
  public static final HeaderTarget DEFAULT_HEADER_TARGET = DEFAULT_REMOVE_ISOFORM_HEADER_TARGET;

  public static final String PROPERTY_ADD_REMOVED_ISOFORM_HEADERS = "property.add.headers";
  public static final String PROPERTY_HEADER_TARGET = "property.header.target";

  public static final Set<String> PROPERTIES =
    new HashSet<>(
      asList(PROPERTY_HEADER_TARGET, PROPERTY_ADD_REMOVED_ISOFORM_HEADERS)
    );

  private JCheckBox addRemovedIsoformHeadersCb;
  private JComboBox<HeaderTarget> headerTargetComboBox;

  private boolean oldReferenceSizeValue = DEFAULT_ADD_REMOVED_ISOFORM_HEADERS;
  private HeaderTarget oldHeaderTargetValue = DEFAULT_HEADER_TARGET;

  private InputParameter[] additionalParameters;

  public RemovedIsoformHeadersConfigurationPanel(InputParameter... additionalParameters) {
    this.additionalParameters = additionalParameters;
    this.init();
  }

  private void init() {
    this.setLayout(new BorderLayout());
    this.add(new InputParametersPanel(getInputParameters()));
  }

  private InputParameter[] getInputParameters() {
    List<InputParameter> parameters = new LinkedList<>();
    parameters.add(getAddRemovedIsoformHeadersParameter());
    parameters.add(getHeaderTargetParameter());
    parameters.addAll(Arrays.asList(additionalParameters));

    return parameters.toArray(new InputParameter[parameters.size()]);
  }

  private InputParameter getAddRemovedIsoformHeadersParameter() {
    this.addRemovedIsoformHeadersCb = new JCheckBox(PARAM_REMOVE_ISOFORM_HEADER_DESCRIPTION + "?");
    this.addRemovedIsoformHeadersCb.addItemListener(this::removedIsoformHeadersChanged);

    return new InputParameter("", this.addRemovedIsoformHeadersCb, DESCRIPTION_ADD_HEADERS);
  }

  private void removedIsoformHeadersChanged(ItemEvent event) {
    boolean newValue = isAddRemovedIsoformHeaders();
    firePropertyChange(PROPERTY_ADD_REMOVED_ISOFORM_HEADERS, oldReferenceSizeValue, newValue);
    oldReferenceSizeValue = newValue;
  }

  private boolean isAddRemovedIsoformHeaders() {
    return this.addRemovedIsoformHeadersCb.isSelected();
  }

  private InputParameter getHeaderTargetParameter() {
    this.headerTargetComboBox = new JComboBox<HeaderTarget>(HeaderTarget.values());
    this.headerTargetComboBox.setSelectedItem(DEFAULT_HEADER_TARGET);
    this.headerTargetComboBox.addItemListener(this::headerTargetChanged);

    return new InputParameter(
      PARAM_HEADER_TARGET_DESCRIPTION + ":", this.headerTargetComboBox, DESCRIPTION_HEADER_TARGET
    );
  }

  private void headerTargetChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      HeaderTarget newValue = getHeaderTarget();
      firePropertyChange(PROPERTY_HEADER_TARGET, oldHeaderTargetValue, newValue);
      oldHeaderTargetValue = newValue;
    }
  }

  private HeaderTarget getHeaderTarget() {
    return (HeaderTarget) this.headerTargetComboBox.getSelectedItem();
  }

  public SequenceHeadersJoiner getSequenceHeadersJoiner() {
    if (isAddRemovedIsoformHeaders()) {
      return new SequenceHeadersJoiner(getHeaderTarget(), DEFAULT_DELIMITER, DEFAULT_PREFIX, DEFAULT_SUFFIX);
    } else {
      return new EmptySequenceHeadersJoiner();
    }
  }

  public void setSequenceHeadersJoiner(SequenceHeadersJoiner sequenceHeadersJoiner) {
    if (sequenceHeadersJoiner instanceof EmptySequenceHeadersJoiner) {
      this.addRemovedIsoformHeadersCb.setSelected(false);
    } else if (sequenceHeadersJoiner instanceof SequenceHeadersJoiner) {
      this.addRemovedIsoformHeadersCb.setSelected(true);
      this.headerTargetComboBox.setSelectedItem(((SequenceHeadersJoiner) sequenceHeadersJoiner).getTarget());
    } else {
      throw new IllegalArgumentException("Unsupported class " + sequenceHeadersJoiner.getClass());
    }
  }
}
