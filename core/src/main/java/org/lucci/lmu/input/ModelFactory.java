package org.lucci.lmu.input;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luc.hogie, Quentin Cornevin, Tom Guillermin & Remy Dupanloup
 */
public abstract class ModelFactory
{

	static private Map<String, ModelCreator> factoryMap = new HashMap<String, ModelCreator>();

	static
	{
		factoryMap.put(null, LmuParser.getParser());
		factoryMap.put("lmu", LmuParser.getParser());
		factoryMap.put("jar", new JarFileAnalyser());
		factoryMap.put("package", new FolderAnalyser());
		factoryMap.put("java", new FileAnalyser());
	}

	public static ModelCreator getModelFactory(String type)
	{
		return factoryMap.get(type);
	}
}
