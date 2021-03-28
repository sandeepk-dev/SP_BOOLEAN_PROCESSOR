package org.apache.streampipes.processor;

import org.apache.streampipes.commons.exceptions.SpRuntimeException;
import org.apache.streampipes.enums.BooleanOperatorType;
import org.apache.streampipes.configs.BooleanOperationInputConfigs;
import org.apache.streampipes.model.DataProcessorType;
import org.apache.streampipes.model.graph.DataProcessorDescription;
import org.apache.streampipes.model.runtime.Event;
import org.apache.streampipes.operations.factory.BoolOperationFactory;
import org.apache.streampipes.operations.IBoolOperation;
import org.apache.streampipes.sdk.builder.PrimitivePropertyBuilder;
import org.apache.streampipes.sdk.builder.ProcessingElementBuilder;
import org.apache.streampipes.sdk.builder.StreamRequirementsBuilder;
import org.apache.streampipes.sdk.helpers.EpRequirements;
import org.apache.streampipes.sdk.helpers.Labels;
import org.apache.streampipes.sdk.helpers.Locales;
import org.apache.streampipes.sdk.helpers.OutputStrategies;
import org.apache.streampipes.sdk.utils.Assets;
import org.apache.streampipes.sdk.utils.Datatypes;
import org.apache.streampipes.wrapper.context.EventProcessorRuntimeContext;
import org.apache.streampipes.wrapper.routing.SpOutputCollector;
import org.apache.streampipes.wrapper.standalone.ProcessorParams;
import org.apache.streampipes.wrapper.standalone.StreamPipesDataProcessor;

import java.util.List;

import static org.apache.streampipes.enums.BooleanOperatorType.NOT;

public class BooleanOperatorProcessor extends StreamPipesDataProcessor {

    private static final String BOOLEAN_PROCESSOR_INPUT_KEY = "boolean-processor-configs";
    private static final String BOOLEAN_PROCESSOR_OUT_KEY = "boolean-operations-result";
    private BooleanOperationInputConfigs configs;

    @Override
    public DataProcessorDescription declareModel() {
        return ProcessingElementBuilder.create("org.apache.streampipes.pe.processor.greeter")
                /**
                 * Assets (docs, icon) and locales can be found under resources and the <id> for this data processor
                 * (see above)
                 */
                .withAssets(Assets.DOCUMENTATION, Assets.ICON)
                .withLocales(Locales.EN)
                .category(DataProcessorType.ENRICH)
                /**
                 * input stream requirements
                 *
                 * defining input event stream requirements here, i.e. in order for this processor to work, it
                 * relies on certain properties such as a numeric value or a certain domain property (latitude)
                 * to be part of any of the event fields.
                 *
                 * here: we don't expect the input event stream to contain anything, thus we indicate it using
                 * .requiredProperty(EpRequirements.anyProperty())...
                 */
                .requiredStream(
                        StreamRequirementsBuilder
                                .create()
                                .requiredProperty(EpRequirements.anyProperty())
                                .build())
                /**
                 * user configuration (static property)
                 *
                 * this processor uses a textual user-defined input to be appended to the input event stream
                 */
                .requiredTextParameter(Labels.withId(BOOLEAN_PROCESSOR_INPUT_KEY))
                /**
                 * output strategy
                 *
                 * the SDK offers various output strategies (keep, fixed, custom, etc). Here, we simply append the given
                 * user-defined greeting as a new event field called "greeting" to the input event stream. As part of
                 * the model description, we describe how the output event schema looks like.
                 */
                .outputStrategy(OutputStrategies.append(
                        PrimitivePropertyBuilder.create(
                                Datatypes.String, BOOLEAN_PROCESSOR_OUT_KEY)
                                .build()))
                .build();
    }

    @Override
    public void onInvocation(ProcessorParams processorParams, SpOutputCollector spOutputCollector, EventProcessorRuntimeContext eventProcessorRuntimeContext) throws SpRuntimeException {
        BooleanOperationInputConfigs configs = processorParams.extractor().singleValueParameter(BOOLEAN_PROCESSOR_INPUT_KEY, BooleanOperationInputConfigs.class);
        preChecks(configs);
        this.configs = configs;
    }

    @Override
    public void onEvent(Event event, SpOutputCollector spOutputCollector) throws SpRuntimeException {
        List<String> properties = configs.getProperties();
        BooleanOperatorType operatorType = configs.getOperator();
        Boolean firstProperty = event.getFieldBySelector(properties.get(0)).getAsPrimitive().getAsBoolean();
        IBoolOperation<Boolean> boolOperation = BoolOperationFactory.getBoolOperation(operatorType);
        Boolean result;
        if (properties.size() == 1) {
            // support for NOT operator
            result = boolOperation.evaluate(firstProperty, firstProperty);

        } else {
            Boolean secondProperty = event.getFieldBySelector(properties.get(1)).getAsPrimitive().getAsBoolean();
            result = boolOperation.evaluate(firstProperty, secondProperty);

            //loop through rest of the properties to get final result
            for (int i = 2; i < properties.size(); i++) {
                result = boolOperation.evaluate(result, event.getFieldBySelector(properties.get(i)).getAsPrimitive().getAsBoolean());
            }

        }
        event.addField(BOOLEAN_PROCESSOR_OUT_KEY, result);
        spOutputCollector.collect(event);
    }

    @Override
    public void onDetach() throws SpRuntimeException {
        configs = null;
    }

    private void preChecks(BooleanOperationInputConfigs configs) {
        BooleanOperatorType operatorType = configs.getOperator();
        List<String> properties =  configs.getProperties();
        if (operatorType == NOT) {
            assert properties.size() == 1 : "NOT operator can operate only on single operand";
        } else {
            assert properties.size() >= 2 : "Number of operands are less that 2";
        }
    }
}
