package org.apache.streampipes.operations;

public interface IBoolOperation<T extends Boolean> {
    T evaluate(T operand, T otherOperand);
}
