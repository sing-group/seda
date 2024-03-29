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

import java.util.Arrays;

import org.sing_group.seda.core.SedaContext;
import org.sing_group.seda.gui.TestGuiUtils;
import org.sing_group.seda.gui.configuration_panel.rename.RenameHeaderTransformationConfigurationPanel;
import org.sing_group.seda.gui.configuration_panel.rename.RenamePanelEventListener;

public class RenameTransformationConfigurationPanelTest {

  private static final SedaContext TEST_CONTEXT = new SedaContext();

  public static void main(String[] args) {
    RenameHeaderTransformationConfigurationPanel panel = new RenameHeaderTransformationConfigurationPanel();

    panel.setSedaContext(TEST_CONTEXT);
    panel.addRenamePanelEventListener(
      new RenamePanelEventListener() {

        @Override
        public void onRenameConfigurationChanged(Object source) {
          System.err.println("Configuration changed. Valid = " + panel.isValidConfiguration());
        }
      }
    );
    TEST_CONTEXT.setSelectedPaths(Arrays.asList("src/test/resources/fasta/write-test.fa"));
    TestGuiUtils.showComponent(panel);
  }
}
