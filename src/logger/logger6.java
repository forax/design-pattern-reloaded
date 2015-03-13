package logger;

import java.util.Objects;
import java.util.function.Predicate;

public interface logger6 {
  public interface Logger {
    public void log(String message);
    
    public default Logger filter(Predicate<? super String> filter) {
      // signature Logger → Predicate → Logger
      Objects.requireNonNull(filter);
      return message -> {
        if (filter.test(message)) {
          log(message);
        }
      };
    }
  }
  
  public static void main(String[] args) {
    Logger logger = msg -> System.out.println(msg);
    logger.log("hello");
    
    Predicate<String> filter = msg -> msg.startsWith("hell");
    Logger filterLogger = logger.filter(filter);
    filterLogger.log("hello");
    filterLogger.log("ok");
  }
}
