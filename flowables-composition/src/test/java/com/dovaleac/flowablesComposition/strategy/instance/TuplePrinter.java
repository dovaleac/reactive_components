package com.dovaleac.flowablesComposition.strategy.instance;

import com.dovaleac.flowablesComposition.tuples.InnerJoinTuple;
import com.dovaleac.flowablesComposition.tuples.LeftJoinTuple;
import com.dovaleac.flowablesComposition.tuples.RightJoinTuple;
import com.dovaleac.flowablesComposition.tuples.TupleVisitor;

public class TuplePrinter implements TupleVisitor<SmallTuple, SmallTuple, String> {

  public static final String DELIMITER = ",";

  @Override
  public String visit(InnerJoinTuple<SmallTuple, SmallTuple> inner) {
    return inner.getLeft().getId() + DELIMITER + inner.getLeft().getValue() + DELIMITER + inner.getRight().getValue();
  }

  @Override
  public String visit(LeftJoinTuple<SmallTuple, SmallTuple> left) {
    return left.getLeft().getId() + DELIMITER + left.getLeft().getValue() + DELIMITER ;
  }

  @Override
  public String visit(RightJoinTuple<SmallTuple, SmallTuple> right) {
    return right.getRight().getId() + DELIMITER + DELIMITER + right.getRight().getValue();
  }
}
