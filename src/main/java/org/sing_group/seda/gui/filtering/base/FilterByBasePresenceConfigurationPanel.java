package org.sing_group.seda.gui.filtering.base;

import javax.swing.JPanel;

public class FilterByBasePresenceConfigurationPanel extends JPanel {
  private static final long serialVersionUID = 1L;
  private FilterByBasePresenceConfigurationModel model;
  private MultipleBasePresenceConfigurationPanel basesConfigurationPanel;

  public FilterByBasePresenceConfigurationPanel() {
    this.init();
    this.model = new FilterByBasePresenceConfigurationModel(this);
  }

  private void init() {
    this.basesConfigurationPanel = new MultipleBasePresenceConfigurationPanel();
    this.add(this.basesConfigurationPanel);
  }

  public MultipleBasePresenceConfigurationPanel getBasesConfigurationPanel() {
    return basesConfigurationPanel;
  }

  public FilterByBasePresenceConfigurationModel getModel() {
    return model;
  }
}
