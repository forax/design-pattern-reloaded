# Template Method Pattern

The idea of the template method pattern is to decompose a problem in two parts, a generic reusable part
and a specific part.

## Using inheritance

Let's say our generic part add hearts to a String and the specific part returns the string to be decorated. 

Using an abstract class `DecoratedString, the abstract method `plainString()` returns the string to be decorated
and the method `loveString()` the generic part that adds a heart before and after the string.
```java
abstract class DecoratedString {
  protected abstract String plainString();

  public String loveString() {
    return "❤️ " + plainString() + " ❤️";
  }
}
```

and the specific part use inheritance (here with an anonymous class) to specify the string to be decorated.
```java
static void main(String[] args) {
  var decoratedString = new DecoratedString() {
    @Override
    protected String plainString() {
      return "hello";
    }
  };
  System.out.println(decoratedString.loveString());
}
```

Using inheritance here as usual is not the best solution because the API of `DecoratedString` contains
both methods even if it may not make sense. In particular, inside the method `plainString()`,
one can call the method `loveString()` leading to a recursive loop that will blow the stack.


## Using delegation

A better design is to use delegation instead of inheritance.
The class `DecoratedString` takes a `Supplier` at construction and call it in `loveString()`.
```java
class DecoratedString {
  private final Supplier<String> supplier;

  public DecoratedString(Supplier<String> supplier) {
    this.supplier = supplier;
  }

  public String loveString() {
    return "❤️ " + supplier.get() + " ❤️";
  }
}
```

A use site, the supplier can be specified as a lambda
```java
static void main(String[] args) {
  var decoratedString = new DecoratedString(() -> "hello");
  System.out.println(decoratedString.loveString());
}
```

The [Memoizer Pattern](../memoizer) is a more advanced example of template method pattern.


