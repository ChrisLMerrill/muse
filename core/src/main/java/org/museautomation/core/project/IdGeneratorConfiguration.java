package org.museautomation.core.project;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("idgen")
@Deprecated
public class IdGeneratorConfiguration extends BaseMuseResource
	{
	public IdGeneratorConfiguration()
		{
		// pick a wide range of positive longs
		_next_id = ((long)Math.abs(new Random().nextInt())) << 16;
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
    @Deprecated
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

    public static IdGeneratorConfiguration get(MuseProject project)
        {
        return project.getResourceStorage().getResource("project-ids", IdGeneratorConfiguration.class);
        }

	public final static String TYPE_ID = IdGeneratorConfiguration.class.getAnnotation(MuseTypeId.class).value();
	}


