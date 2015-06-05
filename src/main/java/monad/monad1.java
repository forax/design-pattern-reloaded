package monad;

public interface monad1 {
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
  
  public static void main(String[] args) {   // no monad
    User user = new User("bob", 12);
    //User user = new User("", -12);
    if (user.getName() == null) {
      throw new IllegalStateException("name is null");
    }
    if (user.getName().isEmpty()) {
      throw new IllegalStateException("name is empty");
    }
    if (!(user.getAge() > 0 && user.getAge() < 150)) {
      throw new IllegalStateException("age is between 0 and 150");
    }
  }
}
