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
package org.sing_group.seda.bio;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionUtil {

  public static <T, R> Function<T, R> wrapWithExceptionToNull(Function<T, R> function) {
    return wrapWithExceptionToNull(function, e -> {});
  }

  public static <T, R> Function<T, R> wrapWithExceptionToNull(Function<T, R> function, Consumer<Throwable> exceptionManager) {
    return param -> {
      try {
        return function.apply(param);
      } catch (Throwable e) {
        exceptionManager.accept(e);
        return null;
      }
    };
  }

  public static <T, R> Function<T, R> wrapWithExceptionToNull(Function<T, R> function, BiConsumer<T, Throwable> exceptionManager) {
    return param -> {
      try {
        return function.apply(param);
      } catch (Throwable e) {
        exceptionManager.accept(param, e);
        return null;
      }
    };
  }

}
