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
package org.sing_group.seda.gui.filtering.base;

import static org.sing_group.seda.plugin.core.info.plugin.BasePresenceSedaPluginInfo.PARAM_BASE_FILTER_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.BasePresenceSedaPluginInfo.PARAM_BASE_FILTER_HELP_GUI;

import java.awt.*;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.event.DocumentEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.event.DocumentAdapter;
import org.sing_group.gc4s.input.DoubleRange;
import org.sing_group.gc4s.input.DoubleRangeSpinnerInputPanel;
import org.sing_group.gc4s.ui.icons.Icons;
import org.sing_group.seda.transformation.sequencesgroup.FilterByBasePresenceTransformation.BasePresence;

public class BasePresenceConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  public static final String PROPERTY_BASE_PRESENCE = "seda.base.presence";

  private JXTextField basesTextField;
  private DoubleRangeSpinnerInputPanel rangePanel;

  private BasePresence oldValue;
  private BasePresence initialValue;

  public BasePresenceConfigurationPanel() {
    this(null);
  }

  public BasePresenceConfigurationPanel(BasePresence basePresence) {
    this.initialValue = basePresence;
    this.init();
  }

  private void init() {
    this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    this.add(new JLabel(PARAM_BASE_FILTER_DESCRIPTION + ": "));
    this.add(getBasesTextField());
    this.add(Box.createHorizontalStrut(5));
    this.add(getBasesHelp());
    this.add(Box.createHorizontalStrut(10));
    this.add(getBasesPercentagesComponent());
    this.add(Box.createHorizontalStrut(5));
    this.add(getPercentagesHelp());
  }

  private JLabel getBasesHelp() {
    JLabel helpLabel = new JLabel(Icons.ICON_INFO_2_16);
    helpLabel.setToolTipText(PARAM_BASE_FILTER_HELP_GUI);

    return helpLabel;
  }

  private JLabel getPercentagesHelp() {
    JLabel helpLabel = new JLabel(Icons.ICON_INFO_2_16);
    helpLabel.setToolTipText("The minimum and maximum percentages allowed for the specified base(s).");

    return helpLabel;
  }

  private Component getBasesTextField() {
    this.basesTextField = new JXTextField("ACTG");
    this.basesTextField.setColumns(6);
    if (this.initialValue != null) {
      this.basesTextField
        .setText(this.initialValue.getBases().stream().map(Object::toString).collect(Collectors.joining()));
    }
    this.basesTextField.getDocument().addDocumentListener(new DocumentAdapter() {

      @Override
      public void removeUpdate(DocumentEvent e) {
        configurationChanged();
      }

      @Override
      public void insertUpdate(DocumentEvent e) {
        configurationChanged();
      }
    });
    return this.basesTextField;
  }

  private Component getBasesPercentagesComponent() {
    this.rangePanel = new DoubleRangeSpinnerInputPanel(0d, 1d, 0d, 1d, 0.1d);
    rangePanel.setStartLabel("Min. % ");
    rangePanel.setEndLabel("Max. % ");
    if (this.initialValue != null) {
      rangePanel.setSelectedRange(
        new DoubleRange(this.initialValue.getMinimumPresence(), this.initialValue.getMaximumPresence())
      );
    }
    rangePanel.addPropertyChangeListener(
      DoubleRangeSpinnerInputPanel.PROPERTY_RANGE,
      e -> {
        configurationChanged();
      }
    );

    return rangePanel;
  }

  private void configurationChanged() {
    BasePresence newValue = null;
    try {
      newValue = getBasePresence();
    } catch (IllegalArgumentException e) {}

    firePropertyChange(PROPERTY_BASE_PRESENCE, this.oldValue, newValue);
    this.oldValue = newValue;
  }

  public BasePresence getBasePresence() {
    DoubleRange range = rangePanel.getRange();
    return new BasePresence(
      range.getMin(), range.getMax(), this.basesTextField.getText().toCharArray()
    );
  }

  public boolean isValidValue() {
    return this.basesTextField.getText() != null && !this.basesTextField.getText().isEmpty()
      && this.rangePanel.isValidRange();
  }
}
