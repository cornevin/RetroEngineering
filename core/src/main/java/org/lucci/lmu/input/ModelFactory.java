package org.lucci.lmu.input;

import org.lucci.lmu.domain.Model;

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

	static private Map<String, ModelFactory> factoryMap = new HashMap<String, ModelFactory>();

	static
	{
		factoryMap.put(null, LmuParser.getParser());
		factoryMap.put("lmu", LmuParser.getParser());
		factoryMap.put("jar", new JarFileAnalyser());
		factoryMap.put("nothing", new FolderAnalyser());
	}

	public static ModelFactory getModelFactory(String type)
	{
		return factoryMap.get(type);
	}

	public abstract Model createModel(byte[] data) throws ParseError, ModelException;
}
