package state;

import static java.util.function.Function.identity;

import java.util.function.Consumer;
import java.util.function.Function;

public interface state4 {
  class Logger {
    enum Level { ERROR, WARNING }
    
    private final Consumer<? super String> error;
    private final Consumer<? super String> warning;
    private final Logger quiet;
    private final Logger chatty;
    
    private Logger(Consumer<? super String> error, Consumer<? super String> warning,
        Function<Logger, Logger> quietFactory, Function<Logger, Logger> chattyFactory) {
      this.error = error;
      this.warning = warning;
      this.quiet = quietFactory.apply(this);
      this.chatty = chattyFactory.apply(this);
    }
    
    public void error(String message) {
      error.accept(message);
    }
    public void warning(String message) {
      warning.accept(message);
    }
    public Logger quiet() {
      return quiet;
    }
    public Logger chatty() {
      return chatty;
    }
    
    public static Logger logger(Consumer<? super String> consumer) {
      return new Logger(consumer, consumer,
          normal -> new Logger(consumer, msg -> { /* empty */ }, identity(), it -> normal),
          identity());
    }
  }
  
  public static void main(String[] args) {
    Logger logger = Logger.logger(System.out::println);
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
