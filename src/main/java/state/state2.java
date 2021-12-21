package state;

import java.util.concurrent.atomic.AtomicReference;

public interface state2 {
  class Process {
    private sealed interface State {
      boolean isTerminated();
      String result();
    }
    private enum EnumState implements State {
      CREATED, RUNNING;

      public boolean isTerminated() {
        return false;
      }
      public String result() {
        throw new IllegalStateException("not terminated !");
      }
    }
    private record TerminatedState(String result) implements State {
      @Override
      public boolean isTerminated() {
        return true;
      }
    }

    private final AtomicReference<State> state = new AtomicReference<>(EnumState.CREATED);

    public void start() {
      if (!state.compareAndSet(EnumState.CREATED, EnumState.RUNNING)) {
        throw new IllegalStateException("already stated");
      }
      new Thread(() -> {
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          state.set(new TerminatedState(null));
          return;
        }
        state.set(new TerminatedState("hello"));
      }).start();
    }

    public boolean isTerminated() {
      return state.get().isTerminated();
    }

    public String result() {
      return state.get().result();
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
