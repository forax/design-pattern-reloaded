# Observer Pattern

The point of the observer pattern is to decouple two pieces of code by using an interface in the middle.

Let say we have a method that parse a CSV file and compute the sum of all datas
```java
static double parseAndSum(Path path) throws IOException {
  try (var lines = Files.lines(path)) {
    return lines
        .flatMap(Pattern.compile(",")::splitAsStream)
        .mapToDouble(Double::parseDouble)
        .sum();
  }
}
```

This code is hard to reuse because it is too specialized, it can be split into two parts,
a reusable par that parse a CSV file and a specific part that compute the sum of the data.

Because we want to be able to reuse the part that parse a CSV file,
we introduce an interface `Observer` that is called each time a new data is found.

```java
interface Observer {
  void data(double value);
}
```

With that, wa can extract the CVS parsing into its own class `CSVParser`.

```java
public class CSVParser {
  public static void parse(Path path, Observer observer) throws IOException {
    try (var lines = Files.lines(path)) {
      lines
          .flatMap(Pattern.compile(",")::splitAsStream)
          .mapToDouble(Double::parseDouble)
          .forEach(observer::data);
    }
  }
}
```

and rewrite the method `parseAndSum()` to use the `CSVParser`.

```java
static double parseAndSum(Path path) throws IOException {
  var box = new Object() { int sum; };
  CSVParser.parse(path, value -> box.sum += value);
  return box.sum;
}
```

The Observer Pattern allow to decouple/untangle a code to create an on the shelf class that can be reused
because it depends on an interface.

