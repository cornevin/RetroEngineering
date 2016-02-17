package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.DeploymentUnit;
import org.lucci.lmu.domain.DeploymentUnitRelation;
import org.lucci.lmu.domain.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
            JarFile jarFile = new JarFile(this.jarPAth);
            Manifest m = jarFile.getManifest();
            Attributes mainAttributes = m.getMainAttributes();

            DeploymentUnit deploymentUnit = new DeploymentUnit();
            deploymentUnit.setName(this.jarPAth);
            Attributes attributes = m.getMainAttributes();


            List<Attributes.Name> targetKeys = Arrays.asList(new Attributes.Name("Bundle-ClassPath"));
            List<String> depencies = new ArrayList<>();

            for(Attributes.Name name : targetKeys) {
                System.out.println("Name : " + name);
                System.out.println("Result : " + mainAttributes.get(name));
                String[] splittedResult = ((String) mainAttributes.get(name)).split(",");
                List<String> stringList = Arrays.asList(splittedResult);
                depencies.addAll(stringList);
            }

            Entity jar = new DeploymentUnit();
            String jarName = getDeploymentUnitName(jarFile.getName());
            jar.setName(jarName);
            jar.setNamespace(jarName);
            model.addEntity(jar);

            for(String deploymentUnitName : depencies) {
                if(! deploymentUnitName.equals(".")) {
                    String correctName = this.getDeploymentUnitName(deploymentUnitName);
                    Entity entity = new DeploymentUnit();
                    entity.setName(correctName);
                    entity.setNamespace(correctName);

                    model.addEntity(entity);
                }
            }

            // Create a relation between the original jar and all the deployment unit
            for(Entity deploymentEntity : model.getEntities()) {
                model.addRelation(new DeploymentUnitRelation(jar, deploymentEntity));
            }




        } catch (IOException e) {
            e.printStackTrace();
        }

        return model;
    }


    public AbstractModel createModelFromJar(String jarPath) {
        this.jarPAth = jarPath;
        return createModel();
    }

    private String getDeploymentUnitName(String deploymentUnitPath) {
        String[] splittedPath = deploymentUnitPath.split("/");
        String fileName = splittedPath[splittedPath.length - 1];

        // Problem with DotWritter otherwise
        fileName = fileName.replace("-", "_");
        fileName = fileName.replace(".", "_");

        return fileName;
    }
}
