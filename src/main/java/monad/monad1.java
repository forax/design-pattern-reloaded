package monad;

public interface monad1 {
  record User(String name, int age) { }

  static void validateUser(User user) {
    if (user.name().isEmpty()) {
      throw new IllegalArgumentException("name is empty");
    }
    if (!(user.age() > 0 && user.age() < 150)) {
      throw new IllegalArgumentException("age is not between 0 and 150");
    }
  }

  static void main(String[] args) {   // no monad
    var user = new User("bob", 12);
    //var user = new User("", -12);
    validateUser(user);
  }
}
