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
package org.sing_group.seda.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetUtils {

  public static final boolean isHostAvailable(String hostName) {
    return isHostAvailable(hostName, 5000);
  }

  public static final boolean isHostAvailable(String hostName, int timeout) {
    try (Socket socket = new Socket()) {
      int port = 80;
      InetSocketAddress socketAddress = new InetSocketAddress(hostName, port);
      socket.connect(socketAddress, timeout);

      return true;
    } catch (UnknownHostException unknownHost) {
      return false;
    } catch (IOException e) {
      return false;
    }
  }
}
