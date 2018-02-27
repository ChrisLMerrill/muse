package org.musetest.core.values;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StringExpressionContext
	{
	MuseProject getProject();
	StringExpressionContext getParentContext();
	ValueSourceConfiguration getParentSource();
	}

