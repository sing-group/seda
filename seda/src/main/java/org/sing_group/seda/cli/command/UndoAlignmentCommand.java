package org.sing_group.seda.cli.command;

import java.io.File;
import java.io.IOException;

import org.sing_group.seda.core.io.JsonObjectReader;
import org.sing_group.seda.gui.reformat.ReformatFastaTransformationProvider;
import org.sing_group.seda.gui.undoalignment.UndoAlignmentTransformationProvider;
import org.sing_group.seda.plugin.core.UndoAlignmentSedaPluginInfo;
import org.sing_group.seda.plugin.spi.TransformationProvider;

import es.uvigo.ei.sing.yacli.command.parameter.Parameters;

public class UndoAlignmentCommand extends ReformatFastaCommand {

  @Override
  public String getName() {
    return UndoAlignmentSedaPluginInfo.SHORT_NAME;
  }

  @Override
  public String getDescriptiveName() {
    return UndoAlignmentSedaPluginInfo.NAME;
  }

  @Override
  public String getDescription() {
    return UndoAlignmentSedaPluginInfo.DESCRIPTION;
  }

  @Override
  protected TransformationProvider getTransformation(Parameters parameters) {
    UndoAlignmentTransformationProvider provider = new UndoAlignmentTransformationProvider();

    provider.setReformatFastaTransformationProvider(
      (ReformatFastaTransformationProvider) super.getTransformation(parameters)
    );

    return provider;
  }

  @Override
  protected TransformationProvider getTransformation(File parametersFile) throws IOException {
    return new JsonObjectReader<UndoAlignmentTransformationProvider>()
      .read(parametersFile, UndoAlignmentTransformationProvider.class);
  }
}
