package observer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public interface observer1 {
  static double parseAndSum(Path path) throws IOException {
    try (var lines = Files.lines(path)) {
      return lines
          .flatMap(Pattern.compile(",")::splitAsStream)
          .mapToDouble(Double::parseDouble)
          .sum();
    }
  }
  
  static void main(String[] args) throws IOException {
    var path = Path.of("test/test.csv");
    var value = parseAndSum(path);
    System.out.println(value);
  }
}
