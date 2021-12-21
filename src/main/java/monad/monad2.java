package monad;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface monad2 {
  record User(String name, int age) { }

  record Error(IllegalArgumentException e, Error next) { }

  record Validator<V>(V value, Error error) {
    public V orElseThrow() throws IllegalStateException {
      if (error == null) {
        return value;
      }
      var exception = new IllegalStateException();
      Stream.iterate(error, Objects::nonNull, Error::next).map(Error::e).forEach(exception::addSuppressed);
      throw exception;
    }
    
    public Validator<V> check(Predicate<? super V> validation, String message) {
      if (!validation.test(value)) {
        return new Validator<>(value, new Error(new IllegalArgumentException(message), error));
      }
      return this;
    }
  }
  
  static void main(String[] args) {
    var user = new User("bob", 12);
    //var user = new User("", -12);
    var validatedUser = new Validator<>(user, null)
        .check(u -> u.name() != null, "name is null")
        .check(u -> !u.name().isEmpty(), "name is empty")
        .check(u -> u.age() >= 0 && u.age() <= 150, "age is not between 0 and 150")
        .orElseThrow();
  }
}
