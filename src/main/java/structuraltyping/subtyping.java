package structuraltyping;

public interface subtyping {
  interface I {
    void m();
  }
  
  class A implements I {
    public void m() { 
      System.out.println("A::m");
    }
  }
  
  class B implements I {
    public void m() { 
      System.out.println("B::m");
    }
  }
  
  static void print(I i) {
    i.m();
  }
  
  public static void main(String[] args) {
    print(new A());
    print(new B());
  }
}
