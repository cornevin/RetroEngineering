package org.lucci.lmu.output;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by remy on 26/01/16.
 */
public class WriterFactory {
    static Map<String, AbstractWriter> factoryMap = new HashMap<>();

    static {
        // Description des classes (format txt)
        factoryMap.put(null, new LmuWriter());
        factoryMap.put("lmu", new LmuWriter());

        // Format de Word 2003
        factoryMap.put("dot", new DotWriter("dot"));
        factoryMap.put("xdot", new DotWriter("xdot"));

        // Génère des .class
        factoryMap.put("java", new JavaSourceWriter());
    }

    public static AbstractWriter getTextFactory(String type) {
        AbstractWriter f = factoryMap.get(type);

        if (f == null) {
            // return a GraphVizBased on type, if type is not allow return a jpeg image
            return new GraphVizBasedViewFactory(type);
        } else {
            return f;
        }
    }

}
