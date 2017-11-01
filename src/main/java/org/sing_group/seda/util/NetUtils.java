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
