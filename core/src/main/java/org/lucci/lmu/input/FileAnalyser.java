package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.Entity;
import toools.io.file.RegularFile;

import java.io.File;

/**
 * Created by Quentin on 1/19/2016.
 */
public class FileAnalyser extends ModelCreator {

    public FileAnalyser() {
        super();
    }

    @Override
    public AbstractModel createModel(String path) throws ParseError, ModelException {
        RegularFile regularFile = new RegularFile(path);
        File test = regularFile.toFile();

        Entity entity = new Entity();
        entity.setName(computeEntityName(test.getClass()));
        entity.setNamespace(computeEntityNamespace(test.getClass()));
        entity_class.put(entity, test.getClass());
        model.addEntity(entity);

        super.fillModel(model);
        return model;
    }
}
