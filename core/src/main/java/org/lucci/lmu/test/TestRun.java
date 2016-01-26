package org.lucci.lmu.test;

import org.lucci.lmu.plugin_communication.ExtensionAvailable;
import org.lucci.lmu.plugin_communication.LmuConfiguration;
import org.lucci.lmu.plugin_communication.OutputAvailable;

/**
 * Created by remy on 12/01/16.
 */
public class TestRun {
    public static void main(String[] args) {
        LmuConfiguration lmu = new LmuConfiguration();
        lmu.setInputExtension(ExtensionAvailable.PACKAGE);
        lmu.setOutputExtension(OutputAvailable.PDF);
        lmu.setInput("/home/remy/Documents/RIMEL/projet/core/src/main/java/org/lucci/lmu/");
        lmu.run("testRunDOT");
    }
}
