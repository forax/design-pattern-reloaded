package structuraltyping;

public interface ducktyping {
  class A {
    public void m() { 
      System.out.println("A::m");
    }
  }
  
  class B {
    public void m() { 
      System.out.println("B::m");
    }
  }
  
  static void print(Object o) throws Exception {
    o.getClass().getMethod("m").invoke(o);
  }
  
  public static void main(String[] args) throws Exception {
    print(new A());
    print(new B());
  }
}
