# Railway Switch

The railway switch pattern, quoted by [Scott Wlaschin](https://fsharpforfunandprofit.com/rop/),
is a way to abstract a cascade of **if ... else** to have a mode declarative way of specify
how a value can be obtained.

For example, we may want to determine the value of a hostname by either reading
the environment variable "HOSTNAME" or the Java property "hostname" or the property
"hostname" of a configuration file or to use "localhost".

This can be written that way
```java
public static String findHostname() {
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
```

## Railway switch pattern

The aim of the railway switch pattern is to simplify codes that do a cascade of **if ...  else**
by creating higher level constructs (functions), here `environment()`, `systemProperty()`,
`fileProperty()`, and a way to compose them (the function `or()`).

For our example, we want a code like this
```java
  static String findHostname() {
    return environment("HOSTNAME")
        .or(systemProperty("hostname"))
        .or(fileProperty(Path.of(".config"), "hostname"))
        .find()
        .orElse("localhost");
  }
```

For that, we first create a functional interface (`Finder`) that either return a value or no value
(we use an `Optional` here) and an instance method `or` able to combine the result of two finders.

```java
@FunctionalInterface
public interface Finder {
  Optional<String> find();

  default Finder or(Finder finder) {
    return () -> find().or(finder::find);
  }
}
```

Using a functional interface here allow to delay the computation in order to not do a side effect like reading
the configuration file if the value of the property have been found by one of the function before.

Once we have our interface `Finder` we can rewrite each code that try to find the value as
a method that returns a `Finder`.

```java
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
```
