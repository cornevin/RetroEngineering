package org.lucci.lmu.input;

import org.lucci.lmu.domain.AbstractModel;
import org.lucci.lmu.domain.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quentin on 30/01/16.
 */
public class ClazzAnalyser extends ModelCreator{

    private List<Class<?>> clazzes = new ArrayList<>();

    @Override
    protected AbstractModel createModel() {
        for (Class<?> thisClass: clazzes) {
            if (!thisClass.getName().matches(".+\\$[0-9]+")) {
                Entity entity = new Entity();
                entity.setName(computeEntityName(thisClass));
                entity.setNamespace(computeEntityNamespace(thisClass));
                entity_class.put(entity, thisClass);
                model.addEntity(entity);
            }
        }

        fillModel(model);
        return model;
    }


    public AbstractModel createModelFromClazzes(List<Class<?>> clazz) {
        this.clazzes = clazz;
        return createModel();
    }
}
