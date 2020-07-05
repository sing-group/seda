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
package org.sing_group.seda.blast.uniprot;

import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.BlastService;
import uk.ac.ebi.uniprot.dataservice.client.alignment.blast.input.DatabaseOption;

public class BlastServiceFactory {

  public static BlastService<?> getBlastService(DatabaseOption database) {
    ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();

    if (database.equals(DatabaseOption.UNIPARC)) {
      return serviceFactoryInstance.getUniParcBlastService();
    } else if (database.getDisplayName().toUpperCase().contains("UNIREF")) {
      return serviceFactoryInstance.getUniRefBlastService();
    } else {
      return serviceFactoryInstance.getUniProtBlastService();
    }
  }
}
