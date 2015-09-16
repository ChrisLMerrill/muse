package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("concatenate")
@MuseValueSourceName("Concatenate")
@MuseSourceDescriptorImplementation(StringConcatenationDescriptor.class)
@MuseValueSourceShortDescription("A concatention of the string value of multiple sub-sources.")
@MuseValueSourceLongDescription("Returns a string constructed from the sub-source list. For sub-sources that resolve to a non-string value, they will be converted to their string form by calling toString() on the object.")
public class StringConcatenationSource implements MuseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public StringConcatenationSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        _config = config;
        _project = project;

        List<ValueSourceConfiguration> configs = config.getSourceList();
        if (configs == null || configs.size() == 0)
            throw new MuseInstantiationException("Missing required parameter (sourceList)");

        List<MuseValueSource> sources = new ArrayList<>();
        for (ValueSourceConfiguration source : configs)
            sources.add(source.createSource(project));

        _sources = sources.toArray(new MuseValueSource[sources.size()]);
        }

    @Override
    public Object resolveValue(StepExecutionContext context) throws StepConfigurationError
        {
        StringBuilder builder = new StringBuilder();
        for (MuseValueSource source : _sources)
            builder.append(source.resolveValue(context));
        String result = builder.toString();
        context.getTestExecutionContext().raiseEvent(new ValueSourceResolvedEvent(_project.getValueSourceDescriptors().get(_config).getInstanceDescription(_config), result));
        return result;
        }

    @Override
    public String getDescription()
        {
        return null;
        }

    public MuseValueSource[] getSources()
        {
        return _sources;
        }

    MuseValueSource[] _sources;
    ValueSourceConfiguration _config;
    MuseProject _project;

    public final static String TYPE_ID = StringConcatenationSource.class.getAnnotation(MuseTypeId.class).value();
    }


