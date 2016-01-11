package org.lucci.lmu.input;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ClassFinder {

    private static final char DOT = '.';

    private static final char SLASH = '/';

    private static final String CLASS_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    public static List<Class<?>> find(String scannedPackage) {
        //String scannedPath = scannedPackage.replace(DOT, SLASH);
        System.out.println(scannedPackage);
//        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPackage);

       // URL url1 = (new java.io.File("page/url1.html")).toURI().toURL();
        URL scannedUrl = null;
        try {
            scannedUrl = new File(scannedPackage).toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // System.out.println(scannedPackage);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPackage, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<Class<?>>();
        System.out.println(scannedDir.listFiles().length);
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage));
        }
        System.out.println(classes.size());
        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        String scannedPath = scannedPackage.replace(SLASH, DOT);
        scannedPath = scannedPath.substring(1);

        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPath + DOT + file.getName();
        System.out.println(resource);
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(CLASS_SUFFIX)) {
            int endIndex = resource.length() - CLASS_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                System.out.println(className);
                classes.add(Class.forName("home.quentin.Documents.RetroEngineering.src.yolo.AssociationRelation"));
            } catch (ClassNotFoundException ignore) {
                System.out.println("derpage");
            }
        }
        return classes;
    }

}