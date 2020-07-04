package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("split-string")
@MuseValueSourceName("Split String")
@MuseValueSourceTypeGroup("Text")
@MuseValueSourceShortDescription("Split a string into a list")
@MuseValueSourceLongDescription("Formats the 'date' param according the the 'format' string (see Javadocs for SimpleDateFormat). If date is not provided, the current date/time is used. If the format is not provided, the output is milliseconds since 1970.")
@MuseStringExpressionSupportImplementation(DateFormatValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Source String", description = "The string to be split", type = SubsourceDescriptor.Type.Named, name = SplitStringIntoListValueSource.SOURCE_PARAM, defaultValue = "1,2,3")
@MuseSubsourceDescriptor(displayName = "Splitting expression", description = "The expression to use when splitting the string. Note that this is a Java Regular expression so characters, such as '.', require escaping with '\\'. ", type = SubsourceDescriptor.Type.Named, name = SplitStringIntoListValueSource.DELIMITER_PARAM, defaultValue = ",")
public class SplitStringIntoListValueSource extends BaseValueSource
    {
    public SplitStringIntoListValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _source = getValueSource(config, SOURCE_PARAM, true, project);
        _delimiter = getValueSource(config, DELIMITER_PARAM, true, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String source = getValue(_source, context, true, String.class);
        String delimiter = getValue(_delimiter, context, true, String.class);

        String[] split = source.split(delimiter);
        List<String> result = Arrays.asList(split);

        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), result));
        return result;
        }

    private final MuseValueSource _source;
    private final MuseValueSource _delimiter;

    public final static String SOURCE_PARAM = "source";
    public final static String DELIMITER_PARAM = "delimter";

    public final static String TYPE_ID = SplitStringIntoListValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "splitString";
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected String getTypeId()
            {
            return SplitStringIntoListValueSource.TYPE_ID;
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {SplitStringIntoListValueSource.SOURCE_PARAM, SplitStringIntoListValueSource.DELIMITER_PARAM };
            }
        }

    }