package org.musetest.core.events;

import org.musetest.core.resource.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventTypes
	{
	public EventTypes(ClassLocator locator)
		{
		List<Class> implementors = locator.getImplementors(EventType.class);
		for (Class the_class : implementors)
			{
			if (the_class.equals(UnknownType.class))
				continue;
			try
				{
				Object obj = the_class.newInstance();
				if (obj instanceof EventType)
					{
					EventType type = (EventType) obj;
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

	public Collection<EventType> getAll()
		{
		return _types.values();
		}

	@SuppressWarnings("unused")  // used in UI
	public EventType findType(String type_id)
		{
		final EventType found = _types.get(type_id.toLowerCase());
		if (found != null)
			return found;

		EventType synthesized = new UnknownType(type_id);
		_types.put(type_id, synthesized);
		return synthesized;
		}

	private Map<String, EventType> _types = new HashMap<>();

	private final static Logger LOG = LoggerFactory.getLogger(EventType.class);

	public final static EventTypes DEFAULT = new EventTypes(DefaultClassLocator.get());

	public final static class UnknownType extends EventType
		{
		public UnknownType(String type_id)
			{
			_type_id = type_id;
			_name = "(" + _type_id + ")";
			}

		@Override
		public String getTypeId()
			{
			return _type_id;
			}

		@Override
		public String getName()
			{
			return _name;
			}

		private String _type_id;
		private String _name;
		}
	}