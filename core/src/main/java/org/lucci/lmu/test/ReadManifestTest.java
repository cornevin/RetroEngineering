package org.lucci.lmu.test;

import org.lucci.lmu.output.WriterException;
import org.lucci.lmu.plugin_communication.ConfigurationException;
import org.lucci.lmu.plugin_communication.LmuConfiguration;
import org.lucci.lmu.plugin_communication.OutputAvailable;

import java.io.IOException;

/**
 * Created by quentin on 09/02/16.
 */
public class ReadManifestTest {

    public static void main(String argc[]) throws WriterException, IOException {
    //    DeploymentUnitAnalyzer deploymentUnitAnalyzer = new DeploymentUnitAnalyzer();
    //    AbstractModel abstractModel = deploymentUnitAnalyzer.createModelFromJar("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/src/main/resources/lmu-eclipse-plugin-bkp_1.0.0.201601261055.jar");

   //     PluginAnalyser pluginAnalyser = new PluginAnalyser();
   //     AbstractModel abstractModel = pluginAnalyser.createModelFromPlugin(Directory.getCurrentDirectory().getPath()+"/../plugin/fr.unice.polytech.rimel.LMUEclipseUI");
     //   Path outputPath = Paths.get("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/test.pdf");


       // AbstractWriter writer = WriterFactory.getTextFactory("pdf");
       // FileUtilities.setFileContent(outputPath.toFile(), writer.writeModel(abstractModel));


        LmuConfiguration lmuConfiguration = new LmuConfiguration();
        lmuConfiguration.setJarUnitPath("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/src/main/resources/lmu-eclipse-plugin-bkp_1.0.0.201601261055.jar");
        lmuConfiguration.setOuputFileName("test");
        lmuConfiguration.setOutputExtension(OutputAvailable.PDF);
        try {
            lmuConfiguration.createModel();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        System.out.println("Work is done !!");
    }
}
