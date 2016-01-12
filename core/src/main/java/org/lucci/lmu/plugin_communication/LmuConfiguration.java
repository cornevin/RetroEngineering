package org.lucci.lmu.plugin_communication;

import org.lucci.lmu.domain.Model;
import org.lucci.lmu.input.ModelException;
import org.lucci.lmu.input.ModelFactory;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.GraphVizBasedViewFactory;
import org.lucci.lmu.output.WriterException;
import toools.io.FileUtilities;
import toools.io.file.Directory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by quentin on 12/01/16.
 */
public class LmuConfiguration {

    private ExtensionAvailable inputExtension;
    private OutputAvailable outputExtension;
    private String path;

    public void setInputExtension(ExtensionAvailable extension) {
        this.inputExtension = extension;
    }

    public void setOutputExtension(OutputAvailable output) {
        this.outputExtension = output;
    }

    public void setInput(String path) {
        this.path = path;
    }

    /**
     * run with the name of the generated output file
     * @param outputFileName
     */
    public void run(String outputFileName) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        File file = new File(url.getPath());

        try {
            Model model = ModelFactory.getModelFactory(inputExtension.toString()).createModel(file);
            AbstractWriter writer = AbstractWriter.getTextFactory(outputExtension.toString());
            if (writer instanceof GraphVizBasedViewFactory) {
                System.out.println("Start writing model");
                FileUtilities.setFileContent(new File(Directory.getCurrentDirectory() + "/" + outputFileName + "." + ((GraphVizBasedViewFactory) writer).getOutputType()), writer.writeModel(model));
                System.out.println("Output generated with format \'" + ((GraphVizBasedViewFactory) writer).getOutputType() + "\'");
            }
        } catch (ModelException | ParseError | IOException | WriterException e) {
            e.printStackTrace();
        }
    }
}
