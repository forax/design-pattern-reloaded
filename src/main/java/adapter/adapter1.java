package adapter;

public interface adapter1 {
  interface Logger {
    void log(String message);
  }
  
  enum Level { WARNING, ERROR }
  interface Logger2 {
    void log(Level level, String message);
  }
  
  static void main(String[] args) {
    Logger2 logger2 = (level, msg) -> System.out.println(level + " " + msg);
    logger2.log(Level.ERROR, "abort abort !");
    
    Logger logger = null; // how to use logger2 as a Logger here ?
  }
}
