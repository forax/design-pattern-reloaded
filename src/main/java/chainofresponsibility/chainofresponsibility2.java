package chainofresponsibility;

public interface chainofresponsibility2 {
  enum Level {
    INFO, WARNING, ERROR
  }

  interface Logger {
    void log(Level messageLevel, String message);

    default Logger withLevel(Level level) {
      return (messageLevel, message) -> {
        if (messageLevel.compareTo(level) >= 0) {
          log(messageLevel, message);
        }
      };
    }

    default Logger withLogger(Logger logger) {
      return (messageLevel, message) -> {
        log(messageLevel, message);
        logger.log(messageLevel, message);
      };
    }

    static Logger console() {
      return (messageLevel, message) -> System.out.println("log on console: " + message);
    }

    static Logger file() {
      return (messageLevel, message) -> System.out.println("log on file: " + message);
    }
  }
  
  static void main(String[] args) {
    var logger = Logger.file().withLevel(Level.WARNING)
        .withLogger(Logger.console().withLevel(Level.INFO));
    logger.log(Level.ERROR, "database connection error");
    logger.log(Level.INFO, "listen on port 7777");
  }
}
