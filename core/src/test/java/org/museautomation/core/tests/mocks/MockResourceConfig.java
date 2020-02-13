package org.museautomation.core.tests.mocks;

import org.museautomation.core.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("mock-resource-type")
public class MockResourceConfig extends GenericResourceConfiguration
	{
	public MockResourceConfig()
		{
		}

	@Override
	public ResourceType getType()
		{
		return TYPE;
		}

	class MockResourceType extends ResourceType
		{
		MockResourceType()
			{
			super("mock-resource-type", "Mock Resource", MockResourceConfig.class);
			}
		}
	private final ResourceType TYPE = new MockResourceType();
	}


