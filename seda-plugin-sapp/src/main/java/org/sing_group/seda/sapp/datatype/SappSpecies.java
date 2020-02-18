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

public enum SappSpecies {
  HOMO_SAPIENS("human", "Homo sapiens"),
  DROSOPHILA_MELANOGASTER("fly", "Drosophila melanogaster"),
  ARABIDOPSIS_THALIANA("arabidopsis", "Arabidopsis thaliana"),
  BRUGIA_MALAYI("brugia", "Brugia malayi"),
  AEDES_AEGYPTI("aedes", "Aedes aegypti"),
  TRIBOLIUM_CASTANEUM("tribolium", "Tribolium castaneum"),
  SCHISTOSOMA_MANSONI("schistosoma", "Schistosoma mansoni"),
  TETRAHYMENA_THERMOPHILA("tetrahymena", "Tetrahymena thermophila"),
  GALDIERIA_SULPHURARIA("galdieria", "Galdieria sulphuraria"),
  ZEA_MAYS("maize", "Zea mays"),
  TOXOPLASMA_GONDII("toxoplasma", "Toxoplasma gondii"),
  CAENORHABDITIS_ELEGANS("caenorhabditis", "Caenorhabditis elegans"),
  ASPERGILLUS_FUMIGATUS("aspergillus_fumigatus", "Aspergillus fumigatus"),
  ASPERGILLUS_NIDULANS("aspergillus_nidulans", "Aspergillus nidulans"),
  ASPERGILLUS_ORYZAE("aspergillus_oryzae", "Aspergillus oryzae"),
  ASPERGILLUS_TERREUS("aspergillus_terreus", "Aspergillus terreus"),
  BOTRYTIS_CINEREA("botrytis_cinerea", "Botrytis cinerea"),
  CANDIDA_ALBICANS("candida_albicans", "Candida albicans"),
  CANDIDA_GUILLIERMONDII("candida_guilliermondii", "Candida guilliermondii"),
  CANDIDA_TROPICALIS("candida_tropicalis", "Candida tropicalis"),
  CHAETOMIUM_GLOBOSUM("chaetomium_globosum", "Chaetomium globosum"),
  COCCIDIOIDES_IMMITIS("coccidioides_immitis", "Coccidioides immitis"),
  COPRINUS_CINEREUS("coprinus", "Coprinus cinereus"),
  CRYPTOCOCCUS_NEOFORMANS_GATTII("cryptococcus_neoformans_gattii", "Cryptococcus neoformans gattii"),
  CRYPTOCOCCUS_NEOFORMANS_NEOFORMANS("cryptococcus_neoformans_neoformans_B", "Cryptococcus neoformans neoformans"),
  CRYPTOCOCCUS_NEOFORMANS("(cryptococcus)", "Cryptococcus neoformans"),
  DEBARYOMYCES_HANSENII("debaryomyces_hansenii", "Debaryomyces hansenii"),
  ENCEPHALITOZOON_CUNICULI("encephalitozoon_cuniculi_GB", "Encephalitozoon cuniculi"),
  EREMOTHECIUM_GOSSYPII("eremothecium_gossypii", "Eremothecium gossypii"),
  FUSARIUM_GRAMINEARUM("fusarium_graminearum", "Fusarium graminearum"),
  FUSARIUM_GRAMINEARIUM("(fusarium)", "Fusarium graminearium"),
  HISTOPLASMA_CAPSULATUM("histoplasma_capsulatum", "Histoplasma capsulatum"),
  KLUYVEROMYCES_LACTIS("kluyveromyces_lactis", "Kluyveromyces lactis"),
  LACCARIA_BICOLOR("laccaria_bicolor", "Laccaria bicolor"),
  PETROMYZON_MARINUS("lamprey", "Petromyzon marinus"),
  LEISHMANIA_TARENTOLAE("leishmania_tarentolae", "Leishmania tarentolae"),
  LODDEROMYCES_ELONGISPORUS("lodderomyces_elongisporus", "Lodderomyces elongisporus"),
  MAGNAPORTHE_GRISEA("magnaporthe_grisea", "Magnaporthe grisea"),
  NEUROSPORA_CRASSA("neurospora_crassa", "Neurospora crassa"),
  PHANEROCHAETE_CHRYSOSPORIUM("phanerochaete_chrysosporium", "Phanerochaete chrysosporium"),
  PICHIA_STIPITIS("pichia_stipitis", "Pichia stipitis"),
  RHIZOPUS_ORYZAE("rhizopus_oryzae", "Rhizopus oryzae"),
  SACCHAROMYCES_CEREVISIAE("saccharomyces_cerevisiae_S288C", "Saccharomyces cerevisiae"),
  SCHIZOSACCHAROMYCES_POMBE("schizosaccharomyces_pombe", "Schizosaccharomyces pombe"),
  TRICHINELLA_SPIRALIS("trichinella", "Trichinella spiralis"),
  USTILAGO_MAYDIS("ustilago_maydis", "Ustilago maydis"),
  YARROWIA_LIPOLYTICA("yarrowia_lipolytica", "Yarrowia lipolytica"),
  NASONIA_VITRIPENNIS("nasonia", "Nasonia vitripennis"),
  SOLANUM_LYCOPERSICUM("tomato", "Solanum lycopersicum"),
  CHLAMYDOMONAS_REINHARDTII("chlamydomonas", "Chlamydomonas reinhardtii"),
  AMPHIMEDON_QUEENSLANDICA("amphimedon", "Amphimedon queenslandica"),
  PNEUMOCYSTIS_JIROVECII("pneumocystis", "Pneumocystis jirovecii");

  private String identifier;
  private String species;

  SappSpecies(String identifier, String species) {
    this.identifier = identifier;
    this.species = species;
  }

  @Override
  public String toString() {
    return this.species;
  }

  public String getIdentifier() {
    return identifier;
  }

  public String getSpecies() {
    return species;
  }
}
