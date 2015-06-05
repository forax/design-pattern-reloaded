package logger;

import java.util.Objects;

public interface logger3 {
  public interface Logger {
    public void log(String message);
  }
  
  public interface Filter {
    public boolean accept(String message);
  }
  
  public class FilterLogger implements Logger {
    private final Logger logger;
    private final Filter filter;
    public FilterLogger(Logger logger, Filter filter) {
      this.logger = Objects.requireNonNull(logger);
      this.filter = Objects.requireNonNull(filter);
    }
    @Override
    public void log(String message) {
      if (filter.accept(message)) {
        logger.log(message);
      }
    }
  }
  
  public static void main(String[] args) {
    Logger logger = msg -> System.out.println(msg);
    logger.log("hello");
    
    Filter filter = msg -> msg.startsWith("hell");
    FilterLogger filterLogger = new FilterLogger(logger, filter);
    filterLogger.log("hello");
    filterLogger.log("ok");
  }
}
