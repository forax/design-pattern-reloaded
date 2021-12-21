package monad;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public interface monad3 {
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

    public <U> Validator<V> check(Function<? super V, ? extends U> projection, Predicate<? super U> validation, String message) {
      return check(projection.andThen(validation::test)::apply, message);
    }
  }

  static IntPredicate inBetween(int start, int end) {
    return value -> value >= start && value <= end;
  }

  static void main(String[] args) {
    var user = new User("bob", 12);
    //var user = new User("", -12);
    var validatedUser = new Validator<>(user, null)
        .check(User::name, Objects::nonNull, "name is null")
        .check(User::name, not(String::isEmpty), "name is empty")
        .check(User::age,  inBetween(0, 150)::test, "age is not between 0 and 150")
        .orElseThrow();
  }
}
