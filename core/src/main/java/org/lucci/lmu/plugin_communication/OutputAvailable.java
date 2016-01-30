package org.lucci.lmu.plugin_communication;

/**
 * Created by remy on 12/01/16.
 */
public enum OutputAvailable {
    PDF("pdf"), JPEG("jpeg"), PNG("png"), SVG("svg"), CANON("canon"), DOT("dot"), XDOT("xdot"),
    CMAP("cmap"), DIA("dia"), FIG("fig"), GD("gd"), GD2("gd2"), GIF("gif"), HPGL("hpgl"),
    IMAP("imap"), CMAPX("cmapx"), ISMAP("ismap"), JPG("jpg"), MIF("mif"), MP("mp"), PCL("pcl"),
    PIC("pic"), PLAIN("plain"), PLAIN_EXT("plain-ext"), PS("ps"), PS2("ps2"), SVGZ("svgz"), VRML("vrml"),
    VTX("vtx"), WBMP("wbmp"), JAVA("java"), LMU("lmu");

    private final String name;

    OutputAvailable(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
