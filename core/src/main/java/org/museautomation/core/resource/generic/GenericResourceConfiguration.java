package org.museautomation.core.resource.generic;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.builtins.plugins.resultstorage.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * A MuseResource that uses a general set of configuration parameters. It is intended for resources which
 * have a few basic configurations for the user to supply. When used in conjunction with with the supported
 * annotations, the IDE can generate an editor for those configuration settings  without needing custom UI code.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class GenericResourceConfiguration extends BaseMuseResource
    {
    /**
     * Used for JSON serialization. Use parameters() to get access to the sub-sources
     * for this configuration.
     */
    public Map<String, ValueSourceConfiguration> getParameters()
        {
        return _parameters.getSourceMap();
        }

    public void setParameters(Map<String, ValueSourceConfiguration> sources)
        {
        _parameters.setSourceMap(sources);
        }

    public NamedSourcesContainer parameters()
        {
        return _parameters;
        }

    protected NamedSourcesContainer _parameters = new NamedSourcesContainer();

    protected String getParameterAsString(MuseExecutionContext context, String name) throws MuseInstantiationException, ValueSourceResolutionError
        {
        MuseValueSource source = BaseValueSource.getValueSource(_parameters, name, true, context.getProject());
        return BaseValueSource.getValue(source, context, false, String.class);
        }

    @SuppressWarnings("unused")  // expect extensions to use this
    @JsonIgnore
    protected boolean isParameterTrue(MuseExecutionContext context, String name)
        {
        return isParameterTrue(context, name, false);
        }

    @SuppressWarnings("unused")  // expect extensions to use this
    @JsonIgnore
    protected boolean isParameterTrue(MuseExecutionContext context, String name, boolean default_value)
        {
        if (_parameters != null)
            {
            try
                {
                MuseValueSource source = BaseValueSource.getValueSource(_parameters, name, true, context.getProject());
                return BaseValueSource.getValue(source, context, false, Boolean.class);
                }
            catch (MuseInstantiationException | ValueSourceResolutionError e)
                {
                return default_value;
                }
            }
        return default_value;
        }
    }