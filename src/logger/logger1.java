package logger;

public interface logger1 {
  public interface Logger {
    public void log(String message);
  }
  
  public static void main(String[] args) {
    Logger logger = msg -> System.out.println(msg);
    logger.log("hello");
  }
}
