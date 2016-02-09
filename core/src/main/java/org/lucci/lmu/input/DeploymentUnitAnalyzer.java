package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.DeploymentUnit;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by quentin on 08/02/16.
 */
public class DeploymentUnitAnalyzer extends ModelCreator{

    private String jarPAth;

    @Override
    protected AbstractModel createModel() {
        try {
            /*String jarPath = "";
            File file = new File(jarPath);
            JarFile jarFile = new JarFile(file);
            Manifest manifest = jarFile.getManifest();
            Map<String, Attributes> entries = manifest.getEntries();

            LOGGER.debug(manifest.getMainAttributes().get(Attributes.Name.MANIFEST_VERSION));
            entries.forEach((name, attributes) -> LOGGER.debug(name + "\t" + attributes));
            */


            Manifest m = new JarFile(this.jarPAth).getManifest();
        //    Attributes attributes =  m.getAttributes(Attributes.Name.EXTENSION_NAME.toString());
            Attributes attributes =  m.getAttributes("Import-Package");
            Iterator it = attributes.entrySet().iterator();
            while(it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                DeploymentUnit deploymentUnit = new DeploymentUnit();
                deploymentUnit.setName((String) pair.getValue());

                // TODO : Recursive loop in the jar

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void createModelFromJar(String jarPath) {
        this.jarPAth = jarPath;
    }
}
