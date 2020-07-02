/*
 * #%L
 * SEquence DAtaset builder BLAST plugin
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
package org.sing_group.seda.blast.ncbi;

import org.sing_group.seda.blast.datatype.SequenceType;

/*
 * https://ncbi.github.io/blast-cloud/blastdb/available-blastdbs.html
 */
public enum NcbiBlastDatabase {
  NT("nt", "Nucleotide collection", SequenceType.NUCLEOTIDES),
  NR("nr", "Non-redundant", SequenceType.PROTEINS),
  REFSEQ_RNA("refseq_rna", "NCBI Transcript Reference Sequences ", SequenceType.NUCLEOTIDES),
  REFSEQ_PROTEIN("refseq_protein", "NCBI Protein Reference Sequences", SequenceType.PROTEINS),
  SWISSPROT("swissprot", "Non-redundant UniProtKB/SwissProt sequences", SequenceType.PROTEINS),
  PDBAA("pdbaa", "PDB protein database", SequenceType.PROTEINS),
  PDBNT("pdbnt", "PDB nucleotide database", SequenceType.NUCLEOTIDES);

  private String name;
  private String title;
  private SequenceType sequenceType;

  NcbiBlastDatabase(String name, String title, SequenceType sequenceType) {
    this.name = name;
    this.title = title;
    this.sequenceType = sequenceType;
  }

  public String getName() {
    return name;
  }

  public String getTitle() {
    return title;
  }

  public SequenceType getSequenceType() {
    return sequenceType;
  }
  
  @Override
  public String toString() {
    return this.title;
  }
}
