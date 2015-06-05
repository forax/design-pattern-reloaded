package logger;

import java.util.Objects;

public interface logger4 {
  public interface Logger {
    public void log(String message);
  }
  
  public interface Filter {
    public boolean accept(String message);
  }
  
  public class Loggers {
    public static Logger filterLogger(Logger logger, Filter filter) {
                                    // signature Logger → Filter → Logger
       Objects.requireNonNull(logger);
       Objects.requireNonNull(filter);
       return message -> {
         if (filter.accept(message)) {
           logger.log(message);
         }
       };
    }
  }
  
  public static void main(String[] args) {
    Logger logger = msg -> System.out.println(msg);
    logger.log("hello");
    
    Filter filter = msg -> msg.startsWith("hell");
    Logger filterLogger = Loggers.filterLogger(logger, filter);
    filterLogger.log("hello");
    filterLogger.log("ok");
  }
}
