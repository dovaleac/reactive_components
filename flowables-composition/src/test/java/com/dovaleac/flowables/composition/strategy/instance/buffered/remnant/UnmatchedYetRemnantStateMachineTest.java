package com.dovaleac.flowables.composition.strategy.instance.buffered.remnant;

import org.junit.jupiter.api.Test;

class UnmatchedYetRemnantStateMachineTest {

  UnmatchedYetRemnantImpl impl = new UnmatchedYetRemnantImpl();

  @Test
  void test() {
    System.out.println(
        new UnmatchedYetRemnantStateMachine(impl).getConfig()
            .generatePlantUmlDiagramTxt(UnmatchedYetRemnantState.IDLE, true)
    );
  }
}