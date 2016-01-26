package org.lucci.lmu.input;

import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.domain.Model;
import toools.ClassContainer;
import toools.ClassPath;
import toools.io.FileUtilities;
import toools.io.file.RegularFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie
 */
public class JarFileAnalyser extends ModelCreator {

	public JarFileAnalyser() {
		super();
	}

	public Collection<RegularFile> getJarFiles()
	{
		return this.knownJarFiles;
	}

	@Override
	public Model createModel(String path) throws ParseError {
		try
		{
/*			Code pour récuprer la liste des dépendance dans un manifest, faut encore trouvert comment mettre ca propre
			Manifest m = new JarFile("test").getManifest();
			Attributes a =  m.getMainAttributes();
			a.get(Attributes.Name.CLASS_PATH); */

			URL url = new URL("file://" + path);
			File file = new File(url.getPath());
			System.out.println(path);
			// create a jar file on the disk from the binary data
			RegularFile jarFile = RegularFile.createTempFile("lmu-", ".jar");
			jarFile.setContent(FileUtilities.getFileContent(file));

			ClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURL() });

			ClassPath classContainers = new ClassPath();
			classContainers.add(new ClassContainer(jarFile, classLoader));

			for (RegularFile thisJarFile : this.knownJarFiles)
			{
				classContainers.add(new ClassContainer(thisJarFile, classLoader));
			}

			// take all the classes in the jar files and convert them to LMU
			// Entities
			for (Class<?> thisClass : classContainers.listAllClasses())
			{
				// if this is not an anonymous inner class (a.b$1)
				// we take it into account
				if (!thisClass.getName().matches(".+\\$[0-9]+"))
				{
					Entity entity = new Entity();
					entity.setName(computeEntityName(thisClass));
					entity.setNamespace(computeEntityNamespace(thisClass));
					entity_class.put(entity, thisClass);
					model.addEntity(entity);
				}
			}

			// at this only the name of entities is known
			// neither members nor relation are known
			// let's find them
			fillModel(model);
			jarFile.delete();
		}
		catch (IOException ex)
		{
			throw new IllegalStateException();
		}

		return model;
	}
}
