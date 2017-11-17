package org.sing_group.seda.gui.rename;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.RadioButtonsPanel;
import org.sing_group.gc4s.text.MultilineLabel;
import org.sing_group.gc4s.ui.CardsPanel;
import org.sing_group.gc4s.ui.CardsPanel.CardsPanelBuilder;
import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.core.SedaContextEvent;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.io.LazyDatatypeFactory;

public class RenameTransformationConfigurationPanel extends AbstractRenamePanel implements RenamePanelEventListener {
  private static final long serialVersionUID = 1L;
  private static final String NO_PREVIEW_SEQ_AVAILABLE = "No file selected";
  private static final String INVALID_CONFIGURATION = "Invalid rename configuration";

  enum Rename{
    REPLACE_WORD("Replace word", new WordReplaceRenamePanel()),
    REPLACE_INTERVAL("Replace interval", new IntervalReplaceRenamePanel()),
    ADD_STRING("Add prefix/sufix", new AddStringHeaderRenamePanel()),
    MULTIPART_HEADER("Multipart header", new FieldSplitRenamePanel());

    private String name;
    private AbstractRenamePanel panel;

    Rename(String name, AbstractRenamePanel panel) {
      this.name = name;
      this.panel = panel;
    }

    @Override
    public String toString() {
      return this.name;
    }

    public AbstractRenamePanel getPanel() {
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

  public RenameTransformationConfigurationPanel() {
    this.init();
  }

  private void init() {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    this.add(getPreviewPanel());
    this.add(getHeaderTargetPanel());
    this.add(getConfigurationPanel());
  }

  private Component getHeaderTargetPanel() {
    return new InputParametersPanel(getHeaderTargetParameter());
  }

  private InputParameter getHeaderTargetParameter() {
    this.headerTargetRbtnPanel = new RadioButtonsPanel<>(HeaderTarget.values(), 1, 2);
    this.headerTargetRbtnPanel.addItemListener(this::headerTargetValueChanged);

    return new InputParameter("Target: ", this.headerTargetRbtnPanel, "The header target.");
  }

  private void headerTargetValueChanged(ItemEvent event) {
    if (event.getStateChange() == ItemEvent.SELECTED) {
      onRenameConfigurationChanged(this);
    }
  }

  private Component getConfigurationPanel() {
    CardsPanelBuilder builder = new CardsPanelBuilder().withSelectionLabel("Rename type:");
    for(Rename rename : Rename.values())  {
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
  public HeaderRenamer getHeaderRenamer(DatatypeFactory factory, HeaderTarget target) {
    return getSelectedRenamePanel().getHeaderRenamer(factory, target);
  }

  private AbstractRenamePanel getSelectedRenamePanel() {
    return (AbstractRenamePanel) this.cardsPanel.getSelectedCard();
  }

  @Override
  public void onRenameConfigurationChanged(Object source) {
    updateCurrentPreview();
    fireRenameConfigurationChanged();
  }

  private HeaderTarget getHeaderTarget() {
    return this.headerTargetRbtnPanel.getSelectedItem().get();
  }

  public HeaderRenamer getHeaderRenamer(DatatypeFactory factory) {
    return this.getHeaderRenamer(factory, getHeaderTarget());
  }

  private void sedaContextPathsChanged(SedaContextEvent event) {
    SwingUtilities.invokeLater(
      () -> {
        if (event.getType().equals(SedaContextEvent.SedaContextEventType.SELECTED_PATHS_CHANGED)) {
          List<String> selectedPaths = this.sedaContext.getSelectedPaths();
          if (!selectedPaths.isEmpty()) {
            setPreviewPath(selectedPaths.get(0));
          } else {
            clearPreviewPath();
          }
        }
      }
    );
  }

  private void clearPreviewPath() {
    setPreviewSequence(null);
  }

  private void setPreviewPath(String path) {
    if (this.currentPath == null || !currentPath.equals(path)) {
      this.currentPath = path;
      SequencesGroup group = new LazyDatatypeFactory().newSequencesGroup(new File(path).toPath());
      if (group.getSequenceCount() > 0) {
        setPreviewSequence(group.getSequence(0));
      }
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
      if(isValidConfiguration()) {
        Sequence previewSequence =
          getHeaderRenamer(DatatypeFactory.getDefaultDatatypeFactory())
            .rename(new LazyDatatypeFactory().newSequencesGroup("Test", sampleSequence)).getSequence(0);
        this.currentPreviewLabel.setText(previewSequence.getHeader());
      } else {
        this.currentPreviewLabel.setText(INVALID_CONFIGURATION);
      }
    } else {
      this.currentPreviewLabel.setText(NO_PREVIEW_SEQ_AVAILABLE);
    }
  }

  public void setSedaContext(SedaContext context) {
    this.sedaContext = context;
    this.sedaContext.addSedaContextListener(this::sedaContextPathsChanged);
  }
}
