package com.dovaleac.flowablesComposition.strategy.instance.buffered.remnant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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