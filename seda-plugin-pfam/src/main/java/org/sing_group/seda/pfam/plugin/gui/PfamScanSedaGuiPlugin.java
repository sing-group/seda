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
package org.sing_group.seda.pfam.plugin.gui;

import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.DESCRIPTION;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.GROUP;
import static org.sing_group.seda.pfam.core.PfamScanSedaPluginInfo.NAME;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.core.io.JsonObjectWriter;
import org.sing_group.seda.pfam.gui.PfamScanTransformationConfigurationPanel;
import org.sing_group.seda.pfam.transformations.provider.PfamScanTransformationProvider;
import org.sing_group.seda.plugin.core.gui.AbstractSedaGuiPlugin;
import org.sing_group.seda.plugin.spi.TransformationProvider;

public class PfamScanSedaGuiPlugin extends AbstractSedaGuiPlugin {
  private PfamScanTransformationConfigurationPanel pfamScan =
    new PfamScanTransformationConfigurationPanel();

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getGroupName() {
    return GROUP;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public Component getEditor() {
    return this.pfamScan;
  }

  @Override
  public TransformationProvider getTransformation() {
    return this.pfamScan.getTransformationProvider();
  }

  @Override
  public boolean canSaveTransformation() {
    return true;
  }

  @Override
  public void saveTransformation(File file) throws IOException {
    new JsonObjectWriter<PfamScanTransformationProvider>()
      .write(this.pfamScan.getTransformationProvider(), file);
  }

  @Override
  public void loadTransformation(File file) throws IOException {
    this.pfamScan.setTransformationProvider(
      new JsonObjectReader<PfamScanTransformationProvider>()
        .read(file, PfamScanTransformationProvider.class)
    );
  }
}
