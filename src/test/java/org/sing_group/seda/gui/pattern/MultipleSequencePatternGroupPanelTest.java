package org.sing_group.seda.gui.pattern;

import java.awt.Component;

import javax.swing.event.ChangeEvent;

import org.sing_group.seda.gui.TestGuiUtils;

public class MultipleSequencePatternGroupPanelTest {

  public static void main(String[] args) {
    TestGuiUtils.showComponent(getTestPanel());
  }

  private static Component getTestPanel() {
    MultipleSequencePatternGroupPanel testPanel = new MultipleSequencePatternGroupPanel();
    testPanel.addSequencePatternEditorListener(new SequencePatternEditorListener() {
      
      @Override
      public void patternEdited(PatternEditionEvent event) {
        printValidUserSelection("Pattern edited");
      }

      @Override
      public void patternAdded(ChangeEvent event) {
        printValidUserSelection("Pattern added");
      }
 
      @Override
      public void patternRemoved(ChangeEvent event) {
        printValidUserSelection("Pattern removed");
      }
      
      private void printValidUserSelection(String message) {
        System.err.println("[" + message + "] isValidUserSelection() = " + testPanel.isValidUserSelection());
        if(testPanel.isValidUserSelection()) {
          System.err.println("\t" + testPanel.getEvaluableSequencePattern());
        }
      }

    });
    return testPanel;
  }
}
