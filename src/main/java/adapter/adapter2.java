package adapter;

public interface adapter2 {
  interface Logger {
    void log(String message);
  }
  
  enum Level { WARNING, ERROR }
  interface Logger2 {
    void log(Level level, String message);

    default Logger adapt(Level level) {
      return msg -> log(level, msg);
    }
  }
  
  public static void main(String[] args) {
    Logger2 logger2 = (level, msg) -> System.out.println(level + " " + msg);
    logger2.log(Level.ERROR, "abort abort !");

    Logger logger = logger2.adapt(Level.ERROR);
    logger.log("abort abort !");
  }
}
