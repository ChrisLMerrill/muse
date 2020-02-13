package org.museautomation.core.resource.generic;

import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultResourceDescriptor implements ResourceDescriptor
	{
	public DefaultResourceDescriptor(ResourceType type)
		{
		_type = type;
		}

	public DefaultResourceDescriptor(ResourceType type, String short_description)
		{
		_type = type;
		_short_description = short_description;
		}

	@Override
	public String getShortDescription()
		{
		if (_short_description == null)
			return _type.getInterfaceClass().getSimpleName();
		else
			return _short_description;
		}

	@Override
	public SubsourceDescriptor[] getSubsourceDescriptors()
		{
		if (_descriptors == null)
			{
			_descriptors = SubsourceDescriptor.getSubsourceDescriptors(_type.getInterfaceClass());
			if (_descriptors == null)
				_descriptors =  new SubsourceDescriptor[0];
			}
		return _descriptors;
		}

	private ResourceType _type;
	private String _short_description;
	private SubsourceDescriptor[] _descriptors = null;
	}


