package org.apache.streampipes.operations;

public class XNORBoolOperation implements IBoolOperation<Boolean> {
    @Override
    public Boolean evaluate(Boolean operand, Boolean otherOperand) {
        return operand == otherOperand;
    }
}
