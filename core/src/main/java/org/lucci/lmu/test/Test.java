package org.lucci.lmu.test;

import org.lucci.lmu.domain.Entities;
import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.domain.Model;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ModelException;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.GraphVizBasedViewFactory;
import org.lucci.lmu.output.WriterException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Test {
	public static void main(String... args) throws ParseError, IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("jfig.jar");
		File file = new File(url.getPath());

        Model model = null;
        try {
            model = new JarFileAnalyser().createModel(file);
        } catch (ModelException ex) {
            ex.printStackTrace();
        }
        Entity e = Entities.findEntityByName(model, "ConsoleMessage");

		// Try to write the output
		AbstractWriter writer = AbstractWriter.getTextFactory("jpeg");
		try {
            if (writer instanceof GraphVizBasedViewFactory){
                System.out.println("Start writing model");
                writer.writeModel(model);
                System.out.println("Output generated with format \'" + ((GraphVizBasedViewFactory) writer).getOutputType()+"\'");
            }

		} catch (WriterException ex){
			ex.printStackTrace();
		}

		System.out.println("Entities : " + e.getName());
		System.out.println("Attributes : " + e.getAttributes());

	}
}
