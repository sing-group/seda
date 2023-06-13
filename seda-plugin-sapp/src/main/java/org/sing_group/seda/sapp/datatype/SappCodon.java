/*
 * #%L
 * SEquence DAtaset builder SAPP (Semantic genome Annotation Platform with Provenance) plugin
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
package org.sing_group.seda.sapp.datatype;

public enum SappCodon {
  STANDARD(0, "Standard"),
  STANDARD_ALT(1, "Standard (with alternative initiation codons)"),
  VERTEBRATE_MIT_(2, "Vertebrate Mitochondrial"),
  YEAS_MIT(3, "Yeast Mitochondrial"),
  MOLD_PROT_COEL_MYC(4, "Mold, Protozoan, Coelenterate Mitochondrial and Mycoplasma/Spiroplasma"),
  INVERTEBRATE_MIT(5, "Invertebrate Mitochondrial"),
  CIL_DASY(6, "Ciliate Macronuclear and Dasycladacean"),
  ECHINO_MIT(9, "Echinoderm Mitochondrial"),
  EUPLOTID(10, "Euplotid Nuclear"),
  BACTERIAL(11, "Bacterial"),
  ALT_YEAST_NUC(12, "Alternative Yeast Nuclear"),
  ASCID_MIT(13, "Ascidian Mitochondrial"),
  FLAT_MIT(14, "Flatworm Mitochondrial"),
  BLEPHARISMA(15, "Blepharisma Macronuclear"),
  CHLORO_MIT(16, "Chlorophycean Mitochondrial"),
  TREMA_MIT(21, "Trematode Mitochondrial"),
  SCENE(22, "Scenedesmus obliquus"),
  THRAUS_MIT(23, "Thraustochytrium Mitochondrial");

  private int paramValue;
  private String description;

  SappCodon(int paramValue, String description) {
    this.paramValue = paramValue;
    this.description = description;
  }

  @Override
  public String toString() {
    return this.paramValue + ". " + this.description;
  }

  public int getParamValue() {
    return this.paramValue;
  }
  
  public String getDescription() {
    return description;
  }
}
