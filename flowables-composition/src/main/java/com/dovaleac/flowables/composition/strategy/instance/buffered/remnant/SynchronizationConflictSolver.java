package com.dovaleac.flowables.composition.strategy.instance.buffered.remnant;

public class SynchronizationConflictSolver {

  private UnmatchedYetRemnantImpl leader;
  private static volatile SynchronizationConflictSolver mInstance;

  private SynchronizationConflictSolver() {
  }

  public static SynchronizationConflictSolver getInstance() {
    if (mInstance == null) {
      synchronized (SynchronizationConflictSolver.class) {
        if (mInstance == null) {
          mInstance = new SynchronizationConflictSolver();
        }
      }
    }
    return mInstance;
  }

  public synchronized void solveConflict(UnmatchedYetRemnantImpl stateMachine) {
    if (leader == null) {
//      System.out.println("SCS: then");
      leader = stateMachine;
    } else {
//      System.out.println("SCS: else");
      stateMachine.setSynchronizee();
      leader = null;
    }
  }
}
