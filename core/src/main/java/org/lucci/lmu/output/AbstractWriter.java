package org.lucci.lmu.output;

import org.lucci.lmu.domain.Model;

import java.util.HashMap;
import java.util.Map;

/*
 * Created on Oct 3, 2004
 */

/**
 * @author luc.hogie
 */
public abstract class AbstractWriter {

	public abstract byte[] writeModel(Model diagram) throws WriterException;
    
	static Map<String, AbstractWriter> factoryMap = new HashMap<>();
	
	static {
        factoryMap.put(null, new LmuWriter());
		factoryMap.put("lmu", new LmuWriter());
		factoryMap.put("dot", new DotWriter());
		factoryMap.put("java", new JavaSourceWriter());
		factoryMap.put("ps", new GraphVizBasedViewFactory("ps"));
		factoryMap.put("png", new GraphVizBasedViewFactory("png"));
		factoryMap.put("fig", new GraphVizBasedViewFactory("fig"));
		factoryMap.put("svg", new GraphVizBasedViewFactory("svg"));
		factoryMap.put("pdf", new GraphVizBasedViewFactory("pdf"));
	}

	public static AbstractWriter getTextFactory(String type) {
	    AbstractWriter f = factoryMap.get(type);
	    
	    if (f == null) {
			// TODO if f is null so type is type is not define for GraphVizBasedViewFactory
            return new GraphVizBasedViewFactory(type);
	    } else {
	        return f;
	    }
	}
}
