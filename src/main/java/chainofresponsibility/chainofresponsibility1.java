package chainofresponsibility;

public interface chainofresponsibility1 {
  enum Level {
    INFO, WARNING, ERROR
  }

  interface Logger {
    void log(Level messageLevel, String message);
  }

  record ConsoleLogger(Level level, Logger logger) implements Logger {
    @Override
    public void log(Level messageLevel, String message) {
      if (messageLevel.compareTo(level) >= 0) {
        System.out.println("log on console: " + message);
      }
      if (logger != null) {
        logger.log(messageLevel, message);
      }
    }
  }

  record FileLogger(Level level, Logger logger) implements Logger {
    @Override
    public void log(Level messageLevel, String message) {
      if (messageLevel.compareTo(level) >= 0) {
        System.out.println("log on file: " + message);
      }
      if (logger != null) {
        logger.log(messageLevel, message);
      }
    }
  }
  
  static void main(String[] args) {
    var logger = new FileLogger(Level.WARNING, new ConsoleLogger(Level.INFO, null));
    logger.log(Level.ERROR, "database connection error");
    logger.log(Level.INFO, "listen on port 7777");
  }
}
