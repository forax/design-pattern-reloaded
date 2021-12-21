package observer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public interface observer2 {
  interface Observer {
    void data(double value);
  }
  
  class CSVParser {
    public static void parse(Path path, Observer observer) throws IOException {
      try (var lines = Files.lines(path)) {
        lines
            .flatMap(Pattern.compile(",")::splitAsStream)
            .mapToDouble(Double::parseDouble)
            .forEach(observer::data);
      }
    }
  }

  static double parseAndSum(Path path) throws IOException {
    var box = new Object() { int sum; };
    CSVParser.parse(path, value -> box.sum += value);
    return box.sum;
  }
  
  static void main(String[] args) throws IOException {
    var path = Path.of("test/test.csv");
    var value = parseAndSum(path);
    System.out.println(value);
  }
}
