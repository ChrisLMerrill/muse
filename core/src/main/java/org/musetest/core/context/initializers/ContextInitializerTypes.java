package org.musetest.core.context.initializers;

import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ContextInitializerTypes
	{
	public ContextInitializerTypes(ClassLocator locator)
		{
		List<Class> implementors = locator.getImplementors(ContextInitializerType.class);
		for (Class the_class : implementors)
			{
			try
				{
				Object obj = the_class.newInstance();
				if (obj instanceof ContextInitializerType)
					{
					ContextInitializerType type = (ContextInitializerType) obj;
					if (_types.get(type.getTypeId()) != null)
						LOG.warn("Duplicate ContextInitializerType found for id: " + type.getTypeId());
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

	public Collection<ContextInitializerType> getAll()
		{
		return _types.values();
		}

	private Map<String, ContextInitializerType> _types = new HashMap<>();

	private final static Logger LOG = LoggerFactory.getLogger(ContextInitializerTypes.class);
	}


