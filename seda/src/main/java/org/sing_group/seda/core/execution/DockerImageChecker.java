/*
 * #%L
 * SEquence DAtaset builder
 * %%
 * Copyright (C) 2017 - 2019 Jorge Vieira, Cristina Vieira, Noé Vázquez, Miguel Reboiro-Jato and Hugo López-Fernández
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
package org.sing_group.seda.core.execution;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DockerImageChecker {
  public final long CHECKED_IMAGE_VALID_TIME = 5;
  private final Map<String, Date> checkedImagesMap = new HashMap<>();

  private static DockerImageChecker INSTANCE;

  public static final synchronized DockerImageChecker getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new DockerImageChecker();
    }
    return INSTANCE;
  }

  public boolean shouldCheckDockerImage(String dockerImage) {
    if (checkedImagesMap.containsKey(dockerImage)) {
      Date now = getNow();
      long diffMillis = now.getTime() - checkedImagesMap.get(dockerImage).getTime();
      if (TimeUnit.MINUTES.convert(diffMillis, TimeUnit.MILLISECONDS) < CHECKED_IMAGE_VALID_TIME) {
        return false;
      } else {
        checkedImagesMap.remove(dockerImage);
        return true;
      }
    } else {
      return true;
    }
  }

  public void storeImageTimestamp(String dockerImage) {
    checkedImagesMap.put(dockerImage, getNow());
  }

  private Date getNow() {
    return Calendar.getInstance().getTime();
  }
}
