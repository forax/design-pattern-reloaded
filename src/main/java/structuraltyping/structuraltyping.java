package structuraltyping;

public interface structuraltyping {
  interface I {
    void m();
  }
  
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
  
  static void print(I i) {
    i.m();
  }
  
  public static void main(String[] args) {
    print(new A()::m);
    print(new B()::m);
  }
}
