package org.lucci.lmu.plugin_communication;

/**
 * Created by remy on 12/01/16.
 */
public enum OutputAvailable {
    PDF("pdf"), JPEG("jpeg"), PNG("png"), SVG("svg");

    private final String name;

    OutputAvailable(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
