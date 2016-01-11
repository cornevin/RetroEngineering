package org.lucci.lmu.test;

import org.lucci.lmu.domain.Entities;
import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.domain.Model;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ParseError;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Test
{
	public static void main(String... args) throws ParseError, IOException
	{
		URL url = Thread.currentThread().getContextClassLoader().getResource("jfig.jar");
		File file = new File(url.getPath());

		Model model = new JarFileAnalyser().createModel(file);
		Entity e = Entities.findEntityByName(model, "ConsoleMessage");

		System.out.println(e.getName());
		System.out.println(e.getAttributes());
	}
}
