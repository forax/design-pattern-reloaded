package state;

import java.util.concurrent.atomic.AtomicReference;

public interface state1 {
  class Process {
    private enum EnumState {
      CREATED, RUNNING, TERMINATED;
    }

    private String result;
    private final AtomicReference<EnumState> state = new AtomicReference<>(EnumState.CREATED);


    public void start() {
      if (!state.compareAndSet(EnumState.CREATED, EnumState.RUNNING)) {
        throw new IllegalStateException("already stated");
      }
      new Thread(() -> {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          result = null;
          state.set(EnumState.TERMINATED);
          return;
        }
        result = "hello";
        state.set(EnumState.TERMINATED);
      }).start();
    }

    public boolean isTerminated() {
      var state = this.state.get();
      return state != EnumState.CREATED && state != EnumState.RUNNING;
    }

    public String result() {
      if (state.get() == EnumState.TERMINATED) {
        return result;
      }
      throw new IllegalStateException("not terminated !");
    }
  }

  static void main(String[] args) throws InterruptedException {
    var process = new Process();
    System.out.println("terminated? " + process.isTerminated());
    process.start();
    Thread.sleep(1_000);
    System.out.println("terminated? " + process.isTerminated());
    System.out.println("result " + process.result());
  }
}
