package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("nvp")
@MuseValueSourceName("Name/Value Pair")
@MuseValueSourceShortDescription("Creates a name/value pair.")
@MuseValueSourceLongDescription("Assembles a Pair from the name and value subsources")
@MuseSourceDescriptorImplementation(NameValuePairDescriptor.class)
@MuseSubsourceDescriptor(displayName = "Name", name = NameValuePairSource.NAME_PARAM, description = "The name of the pair", type = SubsourceDescriptor.Type.Named)
@MuseSubsourceDescriptor(displayName = "Value", name = NameValuePairSource.VALUE_PARAM, description = "The value of the pair", type = SubsourceDescriptor.Type.Named)
public class NameValuePairSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public NameValuePairSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);

        _name = getValueSource(config, NAME_PARAM, false, project);
        _value = getValueSource(config, VALUE_PARAM, false, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Object name = getValue(_name, context, true);
        Object value = getValue(_value, context, true);
        NameValuePair pair = new NameValuePair(name.toString(), value);

        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), String.format("(%s,%s)", name.toString(), value.toString())));
        return pair;
        }

    private MuseValueSource _name;
    private MuseValueSource _value;

    public final static String NAME_PARAM = "name";
    public final static String VALUE_PARAM = "value";

    public final static String TYPE_ID = NameValuePairSource.class.getAnnotation(MuseTypeId.class).value();
    }