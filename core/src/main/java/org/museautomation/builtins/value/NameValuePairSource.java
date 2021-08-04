package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("nvp")
@MuseValueSourceName("Name/Value Pair")
@MuseValueSourceTypeGroup("Collections")
@MuseValueSourceShortDescription("Creates a name/value pair.")
@MuseValueSourceLongDescription("Assembles a Pair from the name and value subsources")
@MuseSourceDescriptorImplementation(NameValuePairDescriptor.class)
@MuseStringExpressionSupportImplementation(NameValuePairSource.StringExpressionSupport.class)
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

        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), String.format("(%s,%s)", name, value)));
        return pair;
        }

    private final MuseValueSource _name;
    private final MuseValueSource _value;

    public final static String NAME_PARAM = "name";
    public final static String VALUE_PARAM = "value";

    public final static String TYPE_ID = NameValuePairSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "nvPair";
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {NAME_PARAM,VALUE_PARAM};
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected String getTypeId()
            {
            return NameValuePairSource.TYPE_ID;
            }
        }

    }