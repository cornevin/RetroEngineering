package org.lucci.lmu.input;

import org.lucci.lmu.domain.Entities;
import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.domain.Model;
import toools.io.file.RegularFile;

import java.util.*;

public class FolderAnalyser extends ModelCreator {
    //private Collection<RegularFile> knownJarFiles = new HashSet<RegularFile>();
    //private Map<Class<?>, Entity> primitiveMap = new HashMap<Class<?>, Entity>();
    //private Map<Entity, Class<?>> entity_class = new HashMap<Entity, Class<?>>();

    public FolderAnalyser() {
        this.knownJarFiles = new HashSet<RegularFile>();
        this.primitiveMap = new HashMap<Class<?>, Entity>();
        this.entity_class = new HashMap<Entity, Class<?>>();
    }

    public Collection<RegularFile> getJarFiles() {
        return this.knownJarFiles;
    }

    @Override
    public Model createModel(byte[] data) throws ParseError {
        Model model = new Model();
        primitiveMap.put(void.class, Entities.findEntityByName(model, "void"));
        primitiveMap.put(int.class, Entities.findEntityByName(model, "int"));
        primitiveMap.put(long.class, Entities.findEntityByName(model, "long"));
        primitiveMap.put(char.class, Entities.findEntityByName(model, "char"));
        primitiveMap.put(float.class, Entities.findEntityByName(model, "float"));
        primitiveMap.put(double.class, Entities.findEntityByName(model, "double"));
        primitiveMap.put(String.class, Entities.findEntityByName(model, "string"));
        primitiveMap.put(Class.class, Entities.findEntityByName(model, "class"));
        primitiveMap.put(boolean.class, Entities.findEntityByName(model, "boolean"));
        primitiveMap.put(Collection.class, Entities.findEntityByName(model, "set"));
        primitiveMap.put(List.class, Entities.findEntityByName(model, "sequence"));
        primitiveMap.put(Map.class, Entities.findEntityByName(model, "map"));
        primitiveMap.put(Object.class, Entities.findEntityByName(model, "object"));
        primitiveMap.put(java.util.Date.class, Entities.findEntityByName(model, "date"));
        primitiveMap.put(java.sql.Date.class, Entities.findEntityByName(model, "date"));

        List<Class<?>> classes = ClassFinder.find("org.lucci.lmu.input");

        // take all the classes in the jar files and convert them to LMU
        // Entities
        for (Class<?> thisClass : classes) {
            // if this is not an anonymous inner class (a.b$1)
            // we take it into account
            if (!thisClass.getName().matches(".+\\$[0-9]+")) {
                Entity entity = new Entity();
                entity.setName(computeEntityName(thisClass));
                entity.setNamespace(computeEntityNamespace(thisClass));
                entity_class.put(entity, thisClass);
                model.addEntity(entity);
            }
        }

        // at this only the name of entities is known
        // neither members nor relation are known
        // let's find them
        super.fillModel(model);

        return model;
    }
}