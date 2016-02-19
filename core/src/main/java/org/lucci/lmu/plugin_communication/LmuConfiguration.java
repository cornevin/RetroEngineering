package org.lucci.lmu.plugin_communication;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.input.ClazzAnalyser;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.PluginAnalyser;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;
import org.lucci.lmu.output.WriterFactory;
import toools.io.FileUtilities;
import toools.io.file.Directory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by quentin on 12/01/16.
 */
public class LmuConfiguration {

    private String classesPath;
    private List<Class<?>> classes;

    private String pluginPath;

    private OutputAvailable outputExtension;
    private String outputFileName;

    private AbstractModel model;

    public void setOutputExtension(OutputAvailable output) {
        this.outputExtension = output;
    }

    public void setInputPath(String path) {
        this.classesPath = path;
    }

    public void setInputPluginPath(String path){
        this.pluginPath = path;
    }

    public void setOuputFileName(String outoutFileName) {
        this.outputFileName = outoutFileName;
    }

    public void setInputClazzes(List<Class<?>> clazzes) {
        this.classes = clazzes;
    }

    public void createModel() throws ConfigurationException {
        if (classesPath == null && classes != null && pluginPath == null) {
            createDiagramFromClasses();
        } else if (classes == null && classesPath != null && pluginPath == null) {
            createDiagramFromJar();
        } else if (pluginPath != null && classesPath == null && classes == null) {
            createDiagramFromPlugin();
        } else {
            throw new ConfigurationException();
        }
    }

    private void createDiagramFromJar() {
        model = new JarFileAnalyser().createModelFromJar(this.classesPath);
        drawModel(model);
    }

    private void createDiagramFromClasses() {
        model = new ClazzAnalyser().createModelFromClazzes(this.classes);
        drawModel(model);
    }

    private void createDiagramFromPlugin(){
        model = new PluginAnalyser().createModelFromPlugin(this.pluginPath);
        drawModel(model);
    }


    private void drawModel(AbstractModel model) {
        AbstractWriter writer = WriterFactory.getTextFactory(outputExtension.toString());
        System.out.println("Start writing model");
        try {
			Path outputPath = Paths.get(this.outputFileName + "." + writer.getOutputType());
			outputPath = outputPath.toAbsolutePath();
            FileUtilities.setFileContent(outputPath.toFile(), writer.writeModel(model));
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        System.out.println("Output generated with format \'" + writer.getOutputType() + "\'");
    }
}
