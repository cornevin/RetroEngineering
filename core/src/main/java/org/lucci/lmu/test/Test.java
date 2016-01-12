package org.lucci.lmu.test;

import org.lucci.lmu.domain.Entities;
import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.domain.Model;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.WriterException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Test {
	public static void main(String... args) throws ParseError, IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("jfig.jar");
		File file = new File(url.getPath());

		Model model = new JarFileAnalyser().createModel(file);
		Entity e = Entities.findEntityByName(model, "ConsoleMessage");

		// Try to write the output
		AbstractWriter writer = AbstractWriter.getTextFactory("png");
		try {
			writer.writeModel(model);
		} catch (WriterException ex){
			ex.printStackTrace();
		}

		System.out.println("Entites : " + e.getName());
		System.out.println("Attributes : " + e.getAttributes());
	}
}
