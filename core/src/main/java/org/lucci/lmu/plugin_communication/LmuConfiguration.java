package org.lucci.lmu.plugin_communication;

import org.lucci.lmu.domain.Model;
import org.lucci.lmu.input.ModelException;
import org.lucci.lmu.input.ModelFactory;
import org.lucci.lmu.input.ParseError;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by quentin on 12/01/16.
 */
public class LmuConfiguration {

    private ExtensionAvailable inputExtension;
    private String path;

    public void setInputExtension(ExtensionAvailable extension) {
        this.inputExtension = extension;
    }

    public void setOutputExtension() {

    }

    public void setInput(String  path) {
        this.path = path;
    }

    public void run() {
        URL url = Thread.currentThread().getContextClassLoader().getResource(path);
        File file = new File(url.getPath());

        try {
            Model model = ModelFactory.getModelFactory(inputExtension.toString()).createModel(file);

            // TODO !!
        } catch (ModelException e) {
            e.printStackTrace();
        } catch (ParseError parseError) {
            parseError.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
