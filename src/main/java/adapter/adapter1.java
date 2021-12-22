package adapter;

public interface adapter1 {
  interface Logger {
    void log(String message);
  }

  static void sayHello(Logger logger) {
    logger.log("hello");
  }

  static void main(String[] args) {
    Logger logger = System.out::println;
  }
}
