package monad;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public interface monad5 {
  public class Validator<T> {
    private final T t;
    private final ArrayList<Throwable> throwables = new ArrayList<>();
    
    private Validator(T t) {
      this.t = t;
    }

    public T get() throws IllegalStateException {
      if (throwables.isEmpty()) {
        return t;
      }
      IllegalStateException e = new IllegalStateException();
      throwables.forEach(e::addSuppressed);
      throw e;
    }
    
    public Validator<T> validate(Predicate<? super T> validation, String message) {
      try {
        if (!validation.test(t)) {
          throwables.add(new IllegalStateException(message));
        }
      } catch(RuntimeException e) {
        throwables.add(e);
      }
      return this;
    }
    
    public <U> Validator<T> validate(Function<? super T, ? extends U> projection, Predicate<? super U> validation, String message) {
      return validate(projection.andThen(validation::test)::apply, message);
    }
    
    public static <T> Validator<T> of(T t) {
      Objects.requireNonNull(t);
      return new Validator<>(t);
    }
  }
  
  public class User {
    private final String name;
    private final int age;
    
    public User(String name, int age) {
      this.name = name;
      this.age = age;
    }
    
    public String getName() {
      return name;
    }
    public int getAge() {
      return age;
    }
  }
  
  public static IntPredicate inBetween(int start, int end) {
    return value -> value > start && value < end;
  }
  
  public static void main(String[] args) {
    User user = new User("bob", 12);
    //User user = new User("", -12);
    User validatedUser = Validator.of(user)
        .validate(User::getName, Objects::nonNull, "name is null")
        .validate(User::getName, name -> !name.isEmpty(), "name is empty")
        //.validate(User::getAge, age -> age > 0 && age < 150, "age is between 0 and 150")
        .validate(User::getAge, inBetween(0, 150)::test, "age is between 0 and 150")
        .get();
  }
}
