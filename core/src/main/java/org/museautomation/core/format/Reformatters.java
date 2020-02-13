package org.museautomation.core.format;

import org.museautomation.core.*;
import org.slf4j.*;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Reformatters
	{
	public static Reformatters get(MuseProject project)
		{
		Reformatters reformatters = REFORMATTERS.get(project);
		if (reformatters == null)
			{
			reformatters = new Reformatters(project);
			REFORMATTERS.put(project, reformatters);
			}
		return reformatters;
		}

	private Reformatters(MuseProject project)
		{
		_project = project;
		List<Class> implementors = project.getClassLocator().getImplementors(Reformatter.class);
		for (Class the_class : implementors)
			{
			if (!Modifier.isAbstract(the_class.getModifiers()))
				{
				try
					{
					the_class.newInstance();  // we can successfully instantiate without an exception
					_classes.add(the_class);
					}
				catch (Exception e)
					{
					LOG.error(String.format("Unable to instantiate a %s. Does it have a public no-args constructor?", the_class.getSimpleName()));
					// ignore this one
					}
				}
			}

		}

	public List<Reformatter> find(Object object, String format_type)
		{
		List<Reformatter> reformatters = new ArrayList<>();
		for (Class implementor : _classes)
			{
			try
				{
				Reformatter reformatter = (Reformatter) implementor.newInstance();  // we can successfully instantiate without an exception
				if (reformatter.canReformat(_project, object, format_type))
					reformatters.add(reformatter);
				}
			catch (Exception e)
				{
				LOG.error("Unexpected failure. Why are we unable to instantiate it here?", e);  // should have caught in constructor!
				}
			}
		return reformatters;
		}

	public List<Reformatter> find(File file, String input_type, String format_type)
		{
		List<Reformatter> reformatters = new ArrayList<>();
		for (Class implementor : _classes)
			{
			try
				{
				Reformatter reformatter = (Reformatter) implementor.newInstance();  // we can successfully instantiate without an exception
				if (reformatter.canReformat(_project, file, input_type, format_type))
					reformatters.add(reformatter);
				}
			catch (Exception e)
				{
				LOG.error("Unexpected failure. Why are we unable to instantiate it here?", e);  // should have caught in constructor!
				}
			}
		return reformatters;
		}

	private final MuseProject _project;
	private Set<Class> _classes = new HashSet<>();

	private final static Map<MuseProject, Reformatters> REFORMATTERS = new HashMap<>();

	private final static Logger LOG = LoggerFactory.getLogger(Reformatters.class);
	}