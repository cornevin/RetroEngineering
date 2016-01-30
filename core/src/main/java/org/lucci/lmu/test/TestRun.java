package org.lucci.lmu.test;

import org.lucci.lmu.plugin_communication.ConfigurationException;
import org.lucci.lmu.plugin_communication.ExtensionAvailable;
import org.lucci.lmu.plugin_communication.LmuConfiguration;
import org.lucci.lmu.plugin_communication.OutputAvailable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by remy on 12/01/16.
 */
public class TestRun {
    public static void main(String[] args) throws ConfigurationException {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        List<Class<?>> classList = new ArrayList<>();
        try {
            classList.add(currentClassLoader.loadClass("org.lucci.lmu.domain.Operation"));
            classList.add(currentClassLoader.loadClass("org.lucci.lmu.domain.Relation"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        LmuConfiguration lmu = new LmuConfiguration();
        lmu.setInputExtension(ExtensionAvailable.PACKAGE);
        lmu.setOutputExtension(OutputAvailable.JPEG);
        lmu.setOuputFileName("test");
       // lmu.setInputPath("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/src/main/resources/jfig.jar");
        lmu.setInputClazzes(classList);
        lmu.createModel();
    }
}
