package logger;

import java.util.Objects;

public interface logger5 {
  public interface Logger {
    public void log(String message);
    
    public default Logger filter(Filter filter) {
      // signature Logger → Filter → Logger
      Objects.requireNonNull(filter);
      return message -> {
        if (filter.accept(message)) {
          log(message);
        }
      };
    }
  }
  
  public interface Filter {
    public boolean accept(String message);
  }
  
  public static void main(String[] args) {
    Logger logger = msg -> System.out.println(msg);
    logger.log("hello");
    
    Filter filter = msg -> msg.startsWith("hell");
    Logger filterLogger = logger.filter(filter);
    filterLogger.log("hello");
    filterLogger.log("ok");
  }
}
