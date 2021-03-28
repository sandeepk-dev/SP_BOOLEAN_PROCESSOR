package org.apache.streampipes;

import org.apache.streampipes.configs.Config;
import org.apache.streampipes.container.init.DeclarersSingleton;
import org.apache.streampipes.container.model.PeConfig;
import org.apache.streampipes.container.standalone.init.StandaloneModelSubmitter;
import org.apache.streampipes.dataformat.cbor.CborDataFormatFactory;
import org.apache.streampipes.dataformat.fst.FstDataFormatFactory;
import org.apache.streampipes.dataformat.json.JsonDataFormatFactory;
import org.apache.streampipes.dataformat.smile.SmileDataFormatFactory;
import org.apache.streampipes.messaging.jms.SpJmsProtocolFactory;
import org.apache.streampipes.messaging.kafka.SpKafkaProtocolFactory;
import org.apache.streampipes.processor.BooleanOperatorProcessor;
import org.springframework.boot.SpringApplication;

import java.util.Collections;

public class Init extends StandaloneModelSubmitter {
    public static void main(String[] args) {


        DeclarersSingleton.getInstance()
                .add(new BooleanOperatorProcessor());


        DeclarersSingleton.getInstance().registerDataFormats(
                new JsonDataFormatFactory(),
                new CborDataFormatFactory(),
                new SmileDataFormatFactory(),
                new FstDataFormatFactory());

        DeclarersSingleton.getInstance().registerProtocols(
                new SpKafkaProtocolFactory(),
                new SpJmsProtocolFactory());

        new Init().init(Config.INSTANCE);
    }

    public void init(PeConfig peConfig) {

        DeclarersSingleton.getInstance()
                .setHostName(peConfig.getHost());
        DeclarersSingleton.getInstance()
                .setPort(peConfig.getPort());

        SpringApplication app = new SpringApplication(Init.class);
        app.setDefaultProperties(Collections.<String, Object>singletonMap("server.port", peConfig.getPort()));
        app.run();
    }

}
