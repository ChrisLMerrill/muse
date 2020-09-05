package org.museautomation.builtins.value.string;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("cut-string")
@MuseValueSourceName("Cut String")
@MuseValueSourceTypeGroup("Text")
@MuseValueSourceShortDescription("Cut a string")
@MuseValueSourceLongDescription("Cuts a string to a maximum length (from start or end).")
@MuseStringExpressionSupportImplementation(CutStringSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Source String", description = "The string to be cut", type = SubsourceDescriptor.Type.Named, name = CutStringSource.SOURCE_PARAM, defaultValue = "1,2,3")
@MuseSubsourceDescriptor(displayName = "Maximum Length", description = "The Maximum length of the resulting string", type = SubsourceDescriptor.Type.Named, name = CutStringSource.MAX_LENGTH_PARAM, defaultValue = "123")
@MuseSubsourceDescriptor(displayName = "From End", description = "Cut from the end of the string if true. Else, cut from the start.", type = SubsourceDescriptor.Type.Named, name = CutStringSource.FROM_END_PARAM, defaultValue = "true")
public class CutStringSource extends BaseValueSource
    {
    public CutStringSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _source = getValueSource(config, SOURCE_PARAM, true, project);
        _max_length = getValueSource(config, MAX_LENGTH_PARAM, true, project);
        _from_end = getValueSource(config, FROM_END_PARAM, true, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String source = getValue(_source, context, true, String.class);
        long max_length = getValue(_max_length, context, true, Long.class);
        boolean from_end = getValue(_from_end, context, true, Boolean.class);

        String result;
        if (source.length() <= max_length)
            result = source;
        else
            {
            long to_remove = source.length() - max_length;
            if (from_end)
                result = source.substring(0, (int)max_length);
            else
                result = source.substring((int)to_remove);
            }

        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), result));
        return result;
        }

    private final MuseValueSource _source;
    private final MuseValueSource _max_length;
    private final MuseValueSource _from_end;

    public final static String SOURCE_PARAM = "source";
    public final static String MAX_LENGTH_PARAM = "max-len";
    public final static String FROM_END_PARAM = "from-end";

    public final static String TYPE_ID = CutStringSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "cutString";
            }

        @Override
        protected int getNumberArguments()
            {
            return 3;
            }

        @Override
        protected String getTypeId()
            {
            return CutStringSource.TYPE_ID;
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {CutStringSource.SOURCE_PARAM, CutStringSource.MAX_LENGTH_PARAM, CutStringSource.FROM_END_PARAM };
            }
        }

    }