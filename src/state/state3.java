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
      default Logger chatty() {
        //return msg -> error(msg);
        return this::error;
      }
    }
    
    default Logger quiet() {
      //return (QuietLogger)msg -> error(msg);
      return (QuietLogger)this::error;
    }
    default Logger chatty() {
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
    
    Logger logger2 = quiet.chatty();
    logger2.error("ERROR");
    logger2.warning("WARNING");
  }
}
