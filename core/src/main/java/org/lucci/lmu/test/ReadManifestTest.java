package org.lucci.lmu.test;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.input.PluginAnalyser;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;
import org.lucci.lmu.output.WriterFactory;
import toools.io.FileUtilities;
import toools.io.file.Directory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by quentin on 09/02/16.
 */
public class ReadManifestTest {

    public static void main(String argc[]) throws WriterException, IOException {
    //    DeploymentUnitAnalyzer deploymentUnitAnalyzer = new DeploymentUnitAnalyzer();
    //    AbstractModel abstractModel = deploymentUnitAnalyzer.createModelFromJar("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/src/main/resources/lmu-eclipse-plugin-bkp_1.0.0.201601261055.jar");

        PluginAnalyser pluginAnalyser = new PluginAnalyser();
        AbstractModel abstractModel = pluginAnalyser.createModelFromPlugin(Directory.getCurrentDirectory().getPath()+"/../plugin/fr.unice.polytech.rimel.LMUEclipseUI");
        Path outputPath = Paths.get("/home/remy/Bureau/test.pdf");


        AbstractWriter writer = WriterFactory.getTextFactory("pdf");
        FileUtilities.setFileContent(outputPath.toFile(), writer.writeModel(abstractModel));
        System.out.println("Work is done !!");
    }
}
