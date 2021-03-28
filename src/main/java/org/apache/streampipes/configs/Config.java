package org.apache.streampipes.configs;

import org.apache.streampipes.config.SpConfig;
import org.apache.streampipes.container.model.PeConfig;

import static org.apache.streampipes.configs.ConfigKeys.HOST;
import static org.apache.streampipes.configs.ConfigKeys.PORT;
import static org.apache.streampipes.configs.ConfigKeys.SERVICE_NAME;

public enum Config implements PeConfig {
    INSTANCE;

    private SpConfig config;
    private final static String SERVICE_ID= "pe/org.apache.streampipes.processor.jvm";

    Config() {
        config = SpConfig.getSpConfig(SERVICE_ID);
        config.register(HOST, "127.0.0.1", "Data processor host");
        config.register(PORT, 8090, "Data processor port");
        config.register(SERVICE_NAME, "boolean-operation-processor", "Data processor service name");
    }

    @Override
    public String getId() {
        return config.getString(SERVICE_ID);
    }

    @Override
    public String getHost() {
        return config.getString(HOST);
    }

    @Override
    public int getPort() {
        return config.getInteger(PORT);
    }

    @Override
    public String getName() {
        return config.getString(SERVICE_NAME);
    }
}
