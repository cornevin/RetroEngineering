package org.lucci.lmu.domain;

import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.domain.NamedModelElement;

import java.util.Collection;
import java.util.HashSet;

public class Package extends NamedModelElement
{
    Collection<Entity> entities = new HashSet<Entity>();
    
    public Collection<Entity> getEntities()
    {
        return this.entities;
    }
}
