package org.sing_group.seda.gui.reformat;

import java.util.HashMap;
import java.util.Map;

import org.sing_group.seda.datatype.DatatypeFactory;
import org.sing_group.seda.datatype.Sequence;
import org.sing_group.seda.datatype.SequenceCase;
import org.sing_group.seda.datatype.SequencesGroup;
import org.sing_group.seda.plugin.spi.AbstractTransformationProvider;
import org.sing_group.seda.transformation.dataset.ComposedSequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.dataset.SequencesGroupDatasetTransformation;
import org.sing_group.seda.transformation.sequence.ChangePropertiesSequenceTransformation;
import org.sing_group.seda.transformation.sequence.SequenceTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ChangePropertiesSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.ComposedSequencesGroupTransformation;
import org.sing_group.seda.transformation.sequencesgroup.SequencesGroupTransformation;

public class ReformatFastaConfigurationModel extends AbstractTransformationProvider {

  private boolean removeLineBreaks = false;
  private int fragmentLength = 80;
  private LineBreakType lineBreakType = LineBreakType.defaultType();
  private SequenceCase sequenceCase = SequenceCase.defaultType();

  public boolean isRemoveLineBreaks() {
    return removeLineBreaks;
  }

  public void setRemoveLineBreaks(boolean removeLineBreaks) {
    if (removeLineBreaks != this.removeLineBreaks) {
      this.removeLineBreaks = removeLineBreaks;
      fireTransformationsConfigurationModelEvent(
        ReformatConfigurationChangeType.REMOVE_LINE_BREAKS_CHANGED, this.removeLineBreaks
      );
    }
  }

  public int getFragmentLength() {
    return fragmentLength;
  }

  public void setFragmentLength(int fragmentLength) {
    if (this.fragmentLength != fragmentLength) {
      this.fragmentLength = fragmentLength;
      fireTransformationsConfigurationModelEvent(
        ReformatConfigurationChangeType.FRAGMENT_LENGTH_CHANGED, this.fragmentLength
      );
    }
  }

  public LineBreakType getLineBreakType() {
    return lineBreakType;
  }

  public void setLineBreakType(LineBreakType lineBreakType) {
    if (!this.lineBreakType.equals(lineBreakType)) {
      this.lineBreakType = lineBreakType;
      fireTransformationsConfigurationModelEvent(
        ReformatConfigurationChangeType.LINE_BREAK_TYPE_CHANGED, this.lineBreakType
      );
    }
  }

  @Override
  public boolean isValidTransformation() {
    return this.removeLineBreaks || isValidFragmentLength();
  }

  private boolean isValidFragmentLength() {
    return this.fragmentLength >= 0;
  }

  @Override
  public SequencesGroupDatasetTransformation getTransformation(DatatypeFactory factory) {
    SequenceTransformation transformation =
      new ChangePropertiesSequenceTransformation(factory, getSequencePropertiesMap());

    SequencesGroupTransformation groupTransformation =
      new ChangePropertiesSequencesGroupTransformation(factory, getGroupPropertiesMap());

    return new ComposedSequencesGroupDatasetTransformation(
      factory, new ComposedSequencesGroupTransformation(factory, transformation), groupTransformation
    );
  }

  private Map<String, Object> getSequencePropertiesMap() {
    Map<String, Object> toret = new HashMap<>();

    if (getChainColumns() != 0) {
      toret.put(Sequence.PROPERTY_CHAIN_COLUMNS, getChainColumns());
    }

    if(!getSequenceCase().equals(SequenceCase.ORIGINAL)) {
      toret.put(Sequence.PROPERTY_CASE, getSequenceCase());
    }

    return toret;
  }

  private Integer getChainColumns() {
    return this.removeLineBreaks ? 0 : this.fragmentLength;
  }

  private Map<String, Object> getGroupPropertiesMap() {
    Map<String, Object> toret = new HashMap<>();
    if (getChainColumns() != 0) {
      toret.put(SequencesGroup.PROPERTY_LINE_BREAK_OS, getLineBreak());
    }

    return toret;
  }

  private String getLineBreak() {
    return getLineBreakType().getLineBreak();
  }

  public SequenceCase getSequenceCase() {
    return sequenceCase;
  }

  public void setSequenceCase(SequenceCase sequenceCase) {
    if (!this.sequenceCase.equals(sequenceCase)) {
      this.sequenceCase = sequenceCase;
      fireTransformationsConfigurationModelEvent(
        ReformatConfigurationChangeType.SEQUENCE_CASE_CHANGED, this.sequenceCase
      );
    }
  }
}
