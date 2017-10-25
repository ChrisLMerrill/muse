package org.musetest.core.project;

import org.musetest.core.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IdGenerator
	{
	private IdGenerator(MuseProject project)
		{
		_project = project;
		_config = _project.getResourceStorage().getResource("project-ids", IdGeneratorConfiguration.class);
		if (_config == null)
			{
			_config = new IdGeneratorConfiguration();
			_config.setId("project-ids");
			try
				{
				_project.getResourceStorage().addResource(_config);
				}
			catch (IOException e)
				{
				LOG.error("Unable to add IdGeneratorConfiguration to the project. Using temporary IdGenerator...which is more likely to create non-unique IDs.");
				}
			}
		}

	public synchronized long generateLongId()
		{
		long id = _config.getNextId();
		_config.setNextId(id + 1);
		_project.getResourceStorage().saveResource(_config);
		return id;
		}

	private MuseProject _project;
	private IdGeneratorConfiguration _config;

	public static synchronized IdGenerator get(MuseProject project)
		{
		IdGenerator generator = GENERATORS.get(project);
		if (generator == null)
			{
			generator = new IdGenerator(project);
			GENERATORS.put(project, generator);
			}
		return generator;
		}

	private static Map<MuseProject, IdGenerator> GENERATORS = new HashMap<>();
	private final static Logger LOG = LoggerFactory.getLogger(IdGenerator.class);
	}