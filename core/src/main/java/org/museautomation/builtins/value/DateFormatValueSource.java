package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("date-format")
@MuseValueSourceName("Format date")
@MuseValueSourceTypeGroup("Text")
@MuseValueSourceShortDescription("Format a date as a string")
@MuseValueSourceLongDescription("Formats the 'date' param according the the 'format' string (see Javadocs for SimpleDateFormat). If date is not provided, the current date/time is used. If the format is not provided, the output is milliseconds since 1970.")
@MuseStringExpressionSupportImplementation(DateFormatValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Date", description = "Date object", type = SubsourceDescriptor.Type.Named, name = DateFormatValueSource.DATE_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Format", description = "format descriptor", type = SubsourceDescriptor.Type.Named, name = DateFormatValueSource.FORMAT_PARAM, optional = true, defaultValue = "EEE, d MMM yyyy HH:mm:ss Z")
public class DateFormatValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public DateFormatValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseExecutionError
        {
        super(config, project);
        _date = getValueSource(config, DATE_PARAM, false, project);
        _format = getValueSource(config, FORMAT_PARAM, false, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        DateFormat formatter = null;
        if (_format != null)
            {
            String format = getValue(_format, context, true, String.class);
            if (format != null)
                try
                    {
                    formatter = new SimpleDateFormat(format);
                    }
                catch (Exception e)
                    {
                    throw new ValueSourceResolutionError("The format parameter is not a valid format string: " + format);
                    }
            }

        Date date = new Date();
        if (_date == null)
            date = new Date();
        else
            {
            Object value = _date.resolveValue(context);
            if (value != null)
                {
                if (value instanceof Date)
                    date = (Date) value;
                else if (formatter != null)
                    {
                    try
                        {
                        date = formatter.parse(value.toString());
                        }
                    catch (ParseException e)
                        {
                        throw new ValueSourceResolutionError("The date parameter did not resolve to a date or a string that could be parsed using the supplied formatter: " + value.toString());
                        }
                    }
                }
            }

        String result;
        if (formatter == null)
            result = Long.toString(date.getTime());
        else
            result = formatter.format(date);
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), result));
        return result;
        }

    private MuseValueSource _date;
    private MuseValueSource _format;

    public final static String DATE_PARAM = "date";
    public final static String FORMAT_PARAM = "format";

    public final static String TYPE_ID = DateFormatValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "formatDate";
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected String getTypeId()
            {
            return DateFormatValueSource.TYPE_ID;
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {DateFormatValueSource.DATE_PARAM, DateFormatValueSource.FORMAT_PARAM };
            }
        }
    }
