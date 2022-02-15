# Builder pattern

By default, when creating an instance, the arguments of the constructor are pass in order,
the association between an argument and a parameter depends on the position of the argument.

By example, if we define a `Spaceship`
```java
record Spaceship(String name, String captain, int torpedoes, int length) {}
```

an instance is created by calling the constructor
```java
var spaceship = new Spaceship("foo", 'baz', 3, 4);
```

The problem is that it's hard to know when reading the last line, which component/property of the record
is initialized with which value, apart taking a look to the definition of the record which can be in another file.

A builder improve the readability of the code by introducing method calls (that have a name)
to initialize each component.
```java
var spaceship = new SpaceshipBuilder()
    .name("USS Enterprise")
    .captain("Kirk")
    .torpedoes(10_000)
    .length(288_646)
    .build()
```

A builder is a mutable class that allows to initialize an object by name.
All the intermediary methods return the builder itself (`this`) so the method calls can be chained. 

``mermaid
classDiagram
class Spaceship {
  <<record>>
  String name
  String captain
  int torpedoes
  int length
}
class SpaceshipBuilder {
  name(String name) SpaceshipBuilder
  captain(String captain) SpaceshipBuilder
  torpedoes(int torpedoes) SpaceshipBuilder
  length(int length) SpaceshipBuilder
  Spaceship build()
}
SpaceshipBuilder ..> Spaceship : creates
```

```java
public class SpaceshipBuilder {
  private String name;
  private String captain;
  private int torpedoes = -1;
  private int length = -1;

  public SpaceshipBuilder name(String name) {
    this.name = name;
    return this;
  }
  public SpaceshipBuilder captain(String captain) {
    this.captain = captain;
    return this;
  }
  public SpaceshipBuilder torpedoes(int torpedoes) {
    this.torpedoes = torpedoes;
    return this;
  }
  public SpaceshipBuilder length(int length) {
    this.length = length;
    return this;
  }
    
  public Spaceship build() {
    if (name == null || captain == null || torpedoes == -1 || length == -1) {
      throw new IllegalStateException("name, captain, torpedoes or length not initialized");
    }
    return new Spaceship(name, captain, torpedoes, length);
  }
}
```

The main issue with this pattern is that it requires usually quite a lot of code and that IDEs does not track
the fact that if a component of the record is renamed, the method of the builder should be renamed too.


## A generic builder

Using reflection, it is possible to implement a generic builder that avoid those pitfalls
at the price of making the code slower

```java
var spaceship = new Builder<>(MethodHandles.lookup(), Spaceship.class)
    .with(Spaceship::name, "USS Enterprise")
    .with(Spaceship::captain, "Kirk")
    .with(Spaceship::torpedoes, 10_000)
    .with(Spaceship::length, 288_646)
    .build()
```

A simple implementation is available here [builder2.java](builder2.java).
