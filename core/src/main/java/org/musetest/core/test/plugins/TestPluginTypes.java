package org.musetest.core.test.plugins;

import org.musetest.core.resource.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestPluginTypes
	{
	public TestPluginTypes(ClassLocator locator)
		{
		List<Class> implementors = locator.getImplementors(TestPluginType.class);
		for (Class the_class : implementors)
			{
			try
				{
				Object obj = the_class.newInstance();
				if (obj instanceof TestPluginType)
					{
					TestPluginType type = (TestPluginType) obj;
					if (_types.get(type.getTypeId()) != null)
						LOG.warn("Duplicate TestPluginType found for id: " + type.getTypeId());
					else
						_types.put(type.getTypeId().toLowerCase(), type);
					}
				}
			catch (Exception e)
				{
				LOG.error(e.getMessage());
				e.printStackTrace(System.err);
				// no need to deal with abstract implementations, etc
				}
			}
		}

	public Collection<TestPluginType> getAll()
		{
		return _types.values();
		}

	@SuppressWarnings("unused")  // used in UI
	public TestPluginType findType(String type_id)
		{
		return _types.get(type_id);
		}

	private Map<String, TestPluginType> _types = new HashMap<>();

	private final static Logger LOG = LoggerFactory.getLogger(TestPluginTypes.class);
	}


