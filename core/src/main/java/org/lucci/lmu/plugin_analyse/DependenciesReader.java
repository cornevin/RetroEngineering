package org.lucci.lmu.plugin_analyse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.Manifest;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import toools.io.FileUtilities;

/**
 * Created by remy on 18/02/16.
 */
public class DependenciesReader {
    private List<String> projects;

    public Map<String, Set<String>> getDependenciesByPath(String path){
        return getDependenciesByPath(path, new HashMap<>());
    }

    private Map<String, Set<String>> getDependenciesByPath(String path, Map<String, Set<String>> dependenciesMap) {

        Set<String> dependencies = new HashSet<>();

        try {
            InputStream input = new FileInputStream(new File(path + "/META-INF/MANIFEST.MF"));
            Manifest manifest = new Manifest(input);
            System.out.println(manifest.getEntries());

            if (!manifest.getEntries().isEmpty()) {
                // ignore the bundle version and visibility
                String attrs = manifest.getMainAttributes().getValue("Require-Bundle")
                        .replaceAll(";bundle-version=\"[^\"]*\"", "").replaceAll(";visibility:=\\w+", "");

                Collections.addAll(dependencies, attrs.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File fXmlFile = new File(path + "/plugin.xml");
        if (fXmlFile.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            try {
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("extension");
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        String s = eElement.getAttribute("point");
                        dependencies.add(s);
                    }
                }
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
        dependenciesMap.put(path.substring(path.lastIndexOf(File.separator) + 1), dependencies);
        String workspace = path.substring(0, path.lastIndexOf(File.separator));
        if (projects == null) {
            projects = new ArrayList<>();
            getAllProjects(workspace);
        }
        projects.forEach(project -> {
            if (dependencies.contains(project)) {
                getDependenciesByPath(workspace + File.separator + project, dependenciesMap);
            }
        });
        return dependenciesMap;
    }

    public List<String> getAllProjects(String workspace) {
        File file = new File(workspace);
        Collection<File> filesUtils = FileUtilities.getChildFiles(file);
        filesUtils.forEach(f -> {
            if (f.isDirectory()) {
                projects.add(f.getName());
            }
        });
        return projects;
    }
}