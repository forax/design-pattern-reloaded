package adapter;

import static adapter.adapter1.Level.ERROR;

public interface adapter1 {
  public interface Logger {
    public void log(String message);
  }
  
  public enum Level { WARNING, ERROR }
  public interface Logger2 {
    public void log(Level level, String message);
  }
  
  public static void main(String[] args) {
    Logger2 logger2 = (level, msg) -> System.out.println(level + " " + msg);
    logger2.log(ERROR, "abort abort !");
    
    Logger logger = null; // how to use logger2 here ?
  }
}
