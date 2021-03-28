package org.apache.streampipes.operations.factory;

import org.apache.streampipes.enums.BooleanOperatorType;
import org.apache.streampipes.operations.ANDBoolOperation;
import org.apache.streampipes.operations.IBoolOperation;
import org.apache.streampipes.operations.NORBoolOperation;
import org.apache.streampipes.operations.NOTBooleanOperation;
import org.apache.streampipes.operations.ORBooleanOperation;
import org.apache.streampipes.operations.XNORBoolOperation;
import org.apache.streampipes.operations.XORBooleanOperation;

import static org.apache.streampipes.enums.BooleanOperatorType.AND;
import static org.apache.streampipes.enums.BooleanOperatorType.NOR;
import static org.apache.streampipes.enums.BooleanOperatorType.NOT;
import static org.apache.streampipes.enums.BooleanOperatorType.OR;
import static org.apache.streampipes.enums.BooleanOperatorType.X_NOR;
import static org.apache.streampipes.enums.BooleanOperatorType.XOR;

public class BoolOperationFactory {

    public static IBoolOperation<Boolean> getBoolOperation(BooleanOperatorType type) {
        if (type == AND) {
            return new ANDBoolOperation();
        } else if (type == OR) {
            return new ORBooleanOperation();
        } else if (type == XOR) {
            return new XORBooleanOperation();
        } else if (type == NOT) {
            return new NOTBooleanOperation();
        } else if (type == X_NOR) {
            return new XNORBoolOperation();
        } else if (type == NOR) {
            return new NORBoolOperation();
        } else {
            throw new UnsupportedOperationException("Operation " + type.operator() + " is not supported");
        }
    }
}
