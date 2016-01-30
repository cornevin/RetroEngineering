package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
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

	private String path;

	public JarFileAnalyser() {
		super();
	}

	public Collection<RegularFile> getJarFiles()
	{
		return this.knownJarFiles;
	}

	@Override
	protected Model createModel() {
		try {
			URL url = new URL("file://" + path);
			File file = new File(url.getPath());
			RegularFile jarFile = RegularFile.createTempFile("lmu-", ".jar");
			jarFile.setContent(FileUtilities.getFileContent(file));

			ClassLoader classLoader = new URLClassLoader(new URL[] { jarFile.toURL() });

			ClassPath classContainers = new ClassPath();
			classContainers.add(new ClassContainer(jarFile, classLoader));

			for (RegularFile thisJarFile : this.knownJarFiles)
			{
				classContainers.add(new ClassContainer(thisJarFile, classLoader));
			}

			for (Class<?> thisClass : classContainers.listAllClasses()) {
				if (!thisClass.getName().matches(".+\\$[0-9]+")) {
					Entity entity = new Entity();
					entity.setName(computeEntityName(thisClass));
					entity.setNamespace(computeEntityNamespace(thisClass));
					entity_class.put(entity, thisClass);
					model.addEntity(entity);
				}
			}

			fillModel(model);
			jarFile.delete();
		} catch (IOException ex) {
			throw new IllegalStateException();
		}
		return model;
	}

	public AbstractModel createModelFromJar(String path) {
		this.path = path;
		return createModel();
	}
}
