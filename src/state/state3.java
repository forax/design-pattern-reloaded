package state;

public interface state3 {
  interface Logger {
    enum Level { ERROR, WARNING }
    
    void error(String message);
    default void warning(String message) {
      error(message);
    }
    
    /*private JDK9 */ interface QuietLogger extends Logger {
      @Override
      default void warning(String message) {
        // empty
      }
      
      @Override
      default Logger quiet() {
        return this;
      }
      @Override
      default Logger all() {
        return msg -> error(msg);
      }
    }
    
    default Logger quiet() {
      return (QuietLogger)msg -> error(msg);
    }
    default Logger all() {
      return this;
    }
  }
  
  public static void main(String[] args) {
    Logger logger = System.out::println;
    logger.error("ERROR");
    logger.warning("WARNING");
    
    Logger quiet = logger.quiet();
    quiet.error("ERROR");
    quiet.warning("WARNING");
    
    Logger logger2 = quiet.all();
    logger2.error("ERROR");
    logger2.warning("WARNING");
  }
}
