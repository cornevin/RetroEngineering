package org.lucci.lmu.input;

import java.util.HashMap;
import java.util.Map;

// USEFULL !


/*
 * Created on Oct 11, 2004
 */

/**
 * @author luc.hogie
 */
public abstract class ModelFactory
{

	static private Map<String, ModelCreator> factoryMap = new HashMap<String, ModelCreator>();

	static
	{
		factoryMap.put(null, LmuParser.getParser());
		factoryMap.put("lmu", LmuParser.getParser());
		factoryMap.put("jar", new JarFileAnalyser());
		factoryMap.put("nothing", new FolderAnalyser());
	}

	public static ModelCreator getModelFactory(String type)
	{
		return factoryMap.get(type);
	}
}
