package org.lucci.lmu.test;

import toools.io.file.JarFile;

import java.io.File;


/**
 * Created by quentin on 09/02/16.
 */
public class ReadManifestTest {
    private String jarName = "lmu-eclipse-plugin-bkp_1.0.0.201601261055.jar";
    private String jarPath;
    private JarFile jarFile;

    public void setJarPath(){
        this.jarPath = "/home/remy/Documents/RIMEL/projet/resources/"+jarName;
    }

    public JarFile createJarFile(){
        return jarFile = new JarFile(jarPath);
    }

    public static void main(String[] args) {
        ReadManifestTest readManifestTest = new ReadManifestTest();
        readManifestTest.setJarPath();
        JarFile jarFile = readManifestTest.createJarFile();

        

        // get Manifest here

        System.out.println(jarFile);
    }
}
