package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.DeploymentUnit;
import org.lucci.lmu.domain.DeploymentUnitRelation;
import org.lucci.lmu.domain.Entity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by remy on 18/02/16.
 */
public class PluginAnalyser extends ModelCreator {

    private String path = null;

/*    public Map<String, Set<String>> getDependenciesByPath(String path){
        Map<String, Set<String>> dependenciesMap = new HashMap<>();

        Set<String> dependencies = new HashSet<>();

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

        return dependenciesMap;
    } */

    @Override
    protected AbstractModel createModel() {

        Set<String> dependencies = new HashSet<>();

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

        String rootName = path.substring(path.lastIndexOf(File.separator) + 1);
        System.out.println("Plugin name : " + rootName);
        Entity root = new DeploymentUnit();
        root.setNamespace(rootName);
        root.setName(rootName);
        model.addEntity(root);

        for(String depencyName : dependencies) {
            Entity entity = new DeploymentUnit();
            entity.setName(depencyName);
            entity.setNamespace(depencyName);
            model.addEntity(entity);
        }

        for(Entity entity : model.getEntities()) {
            model.addRelation(new DeploymentUnitRelation(root, entity));
        }

        return model;
    }

    public AbstractModel createModelFromPlugin(String pluginPath) {
        this.path = pluginPath;
        return createModel();
    }
}