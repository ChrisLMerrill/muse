package org.museautomation.builtins.value.string;

import com.fasterxml.jackson.databind.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("parse-json")
@MuseValueSourceName("Parse JSON")
@MuseValueSourceTypeGroup("Text")
@MuseValueSourceShortDescription("Parse JSON and return the result")
@MuseValueSourceLongDescription("Parses the sub-source (after resolving to a string) as JSON-formatted text and returns the resulting object, which could be a Map, List, Number, String, Boolean or null.")
@MuseStringExpressionSupportImplementation(FindByRegexSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Source", description = "JSON text to parse", type = SubsourceDescriptor.Type.Single)
public class ParseJsonValueSource extends BaseValueSource
    {
    public ParseJsonValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _subsource = getValueSource(config, true, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        if (MAPPER == null)
            MAPPER = new ObjectMapper();
        String json = getValue(_subsource, context, false, String.class);
        try
            {
            Object value = MAPPER.readValue(json.getBytes(), Object.class);
            context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), value));
            return value;
            }
        catch (IOException e)
            {
            throw new ValueSourceResolutionError("Unable to parse the source as JSON: " + e.getMessage());
            }
        }

    private final MuseValueSource _subsource;

    public final static String TYPE_ID = ParseJsonValueSource.class.getAnnotation(MuseTypeId.class).value();
    private static ObjectMapper MAPPER = null;

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "parseJson";
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return ParseJsonValueSource.TYPE_ID;
            }
        }
    }