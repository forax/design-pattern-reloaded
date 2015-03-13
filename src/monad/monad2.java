package monad;

import java.util.Objects;
import java.util.function.Predicate;

public interface monad2 {
  public class Validator<T> {
    private final T t;
    private final IllegalStateException error;
    
    private Validator(T t, IllegalStateException error) {
      this.t = t;
      this.error = error;
    }

    public T get() throws IllegalStateException {
      if (error == null) {
        return t;
      }
      throw error;
    }
    
    public Validator<T> validate(Predicate<? super T> validation, String message) {
      if (!validation.test(t)) {
        return new Validator<>(t, new IllegalStateException(message));
      }
      return this;
    }
    
    public static <T> Validator<T> of(T t) {
      Objects.requireNonNull(t);
      return new Validator<>(t, null);
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
  
  public static void main(String[] args) {
    User user = new User("bob", 12);
    //User user = new User("", -12);
    User validatedUser = Validator.of(user)
        .validate(u -> u.getName() != null, "name is null")
        .validate(u -> !u.getName().isEmpty(), "name is empty")
        .validate(u -> u.getAge() > 0 && u.getAge() < 150, "age is between 0 and 150")
        .get();
  }
}
