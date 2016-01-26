package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.Entities;
import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.domain.Model;
import toools.io.file.AbstractFile;
import toools.io.file.Directory;
import toools.io.file.RegularFile;

import java.io.File;
import java.util.*;

public class FolderAnalyser extends ModelCreator {

    public FolderAnalyser() {
        this.knownJarFiles = new HashSet<RegularFile>();
        this.primitiveMap = new HashMap<Class<?>, Entity>();
        this.entity_class = new HashMap<Entity, Class<?>>();
    }

    public Collection<RegularFile> getJarFiles() {
        return this.knownJarFiles;
    }

    @Override
    public AbstractModel createModel(String path) throws ParseError {
        AbstractModel model = new Model();
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


        Directory directory = new Directory(path);
        for(AbstractFile abstractFile : directory.getChildFiles()) {
            File file = abstractFile.toFile();
            Entity entity = new Entity();
            entity.setName(computeEntityName(file.getClass()));
            entity.setNamespace(computeEntityNamespace(file.getClass()));
            entity_class.put(entity, file.getClass());
            model.addEntity(entity);
        }

        super.fillModel(model);
        return model;
    }
}