package railwayswitch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;

public interface railwayswitch2 {
  interface Finder {
    Optional<String> find();

    default Finder or(Finder finder) {
      return () -> find().or(finder::find);
    }
  }

  static Finder environment(String name) {
    return () -> Optional.ofNullable(System.getenv(name));
  }

  static Finder systemProperty(String name) {
    return () -> Optional.ofNullable(System.getProperty(name));
  }

  static Finder fileProperty(Path path, String name) {
    return () -> {
      var properties = new Properties();
      try (var reader = Files.newBufferedReader(path)) {
        properties.load(reader);
        return Optional.ofNullable(properties.getProperty(name));
      } catch (IOException e) {
        return Optional.empty();
      }
    };
  }

  static Finder hostnameFinder() {
    return environment("HOSTNAME")
        .or(systemProperty("hostname"))
        .or(fileProperty(Path.of(".config"), "hostname"));
  }
  
  static void main(String[] args) {
    var finder = hostnameFinder();
    var hostname = finder.find().orElse("localhost");
    System.out.println(hostname);
  }
}
