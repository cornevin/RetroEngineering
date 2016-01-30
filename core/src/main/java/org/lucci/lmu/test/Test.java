package org.lucci.lmu.test;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.Entities;
import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.input.JarFileAnalyser;
import org.lucci.lmu.input.ModelException;
import org.lucci.lmu.input.ParseError;
import org.lucci.lmu.output.*;
import org.lucci.lmu.input.*;
import org.lucci.lmu.output.AbstractWriter;
import org.lucci.lmu.output.GraphVizBasedViewFactory;
import org.lucci.lmu.output.JavaSourceWriter;
import org.lucci.lmu.output.WriterException;
import toools.io.FileUtilities;
import toools.io.file.Directory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Test {
	public static void main(String... args) throws ParseError, IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("jfig.jar");

    //    FileAnalyser fileAnalyser = new FileAnalyser();
     //   try {
            ///AbstractModel myModel = fileAnalyser.createModel("C:\\Users\\Quentin\\Documents\\SI5\\RetroEngineering\\RetroEngineering\\core\\src\\main\\java\\org\\lucci\\lmu\\input\\ClassFinder.java");
           // System.out.println(myModel.getEntities().size());
    //    } catch (ModelException e) {
      //      e.printStackTrace();
       // }

/*        try {
        //    AbstractModel abstractModel = ModelFactory.getModelFactory("package").createModel("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/src/main/java/org/lucci/lmu/input/");




        } catch (ModelException e) {
            e.printStackTrace();
        } */

       // AbstractModel model = new JarFileAnalyser().createModel(url.getPath());
        AbstractModel model = null;
        model = new JarFileAnalyser().createModelFromJar("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/src/main/resources/jfig.jar");
 /*       try {
//            model = ModelFactory.getModelFactory("java").createModel("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/src/main/java/org/lucci/lmu/input/ParseError.java");
         //   model = ModelFactory.getModelFactory("java").createModel("/home/quentin/Documents/SI5/Retro/RetroEngineering/core/target/classes/org/lucci/lmu/input/FileAnalyser.class");
        } catch (ModelException e) {
            e.printStackTrace();
        } */

        Entity e = Entities.findEntityByName(model, "ConsoleMessage");

		// Try to write the output
		AbstractWriter writer = WriterFactory.getTextFactory("pdf");
		try {
            if (writer instanceof GraphVizBasedViewFactory){
                System.out.println("Start writing model");
                //System.out.println(Directory.getCurrentDirectory());
                FileUtilities.setFileContent(new File(Directory.getCurrentDirectory()+"/output."+((GraphVizBasedViewFactory) writer).getOutputType()), writer.writeModel(model));
                System.out.println("Output generated with format \'" + ((GraphVizBasedViewFactory) writer).getOutputType()+"\'");
            } else if (writer instanceof JavaSourceWriter){
                System.out.println("Start writing model");
                System.out.println(writer.writeModel(model));
                System.out.println("Output generated with format \'java\'");
            }
		} catch (WriterException ex){
			ex.printStackTrace();
		}

		System.out.println("Entities : " + e.getName());
		System.out.println("Attributes : " + e.getAttributes());

	}
}
