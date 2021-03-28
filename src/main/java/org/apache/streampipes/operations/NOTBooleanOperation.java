package org.apache.streampipes.operations;

public class NOTBooleanOperation implements IBoolOperation<Boolean> {
    @Override
    public Boolean evaluate(Boolean operand, Boolean sameOperand) {
        return !operand;
    }
}
