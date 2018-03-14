package org.sing_group.seda.gui.rename;

import java.awt.event.ItemEvent;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.event.ListDataEvent;

import org.jdesktop.swingx.JXTextField;
import org.sing_group.gc4s.input.InputParameter;
import org.sing_group.gc4s.input.InputParametersPanel;
import org.sing_group.gc4s.input.list.JInputList;
import org.sing_group.gc4s.input.list.event.DefaultListDataListener;
import org.sing_group.seda.core.rename.HeaderRenamer;
import org.sing_group.seda.core.rename.HeaderTarget;
import org.sing_group.seda.core.rename.WordReplaceRenamer;
import org.sing_group.seda.datatype.DatatypeFactory;

public class WordReplaceRenamePanel extends AbstractRenamePanel {
  private static final long serialVersionUID = 1L;
  private JCheckBox isRegexCheckBox;
  private JXTextField replacementTextField;
  private JInputList targetsListPanel;

  public WordReplaceRenamePanel() {
    this.init();
  }

  private void init() {
    this.add(new InputParametersPanel(getParameters()));
  }

  private InputParameter[] getParameters() {
    InputParameter[] toret = new  InputParameter[3];
    toret[0] = getTargetsParameter();
    toret[1] = getIsRegexParameter();
    toret[2] = getReplacementParameter();

    return toret;
  }

  private InputParameter getTargetsParameter() {
    this.targetsListPanel = new JInputList(true, false, false);
    this.targetsListPanel.setElementIntroductionEnabled(true);
    this.targetsListPanel.getListPanel().getBtnMoveDown().setVisible(false);
    this.targetsListPanel.getListPanel().getBtnMoveUp().setVisible(false);
    this.targetsListPanel.addListDataListener(new DefaultListDataListener() {

      @Override
      public void intervalAdded(ListDataEvent e) {
        renameConfigurationChanged();
      }

      @Override
      public void intervalRemoved(ListDataEvent e) {
        renameConfigurationChanged();
      }
    });

    return new InputParameter("Targets", this.targetsListPanel, "The target words.");
  }

  private InputParameter getIsRegexParameter() {
    this.isRegexCheckBox = new JCheckBox("Regex");
    this.isRegexCheckBox.addItemListener(this::isRegexCheckBoxItemEvent);

    return new InputParameter("", this.isRegexCheckBox, "Whether targets must be applied as regex or not.");
  }

  private void isRegexCheckBoxItemEvent(ItemEvent event) {
    this.renameConfigurationChanged();
  }

  private InputParameter getReplacementParameter() {
    this.replacementTextField = new JXTextField("Replacement");
    this.replacementTextField.getDocument().addDocumentListener(documentListener);

    return new InputParameter("Replacement", this.replacementTextField, "The replacement.");
  }

  private List<String> getTargets() {
    return this.targetsListPanel.getInputItems();
  }

  private boolean isRegex() {
    return this.isRegexCheckBox.isSelected();
  }

  private String getReplacement() {
    return this.replacementTextField.getText();
  }

  @Override
  public boolean isValidConfiguration() {
    return !getTargets().isEmpty();
  }

  @Override
  public HeaderRenamer getHeaderRenamer(DatatypeFactory factory, HeaderTarget target) {
    return new WordReplaceRenamer(factory, target, getReplacement(), isRegex(), getTargets());
  }
}
