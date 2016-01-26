package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.Entity;
import toools.io.file.AbstractFile;
import toools.io.file.Directory;
import toools.io.file.RegularFile;

import java.io.File;
import java.util.Collection;

public class FolderAnalyser extends ModelCreator {

    public FolderAnalyser() {
        super();
    }

    public Collection<RegularFile> getJarFiles() {
        return this.knownJarFiles;
    }

    @Override
    public AbstractModel createModel(String path) throws ParseError {

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