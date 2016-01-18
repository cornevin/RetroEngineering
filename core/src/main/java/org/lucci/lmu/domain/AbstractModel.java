package org.lucci.lmu.domain;

import java.util.Collection;
import java.util.Set;

/**
 * Created by Quentin on 1/18/2016.
 */
public interface AbstractModel {
    Collection<Collection<Entity>> getAlignments();
    Set<Entity> getEntities();
    Set<Relation> getRelations();
    Collection<Group> getGroups();

    Collection<? extends Relation> removeEntity(Entity entity);
    void addRelation(Relation rel);
    void addEntity(Entity entity);
}
