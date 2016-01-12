package org.lucci.lmu.output;

import java.util.Collection;
import java.util.Iterator;

import org.lucci.lmu.domain.Attribute;
import org.lucci.lmu.domain.Entity;
import org.lucci.lmu.domain.Model;
import org.lucci.lmu.domain.ModelElement;
import org.lucci.lmu.domain.Operation;
import org.lucci.lmu.domain.Visibility;



/*
 * Created on Oct 8, 2004
 */

/**
 * @author luc.hogie
 */
public class JavaSourceWriter extends AbstractWriter {
	
	/* (non-Javadoc)
	 * @see org.lucci.lmu.ViewFactory#createViewData(org.lucci.lmu.model.ClassDiagram)
	 */
	public byte[] writeModel(Model model) throws WriterException {
		String source = "";
		Collection<Entity> entities = ModelElement.findVisibleModelElements(model.getEntities());
//		Collections.sort(entities, new Comparator<Entity>()
//				{
//					public int compare(Entity e1, Entity e2)
//					{
//						return e1.getName().compareTo(e2.getName());
//					}
//				});

		for (Entity entity : entities) {
			source += getJavaSource(entity) + "\n\n";
		}
		
		return source.getBytes();
	}

	public String getJavaSource(Entity entity) throws WriterException {
		String source = "";
		
		source += "\nclass " + entity.getName() + "\n{";
		Iterator attributeIterator = entity.getAttributes().iterator();
		
		while (attributeIterator.hasNext()) {
			Attribute attribute = (Attribute) attributeIterator.next();
			
			if (attribute.getType() == null) {
				throw new WriterException("type for attribute " + entity.getName() + "#" + attribute.getName() + " is undefined");
			} else {
				source += "\n\t" + getJavaVisibility(attribute.getVisibility())
					+ " " + getJavaType(attribute.getType().getName())
					+ " " + attribute.getName() + ";";
			}
		}

		Iterator operationIterator = entity.getOperations().iterator();
		
		while (operationIterator.hasNext()) {
			Operation operation = (Operation) operationIterator.next();
			
			if (operation.getType() == null) {
				throw new WriterException("type for operation " + entity.getName() + "#" + operation.getName() + " is undefined");
			} else {
				source += "\n\t " + getJavaVisibility(operation.getVisibility())
					+ " " + getJavaType(operation.getType().getName()) + " "
					+ operation.getName() + "(";
			
				Iterator<Entity> parameterIterator = operation.getParameterList().iterator();
				
				while (parameterIterator.hasNext()) {
					Entity parm = parameterIterator.next();
					source += parm.getName();
					
					if (parameterIterator.hasNext()) {
						source += ", ";
					}
				}				
				
				source = ");";
			}
		}

		source += "\n}";
		return source;
	}
	
	private String getJavaVisibility(Visibility v) {
		if (v == Visibility.PUBLIC) {
			return "public";
		} else if (v == Visibility.PROTECTED) {
			return "protected";
		} else if (v == Visibility.PRIVATE) {
			return "private";
		} else {
			throw new IllegalArgumentException("unknow visilibity " + v);
		}
	}


	private String getJavaType(String t) {
		// TODO replace if else by switch case
		switch (t) {
			case "string":
				return "String";
			case "set":
				return "java.util.Set";
			case "sequence":
				return "java.util.List";
			case "class":
				return "Class";
			default:
				return t;
		}
	}
}
