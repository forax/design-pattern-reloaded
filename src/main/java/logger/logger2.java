package logger;

import java.util.Objects;

public interface logger2 {
  public interface Logger {
    public void log(String message);
  }
  
  public interface Filter {
    public boolean accept(String message);
  }
  
  public abstract class FilterLogger implements Logger, Filter {
    private final Logger logger;
    public FilterLogger(Logger logger) {
      this.logger = Objects.requireNonNull(logger);
    }
    @Override
    public void log(String message) {
      if (accept(message)) {
        logger.log(message);
      }
    }
    @Override
    public abstract boolean accept(String message);
  }
  
  public static void main(String[] args) {
    Logger logger = msg -> System.out.println(msg);
    logger.log("hello");
    
    FilterLogger filterLogger = new FilterLogger(logger) {
      @Override
      public boolean accept(String message) {
        return message.startsWith("hell");
      }
    };
    filterLogger.log("hello");
    filterLogger.log("ok");
  }
}
