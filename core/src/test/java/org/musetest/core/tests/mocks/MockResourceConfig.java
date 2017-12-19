package org.musetest.core.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;

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
		public MockResourceType()
			{
			super("mock-resource-type", "Mock Resource", MockResourceConfig.class);
			}
		}
	final ResourceType TYPE = new MockResourceType();
	}


