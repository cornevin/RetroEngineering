package org.lucci.lmu.plugin_communication;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.input.ClazzAnalyser;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.GraphVizBasedViewFactory;
import org.lucci.lmu.output.WriterException;
import org.lucci.lmu.output.WriterFactory;
import toools.io.FileUtilities;
import toools.io.file.Directory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by quentin on 12/01/16.
 */
public class LmuConfiguration {

    private ExtensionAvailable inputExtension;
    private OutputAvailable outputExtension;
    private String path;
    private List<Class<?>> classes;
    private String outputFileName;

    public void setInputExtension(ExtensionAvailable extension) {
        this.inputExtension = extension;
    }

    public void setOutputExtension(OutputAvailable output) {
        this.outputExtension = output;
    }

    public void setInputPath(String path) {
        this.path = path;
    }

    public void setOuputFileName(String outoutFileName) {
        this.outputFileName = outoutFileName;
    }

    public void setInputClazzes(List<Class<?>> clazzes) {
        this.classes = clazzes;
    }

    public void createModel() throws ConfigurationException {
        if(path == null && !(classes == null)){
                createDiagramFromClazzes();
        } else if(classes == null && !(path == null)) {
            createDiagramFromJar();
        } else {
            throw new ConfigurationException();
        }
    }


    private void createDiagramFromJar() {
        AbstractModel model = new JarFileAnalyser().createModelFromJar(this.path);
        drawModel(model);
    }

    private void createDiagramFromClazzes() {
        AbstractModel model = new ClazzAnalyser().createModelFromClazzes(this.classes);
        drawModel(model);
    }


    private void drawModel(AbstractModel model) {
        AbstractWriter writer = WriterFactory.getTextFactory(outputExtension.toString());
        if (writer instanceof GraphVizBasedViewFactory) {
            System.out.println("Start writing model");
            try {
                FileUtilities.setFileContent(new File(Directory.getCurrentDirectory() + "/src/main/java/org/lucci/lmu/plugin_communication/" + this.outputFileName + "." + ((GraphVizBasedViewFactory) writer).getOutputType()), writer.writeModel(model));
            } catch (IOException  | WriterException e) {
                e.printStackTrace();
            }
            System.out.println("Output generated with format \'" + ((GraphVizBasedViewFactory) writer).getOutputType() + "\'");
        }
    }
}
