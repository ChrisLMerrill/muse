package org.musetest.core.resource.generic;

import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceDescriptor
	{
	String getShortDescription();
	SubsourceDescriptor[] getSubsourceDescriptors();
	}

