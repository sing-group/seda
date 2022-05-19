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
package org.sing_group.seda.gui.rename;

import static java.util.Collections.emptyMap;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static org.sing_group.seda.datatype.DatatypeFactory.newFactory;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_RENAME_TYPE_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_TARGET_DESCRIPTION;
import static org.sing_group.seda.plugin.core.info.plugin.RenameHeaderSedaPluginInfo.PARAM_TARGET_HELP_GUI;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.*;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanelBuilder;
import org.sing_group.gc4s.ui.CenteredJPanel;
import org.sing_group.gc4s.ui.text.MultilineLabel;
import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.core.SedaContextEvent;
import org.sing_group.seda.core.rename.AddStringHeaderRenamer;
import org.sing_group.seda.core.rename.FieldSplitRenamer;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.IntervalReplaceRenamer;
import org.sing_group.seda.core.rename.WordReplaceRenamer;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.gui.HtmlMessage;

public class RenameHeaderTransformationConfigurationPanel extends AbstractRenameHeaderPanel
  implements RenamePanelEventListener {
  private static final long serialVersionUID = 1L;

  private static final String NO_PREVIEW_SEQ_AVAILABLE = "No file selected";
  private static final String INVALID_CONFIGURATION = "Invalid rename configuration";

  private static final Comparator<? super String> PATH_SIZE_COMPARATOR = new Comparator<String>() {

    @Override
    public int compare(String o1, String o2) {
      long diff = new File(o1).length() - new File(o2).length();
      if (diff < 0) {
        return -1;
      } else if (diff > 0) {
        return 1;
      } else {
        return 0;
      }
    }
  };

  public enum Rename {
    REPLACE_WORD("Replace word", new WordReplaceRenamePanel()), REPLACE_INTERVAL(
      "Replace interval", new IntervalReplaceRenamePanel()
    ), ADD_STRING(
      "Add prefix/suffix", new AddStringHeaderRenamePanel()
    ), MULTIPART_HEADER("Multipart header", new FieldSplitRenamePanel());

    private String name;
    private AbstractRenameHeaderPanel panel;

    Rename(String name, AbstractRenameHeaderPanel panel) {
      this.name = name;
      this.panel = panel;
    }

    @Override
    public String toString() {
      return this.name;
    }

    public AbstractRenameHeaderPanel getPanel() {
      return panel;
    }
  }

  private RadioButtonsPanel<HeaderTarget> headerTargetRbtnPanel;
  private CardsPanel cardsPanel;
  private SedaContext sedaContext;
  private MultilineLabel currentSampleLabel;
  private MultilineLabel currentPreviewLabel;
  private Sequence sampleSequence;
  private String currentPath;
  private RenameHeaderTransformationProvider transformationProvider;

  public RenameHeaderTransformationConfigurationPanel() {
    this.init();
    this.initTransformationProvider();
  }

  private void init() {
    this.add(new CenteredJPanel(getMainPanel()));
  }

  private JPanel getMainPanel() {
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    mainPanel.add(getPreviewPanel());
    mainPanel.add(getHeaderTargetPanel());
    mainPanel.add(getConfigurationPanel());

    return mainPanel;
  }

  private Component getHeaderTargetPanel() {
    return new InputParametersPanel(getHeaderTargetParameter());
  }

  private InputParameter getHeaderTargetParameter() {
    this.headerTargetRbtnPanel = new RadioButtonsPanel<>(HeaderTarget.values(), 1, 2);
    this.headerTargetRbtnPanel.addItemListener(this::headerTargetValueChanged);

    return new InputParameter(PARAM_TARGET_DESCRIPTION + ": ", this.headerTargetRbtnPanel, PARAM_TARGET_HELP_GUI);
  }

  private void headerTargetValueChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      onRenameConfigurationChanged(this);
    }
  }

  private Component getConfigurationPanel() {
    CardsPanelBuilder builder = new CardsPanelBuilder().withSelectionLabel(PARAM_RENAME_TYPE_DESCRIPTION + ":");
    for (Rename rename : Rename.values()) {
      rename.getPanel().addRenamePanelEventListener(this);
      builder.withCard(rename, rename.getPanel());
    }
    this.cardsPanel = builder.build();

    this.cardsPanel.addPropertyChangeListener(CardsPanel.PROPERTY_VISIBLE_CARD, this::selectedCardChanged);

    return this.cardsPanel;
  }

  private void selectedCardChanged(PropertyChangeEvent evt) {
    onRenameConfigurationChanged(this);
  }

  private Component getPreviewPanel() {
    JLabel sampleLabel = new JLabel("Sample: ");
    currentSampleLabel = new MultilineLabel(NO_PREVIEW_SEQ_AVAILABLE);
    currentSampleLabel.setColumns(50);
    JLabel previewLabel = new JLabel("Preview: ");
    currentPreviewLabel = new MultilineLabel(NO_PREVIEW_SEQ_AVAILABLE);
    currentPreviewLabel.setColumns(50);

    JPanel southPanel = new JPanel();
    southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
    southPanel.setBorder(BorderFactory.createTitledBorder("Rename preview"));

    JPanel samplePanel = new JPanel(new FlowLayout());
    samplePanel.add(sampleLabel);
    samplePanel.add(currentSampleLabel);

    JPanel previewPanel = new JPanel(new FlowLayout());
    previewPanel.add(previewLabel);
    previewPanel.add(currentPreviewLabel);

    southPanel.add(samplePanel);
    southPanel.add(previewPanel);

    return southPanel;
  }

  @Override
  public boolean isValidConfiguration() {
    return getSelectedRenamePanel().isValidConfiguration();
  }

  @Override
  public HeaderRenamer getHeaderRenamer(HeaderTarget target) {
    return getSelectedRenamePanel().getHeaderRenamer(target);
  }

  private AbstractRenameHeaderPanel getSelectedRenamePanel() {
    return (AbstractRenameHeaderPanel) this.cardsPanel.getSelectedCard();
  }

  @Override
  public void onRenameConfigurationChanged(Object source) {
    updateCurrentPreview();
    fireRenameConfigurationChanged();
    updateTransformationProvider();
  }

  private void updateTransformationProvider() {
    if (this.isValidConfiguration()) {
      this.transformationProvider.setHeaderRenamer(getHeaderRenamer());
    } else {
      this.transformationProvider.clearHeaderRenamer();
    }
  }

  private HeaderTarget getHeaderTarget() {
    return this.headerTargetRbtnPanel.getSelectedItem().get();
  }

  public HeaderRenamer getHeaderRenamer() {
    return this.getHeaderRenamer(getHeaderTarget());
  }

  private void sedaContextPathsChanged(SedaContextEvent event) {
    SwingUtilities.invokeLater(
      () -> {
        if (event.getType().equals(SedaContextEvent.SedaContextEventType.SELECTED_PATHS_CHANGED)) {
          Iterator<String> selectedPaths =
            this.sedaContext.getSelectedPaths().stream().sorted(PATH_SIZE_COMPARATOR).iterator();

          boolean set = false;
          while (selectedPaths.hasNext() && !set) {
            String nextPreviewPath = selectedPaths.next();
            set = setPreviewPath(nextPreviewPath);
          }

          if (!set) {
            clearPreviewPath();
          }
        }
      }
    );
  }

  private void clearPreviewPath() {
    setPreviewSequence(null);
  }

  private boolean setPreviewPath(String path) {
    if (this.currentPath == null || !currentPath.equals(path)) {
      this.currentPath = path;
      try {

        SequencesGroup group = getDatatypeFactory().newSequencesGroup(new File(path).toPath());
        if (group.getSequenceCount() > 0) {
          setPreviewSequence(group.getSequence(0));
          return true;
        } else {
          this.currentPath = null;
          return false;
        }
      } catch (OutOfMemoryError e) {

        StringBuilder message = new StringBuilder("Error processing dataset: ");
        message
          .append(e.getMessage())
          .append("<br/><br/>Please, visit the SEDA manual to know how to fix this issue: ")
          .append(
            "<a href=\"https://www.sing-group.org/seda/manual/installation-and-configuration.html#increasing-ram-memory\">"
          )
          .append("Installation and configuration / Increasing RAM memory</a>");

        JOptionPane.showMessageDialog(this, new HtmlMessage(message.toString()), "Transformation Error", ERROR_MESSAGE);

        this.currentPath = null;
        return false;
      }
    } else {
      return true;
    }
  }

  private void setPreviewSequence(Sequence sequence) {
    this.sampleSequence = sequence;
    if (this.sampleSequence != null) {
      this.currentSampleLabel.setText(sequence.getHeader());
    } else {
      this.currentPath = null;
      this.currentSampleLabel.setText(NO_PREVIEW_SEQ_AVAILABLE);
    }
    this.updateCurrentPreview();
  }

  private void updateCurrentPreview() {
    if (this.sampleSequence != null) {
      if (isValidConfiguration()) {
        Sequence previewSequence =
          getHeaderRenamer().rename(
            getDatatypeFactory().newSequencesGroup("Test", emptyMap(), sampleSequence),
            DatatypeFactory.getDefaultDatatypeFactory()
          )
            .getSequence(0);
        this.currentPreviewLabel.setText(previewSequence.getHeader());
      } else {
        this.currentPreviewLabel.setText(INVALID_CONFIGURATION);
      }
    } else {
      this.currentPreviewLabel.setText(NO_PREVIEW_SEQ_AVAILABLE);
    }
  }

  private DatatypeFactory getDatatypeFactory() {
    return newFactory(this.sedaContext.isInMemoryProcessingEnabled(), this.sedaContext.isCharsetSupport());
  }

  private void initTransformationProvider() {
    this.transformationProvider = new RenameHeaderTransformationProvider();
    this.updateTransformationProvider();
  }

  public RenameHeaderTransformationProvider getTransformationProvider() {
    return this.transformationProvider;
  }

  public void setSedaContext(SedaContext context) {
    this.sedaContext = context;
    this.sedaContext.addSedaContextListener(this::sedaContextPathsChanged);
  }

  public void setTransformationProvider(RenameHeaderTransformationProvider transformationProvider) {
    this.transformationProvider = transformationProvider;
    Rename selectedCard = null;

    HeaderRenamer renamer = this.transformationProvider.getHeaderRenamer();

    this.headerTargetRbtnPanel.setSelectedItem(renamer.getHeaderTarget());

    if (renamer instanceof IntervalReplaceRenamer) {
      selectedCard = Rename.REPLACE_INTERVAL;
      ((IntervalReplaceRenamePanel) selectedCard.getPanel()).setHeaderRenamer((IntervalReplaceRenamer) renamer);
    } else if (renamer instanceof WordReplaceRenamer) {
      selectedCard = Rename.REPLACE_WORD;
      ((WordReplaceRenamePanel) selectedCard.getPanel()).setHeaderRenamer((WordReplaceRenamer) renamer);
    } else if (renamer instanceof AddStringHeaderRenamer) {
      selectedCard = Rename.ADD_STRING;
      ((AddStringHeaderRenamePanel) selectedCard.getPanel()).setHeaderRenamer((AddStringHeaderRenamer) renamer);
    } else if (renamer instanceof FieldSplitRenamer) {
      selectedCard = Rename.MULTIPART_HEADER;
      ((FieldSplitRenamePanel) selectedCard.getPanel()).setHeaderRenamer((FieldSplitRenamer) renamer);
    }

    this.cardsPanel.setSelectedCard(selectedCard);
  }
}
