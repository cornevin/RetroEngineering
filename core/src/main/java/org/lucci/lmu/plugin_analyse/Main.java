package org.lucci.lmu.plugin_analyse;

import toools.io.file.Directory;

import java.util.Map;
import java.util.Set;

/**
 * Created by remy on 18/02/16.
 */
public class Main {
    public static void main(String[] args) {
        DependenciesReader reader = new DependenciesReader();
        String path = Directory.getCurrentDirectory().getPath()+"/../plugin/fr.unice.polytech.rimel.LMUEclipseUI";
        System.out.println(path);
        Map<String, Set<String>> map = reader.getDependenciesByPath(path);
        System.out.println(map);
    }
}
