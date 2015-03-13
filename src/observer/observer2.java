package observer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface observer2 {
  public class SumCSV {
    public static double parseAndSum(Path path) throws IOException {
      try (Stream<String> lines = Files.lines(path)) {
        return lines
            .flatMap(Pattern.compile(",")::splitAsStream)
            .mapToDouble(Double::parseDouble)
            .sum();
      }
    }
  }
  
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("test/test.csv");
    double value = SumCSV.parseAndSum(path);
    System.out.println(value);
  }
}
