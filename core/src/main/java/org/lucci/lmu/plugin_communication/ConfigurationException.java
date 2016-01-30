package org.lucci.lmu.plugin_communication;

/**
 * Created by quentin on 30/01/16.
 */
public class ConfigurationException extends Exception {

    public ConfigurationException() {
        super();
    }

    @Override
    public String getMessage() {
        String s = "Configuration error, please check how you configured the application";
        return s;
    }
}
