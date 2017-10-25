package org.musetest.core.project;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("idgen")
public class IdGeneratorConfiguration extends BaseMuseResource
	{
	public IdGeneratorConfiguration()
		{
		_next_id = new Random().nextLong();
		}

	public long getNextId()
		{
		return _next_id;
		}

	public void setNextId(long next_id)
		{
		_next_id = next_id;
		}

	private long _next_id;

	@Override
	public ResourceType getType()
		{
		return new IdGeneratorConfigurationResourceType();
		}

	@SuppressWarnings("unused,WeakerAccess")
	// discovered and instantiated by reflection (see class ResourceTypes)
	class IdGeneratorConfigurationResourceType extends ResourceType
		{
		public IdGeneratorConfigurationResourceType()
			{
			super(TYPE_ID, "ID Generator", IdGeneratorConfiguration.class);
			}

		@Override
		public MuseResource create()
			{
			return new IdGeneratorConfiguration();
			}
		}

	public final static String TYPE_ID = IdGeneratorConfiguration.class.getAnnotation(MuseTypeId.class).value();
	}


