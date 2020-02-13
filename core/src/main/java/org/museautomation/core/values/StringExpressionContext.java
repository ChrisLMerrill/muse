package org.museautomation.core.values;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StringExpressionContext
	{
	MuseProject getProject();
	StringExpressionContext getParentContext();
	ValueSourceConfiguration getParentSource();
	}

