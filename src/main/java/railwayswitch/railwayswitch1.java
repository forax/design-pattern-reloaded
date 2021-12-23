package railwayswitch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public interface railwayswitch1 {
  static String findHostname() {
    // 1
    var hostName = System.getenv("HOSTNAME");
    if (hostName != null) {
      return hostName;
    }

    // 2
    hostName = System.getProperty("hostname");
    if (hostName != null) {
      return hostName;
    }

    // 3
    var properties = new Properties();
    try (var reader = Files.newBufferedReader(Path.of(".config"))) {
      properties.load(reader);
      hostName = properties.getProperty("hostname");
    } catch (IOException e) {
      hostName = null;
    }
    if (hostName != null) {
      return hostName;
    }

    // 4
    return "localhost";
  }
  
  static void main(String[] args) {
    var hostname = findHostname();
    System.out.println(hostname);
  }
}
