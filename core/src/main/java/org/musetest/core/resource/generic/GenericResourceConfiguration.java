package org.musetest.core.resource.generic;

import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class GenericResourceConfiguration extends BaseMuseResource
	{
	/**
	 * Used for JSON serialization. Use parameters() to get access to the sub-sources
	 * for this configuration.
	 */
	public Map<String, ValueSourceConfiguration> getSources()
		{
		return _sources.getSourceMap();
		}

	public void setSources(Map<String, ValueSourceConfiguration> sources)
		{
		_sources.setSourceMap(sources);
		}

	public NamedSourcesContainer parameters()
		{
		return _sources;
		}

	private NamedSourcesContainer _sources = new NamedSourcesContainer();
	}