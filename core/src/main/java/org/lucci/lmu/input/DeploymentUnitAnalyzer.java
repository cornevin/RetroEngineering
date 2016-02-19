package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.DeploymentUnit;
import org.lucci.lmu.domain.DeploymentUnitRelation;
import org.lucci.lmu.domain.Entity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Created by quentin on 08/02/16.
 */
public class DeploymentUnitAnalyzer extends ModelCreator{

    private String jarPAth;
    private List<String> deploymentUnitAlreadyRead = new ArrayList<>();

    @Override
    protected AbstractModel createModel() {
        try {
            JarFile jarFile = new JarFile(this.jarPAth);
            Manifest m = jarFile.getManifest();
            Attributes mainAttributes = m.getMainAttributes();

            DeploymentUnit deploymentUnit = new DeploymentUnit();
            deploymentUnit.setName(this.jarPAth);

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
                if(this.checkName(deploymentUnitName)) {
                    String correctName = this.getDeploymentUnitName(deploymentUnitName);
                    Entity entity = new DeploymentUnit();
                    entity.setName(correctName);
                    entity.setNamespace(correctName);

                    model.addEntity(entity);
                    if(! this.deploymentUnitAlreadyRead.contains(correctName)) {
                        System.out.println("Nested jar : " +  correctName);
                    }
                }
            }

            // Create a relation between the original jar and all the deployment unit
            for(Entity deploymentEntity : model.getEntities()) {
                model.addRelation(new DeploymentUnitRelation(jar, deploymentEntity));
            }



            Enumeration<JarEntry> enums = jarFile.entries();
            while (enums.hasMoreElements()) {
                String tempDirectory = "temp_lmu_directory";
                JarEntry jarEntry = enums.nextElement();
                String fileName = jarEntry.getName();
                if(fileName.endsWith(".jar") && depencies.contains(fileName)) {
                    InputStream in = jarFile.getInputStream(jarEntry);
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                    FileOutputStream out = new FileOutputStream(tempDirectory + "/" + fileName);
                    while (in.available() > 0) {
                        out.write(in.read());
                    }
                    out.close();
                    in.close();

                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return model;
    }

    private void readDeploymentUnit(JarFile jarFile) throws IOException {

    }

    /**
     * This method check the name of the depencies and return true if the name is match a jar file
     *
     * @param name of the file or folder to analyze
     * @return true if the file is jar, false otherwise
     */
    private boolean checkName(String name) {
        if(!name.equals(".")) {
            String[] splittedName = name.split("\\.");
            if (splittedName.length > 0 && splittedName[splittedName.length - 1].equals("jar")) {
                return true;
            }
        }
        return false;
    }


    private void openJar(String[] jarsName) {
        for(int i = 0; i < jarsName.length; i++) {
            System.out.println(jarsName[i]);
        }
        ZipExploder ze = new ZipExploder(false);
        try {
            ze.process(jarsName, "/home/quentin/Documents/SI5/Retro/RetroEngineering/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AbstractModel createModelFromJar(String jarPath) {
        this.jarPAth = jarPath;
        return createModel();
    }

    private String getDeploymentUnitName(String deploymentUnitPath) {
        String[] splittedPath = deploymentUnitPath.split("/");
       // System.out.println("Je test un truc : " + splittedPath[splittedPath.length - 1]);
        String fileName = splittedPath[splittedPath.length - 1];

        // Problem with DotWritter otherwise
        fileName = fileName.replace("-", "_");

        return fileName;
    }
}
