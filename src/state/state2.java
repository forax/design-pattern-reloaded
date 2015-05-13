package state;

import java.util.function.Consumer;

public interface state2 {
  interface Logger {
    enum Level { ERROR, WARNING }
    
    public void error(String message);
    public void warning(String message);
    
    Logger quiet();
    Logger normal();
  }
  
  class Loggers {
    static Logger logger(Consumer<? super String> printer) {
      return new NormalLogger(printer);
    }
  }
  
  /* private JDK9 */ class NormalLogger implements Logger {
    
    final Consumer<? super String> printer;
    
    NormalLogger(Consumer<? super String> printer) {
      this.printer = printer;
    }
    
    @Override
    public void error(String message) {
      printer.accept(message);
    }
    @Override
    public void warning(String message) {
      printer.accept(message);
    }
    
    @Override
    public Logger quiet() {
      return new NormalLogger(printer) {
        @Override
        public void warning(String message) {
          // empty
        }
        
        @Override
        public Logger quiet() {
          return this;
        }
        @Override
        public Logger normal() {
          return NormalLogger.this;
        }
      };
    }
    @Override
    public Logger normal() {
      return this;
    }
  }
  
  public static void main(String[] args) {
    Logger logger = Loggers.logger(new Consumer<String>() {
      @Override
      public void accept(String msg) {
        System.out.println(msg);
      }
    });
    logger.error("ERROR");
    logger.warning("WARNING");
    
    Logger quiet = logger.quiet();
    quiet.error("ERROR");
    quiet.warning("WARNING");
    
    Logger logger2 = quiet.normal();
    logger2.error("ERROR");
    logger2.warning("WARNING");
  }
}
