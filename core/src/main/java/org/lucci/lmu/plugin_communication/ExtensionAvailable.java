package org.lucci.lmu.plugin_communication;

/**
 * Created by quentin on 12/01/16.
 */
public enum ExtensionAvailable {

    JAR("jar"), LMU("lmu"), PACKAGE("package"), JAVA("java"), PLUGIN("plugin");

    private final String name;

    ExtensionAvailable(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
