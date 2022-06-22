/*
 * #%L
 * SEquence DAtaset builder CGA Omega plugin
 * %%
 * Copyright (C) 2017 - 2022 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.cga.execution;

import static java.lang.String.join;

import java.util.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CgaCompiPipelineConfiguration {

  public enum SelectionCriterion {
    CRITERION_1(
      1, "Similarity",
      "Similarity with reference sequence first, in case of a tie, percentage of gaps relative to reference sequence."
    ), CRITERION_2(
      2, "Percentage of gaps",
      "Percentage of gaps relative to reference sequence first, in case of a tie, similarity with reference sequence."
    ), CRITERION_3(
      3, "Mixed",
      "A mixed model with similarity with reference sequence first, but if fewer gaps relative to reference sequence similarity gets a bonus defined by the user. Currently, a bonus of 20, means 2%."
    );

    private int value;
    private String name;
    private String description;

    SelectionCriterion(int value, String name, String description) {
      this.value = value;
      this.name = name;
      this.description = description;
    }

    @Override
    public String toString() {
      return join(". ", Integer.toString(this.value), this.name);
    }

    public int getValue() {
      return value;
    }

    public String getDescription() {
      return description;
    }
  };

  public static final int DEFAULT_MAX_DIST = 10000;
  public static final int DEFAULT_INTRON_BP = 100;
  public static final int DEFAULT_MIN_FULL_NUCLEOTIDE_SIZE = 100;
  public static final SelectionCriterion DEFAULT_SELECTION_CRITERION = SelectionCriterion.CRITERION_1;
  public static final int DEFAULT_SELECTION_CORRECTION = 0;
  public static final boolean DEFAULT_SKIP_PULL_DOCKER_IMAGES = false;

  @XmlElement
  private int maxDist = DEFAULT_MAX_DIST;

  @XmlElement
  private int intronBp = DEFAULT_INTRON_BP;

  @XmlElement
  private int minFullNucleotideSize = DEFAULT_MIN_FULL_NUCLEOTIDE_SIZE;

  @XmlElement
  private SelectionCriterion selectionCriterion = DEFAULT_SELECTION_CRITERION;

  @XmlElement
  private int selectionCorrection = DEFAULT_SELECTION_CORRECTION;

  @XmlElement
  private boolean skipPullDockerImages = DEFAULT_SKIP_PULL_DOCKER_IMAGES;

  public CgaCompiPipelineConfiguration() {
  }

  public CgaCompiPipelineConfiguration(
    int maxDist, int intronBp, int minFullNucleotideSize, SelectionCriterion selectionCriterion,
    int selectionCorrection, boolean skipPullDockerImages
  ) {
    this.maxDist = maxDist;
    this.intronBp = intronBp;
    this.minFullNucleotideSize = minFullNucleotideSize;
    this.selectionCriterion = selectionCriterion;
    this.selectionCorrection = selectionCorrection;
    this.skipPullDockerImages = skipPullDockerImages;
  }

  public int getMaxDist() {
    return maxDist;
  }

  public int getIntronBp() {
    return intronBp;
  }

  public int getMinFullNucleotideSize() {
    return minFullNucleotideSize;
  }

  public SelectionCriterion getSelectionCriterion() {
    return selectionCriterion;
  }

  public int getSelectionCorrection() {
    return selectionCorrection;
  }

  public boolean isSkipPullDockerImages() {
    return skipPullDockerImages;
  }

  @Override
  public int hashCode() {
    return Objects
      .hash(intronBp, maxDist, minFullNucleotideSize, selectionCorrection, selectionCriterion, skipPullDockerImages);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CgaCompiPipelineConfiguration other = (CgaCompiPipelineConfiguration) obj;
    return intronBp == other.intronBp && maxDist == other.maxDist
      && minFullNucleotideSize == other.minFullNucleotideSize && selectionCorrection == other.selectionCorrection
      && selectionCriterion == other.selectionCriterion && skipPullDockerImages == other.skipPullDockerImages;
  }
}
