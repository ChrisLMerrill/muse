package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.text.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("date-format")
@MuseValueSourceName("Format date")
@MuseValueSourceShortDescription("Format a date as a string")
@MuseValueSourceLongDescription("Formats the 'date' param according the the 'format' string (see Javadocs for SimpleDateFormat). If date is not provided, the current date/time is used. If the format is not provided, the output is milliseconds since 1970.")
@MuseStringExpressionSupportImplementation(DateFormatValueSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Date", description = "Date object", type = SubsourceDescriptor.Type.Named, name = DateFormatValueSource.DATE_PARAM)
@MuseSubsourceDescriptor(displayName = "Format", description = "format descriptor", type = SubsourceDescriptor.Type.Named, name = DateFormatValueSource.FORMAT_PARAM)
public class DateFormatValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public DateFormatValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        ValueSourceConfiguration date_source = config.getSource(DATE_PARAM);
        if (date_source != null)
            _date = date_source.createSource(project);
        ValueSourceConfiguration format_source = config.getSource(FORMAT_PARAM);
        if (format_source != null)
            _format = format_source.createSource(project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        DateFormat formatter = null;
        if (_format != null)
            {
            Object value = _format.resolveValue(context);
            if (value != null)
                {
                String format = value.toString();
                try
                    {
                    formatter = new SimpleDateFormat(format);
                    }
                catch (Exception e)
                    {
                    throw new ValueSourceResolutionError("The format parameter is not a valid format string: " + format);
                    }
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

        if (formatter == null)
            return Long.toString(date.getTime());
        else
            return formatter.format(date);
        }

    private MuseValueSource _date = null;
    private MuseValueSource _format = null;

    public final static String DATE_PARAM = "date";
    public final static String FORMAT_PARAM = "format";

    public final static String TYPE_ID = DateFormatValueSource.class.getAnnotation(MuseTypeId.class).value();
    }
