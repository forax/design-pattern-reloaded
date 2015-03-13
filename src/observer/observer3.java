package observer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public interface observer3 {
  public interface Observer {
    public void data(double value);
  }
  
  public class CSVParser {
    public static void parse(Path path, Observer observer) throws IOException {
      try (Stream<String> lines = Files.lines(path)) {
        lines
            .flatMap(Pattern.compile(",")::splitAsStream)
            .mapToDouble(Double::parseDouble)
            .forEach(observer::data);
      }
    }
  }
  
  public class SumCSV {
    private double sum;
    public double parseAndSum(Path path) throws IOException {
      CSVParser.parse(path, value -> sum += value);
      return sum;
    }
  }
  
  public static void main(String[] args) throws IOException {
    Path path = Paths.get("test/test.csv");
    double value = new SumCSV().parseAndSum(path);
    System.out.println(value);
  }
}
