package adapter;

import static adapter.adapter2.Level.ERROR;

public interface adapter2 {
  public interface Logger {
    public void log(String message);
  }
  
  public enum Level { WARNING, ERROR }
  public interface Logger2 {
    public void log(Level level, String message);

    public default Logger level(Level level) {
      return msg -> log(level, msg);
    }
  }
  
  public static void main(String[] args) {
    Logger2 logger2 = (level, msg) -> System.out.println(level + " " + msg);
    logger2.log(ERROR, "abort abort !");
    
    logger2.level(ERROR).log("abort abort !");
  }
}
