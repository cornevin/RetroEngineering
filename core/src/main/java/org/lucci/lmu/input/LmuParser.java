package org.lucci.lmu.input;

import org.lucci.lmu.domain.*;
import toools.collections.Collections;
import toools.io.file.RegularFile;
import toools.text.TextUtilities;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class LmuParser extends ModelCreator
{
    private final static LmuParser parser = new LmuParser();

    public static LmuParser getParser()
    {
	return parser;
    }

    private LmuParser()
    {
    }

    private final Map<RegularFile, Model> modelCache = new HashMap<RegularFile, Model>();
    private int lineNumber;
    private Model model;
    private Entity currentEntity = null;
    private String comment = "";

    @Override
    public Model createModel(byte[] data) throws ParseError, ModelException
    {
	return createModel(new String(data));
    }

    public Model createModel(String text) throws ParseError, ModelException
    {
	return createModel(Arrays.asList(text.split("\n")));
    }

    private Model createModel(List<String> lines) throws ParseError, ModelException
    {
	List<List<String>> tokens = new ArrayList<List<String>>();

	for (String thisLine : lines)
	{
	    thisLine = thisLine.trim();

	    if (thisLine.isEmpty())
	    {
		tokens.add(new ArrayList<String>());
	    }
	    else
	    {
		tokens.add(Arrays.asList(thisLine.split(" +")));
	    }
	}

	return createMode(tokens);
    }

    private Model createMode(List<List<String>> tokens) throws ParseError, ModelException
    {
	this.model = new Model();
	createEntities(model, tokens);
	comment = "";
	currentEntity = null;

	for (lineNumber = 1; lineNumber <= tokens.size(); ++lineNumber)
	{
	    List<String> line = tokens.get(lineNumber - 1);

	    if (line.size() == 0)
	    {
		// a blank line means that the current entity declaration ends
		// here
		currentEntity = null;
	    }
	    else if (line.size() > 0)
	    {
		String statement = line.get(0);

		if (statement.startsWith("#"))
		{
		    comment += statement.substring(1).trim();

		    for (int word = 1; word < line.size(); ++word)
		    {
			comment += " " + line.get(word);
		    }
		}
		else
		{
		    try
		    {
			getClass().getMethod('_' + statement, List.class).invoke(this, line);
		    }
		    catch (IllegalArgumentException e)
		    {
			e.printStackTrace();
		    }
		    catch (IllegalAccessException e)
		    {
			e.printStackTrace();
		    }
		    catch (InvocationTargetException e)
		    {
			if (e.getCause() instanceof ParseError)
			{
			    throw (ParseError) e.getCause();
			}
			else if (e.getCause() instanceof ModelException)
			{
			    throw (ModelException) e.getCause();
			}
			else
			{
			    e.printStackTrace();
			}
		    }
		    catch (SecurityException e1)
		    {
			e1.printStackTrace();
		    }
		    catch (NoSuchMethodException e1)
		    {
			// show packages in .*
			if (statement.equalsIgnoreCase("show") || statement.equalsIgnoreCase("hide"))
			{
			    if (line.size() == 4)
			    {
				boolean visible = statement.equalsIgnoreCase("show");
				String target = line.get(1);
				Set<Entity> entities = Entities.findEntitiesWhoseNameMatch(model.getEntities(),
					line.get(3));

				if (line.get(2).equals("in"))
				{
				    if (target.equals("packages"))
				    {
					try
					{
					    Entities.removeJavaPackageNames(entities, model);
					}
					catch (ModelException e)
					{
					    e.setLine(lineNumber);
					    throw e;
					}
				    }
				    else if (target.equals("attributes"))
				    {
					for (Entity entity : entities)
					{
					    for (ModelElement modelElement : entity.getAttributes())
					    {
						modelElement.setVisible(visible);
					    }

					}
				    }
				    else if (target.equals("operations"))
				    {
					for (Entity entity : entities)
					{
					    for (ModelElement modelElement : entity.getOperations())
					    {
						modelElement.setVisible(visible);
					    }
					}
				    }
				    else
				    {
					syntax("target '"
						+ target
						+ "' is unknown. Only 'attributes' 'operations' and 'packages' are allowed");
				    }
				}
				else
				{
				    syntax("show and hides command attributes|operations|packages regexp");

				}
			    }
			    else
			    {
				syntax("Syntax: hide and show commands require 2 parameters: attributes|operations|packages in regexp");

			    }
			}
			else if (TextUtilities.concatene(line, " ").equals("remove_entities"))
			{
			    if (line.size() < 2)
			    {
				syntax("remove_entities [e]");
			    }
			    else
			    {
				String regexp = line.get(1);
				Collection<Entity> entities = Entities.findEntitiesWhoseNameMatch(model.getEntities(),
					regexp);
				Entities.removeEntities(entities, model);
			    }
			}
			else if (statement.equalsIgnoreCase("retain_entities_connected_to_namespace"))
			{
			    if (line.size() < 2)
			    {
				syntax("retain_entities_connected_to_namespace ns [distance]");
			    }
			    else
			    {
				try
				{
				    String regexp = line.get(1);
				    int distance = line.size() > 2 ? Integer.valueOf(line.get(2)) : Integer.MAX_VALUE;
				    Set<Entity> srcEntities = Entities.findEntityWhoseNameSpaceMatches(
					    model.getEntities(), regexp);

				    if (srcEntities.isEmpty())
				    {
					syntax("no entities found!");
				    }
				    else
				    {
					Set<Entity> entitiesToKeep = Entities.findEntitiesConnectedTo(srcEntities,
						distance, model);
					Set<Entity> entitiesToRemove = (Set<Entity>) Collections.difference(
						model.getEntities(), entitiesToKeep);
					Entities.removeEntities(entitiesToRemove, model);
				    }
				}
				catch (NumberFormatException e)
				{
				    syntax("'retain_entities_connected_to' entity [distance]");
				}
			    }
			}
			else if (statement.equalsIgnoreCase("remove_package_names"))
			{
			    if (line.size() != 1)
			    {
				syntax("no argument allowed");
			    }
			    else
			    {
				Entities.removeJavaPackageNames(model.getEntities(), model);
			    }
			}
			else if (statement.equalsIgnoreCase("remove_isolated_entities"))
			{
			    if (line.size() != 1)
			    {
				syntax("no argument allowed");
			    }
			    else
			    {
				Collection<Entity> isolatedEntities = Entities.findIsolatedEntities(
					model.getEntities(), model);
				Entities.removeEntities(isolatedEntities, model);
			    }
			}
			else if (statement.equalsIgnoreCase("retain_isolated_entities"))
			{
			    if (line.size() != 1)
			    {
				syntax("no argument allowed");
			    }
			    else
			    {
				Entities.retainEntities(Entities.findIsolatedEntities(model.getEntities(), model),
					model);
			    }
			}
			else if (statement.equalsIgnoreCase("retain_largest_connected_component"))
			{
			    if (line.size() != 1)
			    {
				syntax("no argument allowed");
			    }
			    else
			    {
				Set<Entity> s = Entities.findLargestConnectedComponent(model.getEntities(), model);
				Entities.retainEntities(s, model);
			    }
			}
			else if (statement.equalsIgnoreCase("retain_entities_connected_to"))
			{
			    if (line.size() != 2 && line.size() != 3)
			    {
				syntax("entity [distance]");
			    }
			    else
			    {
				try
				{
				    String regexp = line.get(1);
				    int distance = line.size() > 2 ? Integer.valueOf(line.get(2)) : Integer.MAX_VALUE;
				    Set<Entity> srcEntities = Entities.findEntitiesWhoseNameMatch(model.getEntities(),
					    regexp);

				    if (srcEntities.isEmpty())
				    {
					syntax("no entities found!");
				    }
				    else
				    {
					Set<Entity> entitiesToKeep = Entities.findEntitiesConnectedTo(srcEntities,
						distance, model);
					Set<Entity> entitiesToRemove = (Set<Entity>) Collections.difference(
						model.getEntities(), entitiesToKeep);
					Entities.removeEntities(entitiesToRemove, model);
				    }
				}
				catch (NumberFormatException e)
				{
				    syntax("'retain_entities_connected_to' entity [distance]");
				}
			    }
			}
			else if (statement.equalsIgnoreCase("hide_non_public_elements"))
			{
			    if (line.size() != 1)
			    {
				syntax("no argument allowed");
			    }
			    else
			    {
				for (ModelElement me : Models.findAllModelElementsInModel(model))
				{
				    if (me.getVisibility() != Visibility.PUBLIC)
				    {
					me.setVisible(false);
				    }
				}
			    }
			}
			else if (statement.equalsIgnoreCase("entity"))
			{
			    if (line.size() != 2)
			    {
				syntax("'entity' name");
			    }
			    else
			    {
				String entityName = line.get(1);
				currentEntity = Entities.findEntityByName(model, entityName);
				currentEntity.setComment(comment);
				comment = "";
			    }
			}
			else if (statement.equalsIgnoreCase("color"))
			{
			    if (currentEntity == null)
			    {
				syntax("the <b>'" + statement
					+ "'</b> statement doesn't belong to any entity declaration");
			    }
			    else if (line.size() != 2)
			    {
				syntax("'color' color_name");
			    }
			    else
			    {
				currentEntity.setColorName(line.get(1));
			    }
			}
			else if (statement.equalsIgnoreCase("extends"))
			{
			    if (currentEntity == null)
			    {
				syntax("the <b>'" + statement
					+ "'</b> statement doesn't belong to any entity declaration");
			    }
			    else if (line.size() < 2)
			    {
				throw new ParseError(
					lineNumber,
					"the <b>'extends'</b> keyword must be followed by a whitespace separated list of entity names",
					"extends <i>entity_name</i>");
			    }
			    else
			    {
				for (int i = 1; i < line.size(); ++i)
				{
				    String superEntityName = line.get(i);
				    Entity superEntity = Entities.findEntityByName(model, superEntityName);

				    if (superEntity == null)
				    {
					syntax("undeclared entity: <b>" + superEntityName + "</b>");
				    }
				    else
				    {
					InheritanceRelation rel = new InheritanceRelation(currentEntity, superEntity);
					model.addRelation(rel);
				    }
				}
			    }
			}
			else
			{
			    syntax("don't know what to do with statement " + statement);
			}
		    }
		}
	    }
	}

	return model;
    }

    private void syntax(String s) throws ParseError
    {
	throw new ParseError(lineNumber, s);
    }

    private void syntax(String s, String suggestion) throws ParseError
    {
	throw new ParseError(lineNumber, s, suggestion);
    }

    private void createEntities(Model model, List<List<String>> lines) throws ParseError
    {
	// first instantiate all explicitely declared entities
	// for this, only "entity" lines are considered
	for (int lineNumber = 1; lineNumber <= lines.size(); ++lineNumber)
	{
	    List<String> line = lines.get(lineNumber - 1);

	    if (line.size() > 0)
	    {
		String statement = line.get(0);

		if (statement.equalsIgnoreCase("entity"))
		{
		    if (line.size() == 2)
		    {
			String name = line.get(1);

			if (Entities.findEntityByName(model, name) == null)
			{
			    Entity entity = new Entity();
			    entity.setName(name);
			    model.addEntity(entity);
			}
			else
			{
			    syntax("entity already declared: <b>" + name + "</b>");
			}
		    }
		    else
		    {
			syntax("the '<b>entity</b>' keyword must be followed by the name of the entity",
				"entity <i>name</i>");
		    }
		}
	    }
	}
    }

}
