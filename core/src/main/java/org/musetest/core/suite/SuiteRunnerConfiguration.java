package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class SuiteRunnerConfiguration extends GenericResourceConfiguration
	{
	public ResourceType getType()
		{
		return TYPE;
		}

	public abstract MuseTestSuiteRunner createRunner(MuseExecutionContext context) throws MuseExecutionError;

	/**
	 * Used for JSON serialization. Use parameters() to get access to the sub-sources
	 * for this configuration.
	 */
	public Map<String, ValueSourceConfiguration> getParameters()
		{
		return _sources.getSourceMap();
		}

	public void setParameters(Map<String, ValueSourceConfiguration> sources)
		{
		_sources.setSourceMap(sources);
		}

	public NamedSourcesContainer parameters()
		{
		return _sources;
		}

	private NamedSourcesContainer _sources = new NamedSourcesContainer();

	public static class SuiteRunnerConfigurationType extends ResourceType
		{
		SuiteRunnerConfigurationType()
			{
			super(TYPE_ID, "Test Suite Runner", SuiteRunnerConfiguration.class);
			}
		}

	public final static String TYPE_ID = "suite-runner";

	@SuppressWarnings("WeakerAccess")  // subtypes need access
	public final static SuiteRunnerConfigurationType TYPE = new SuiteRunnerConfigurationType();
	}