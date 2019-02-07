package org.musetest.builtins.value.encoding;

import com.google.common.base.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("url-encode")
@MuseValueSourceName("URL Encode")
@MuseValueSourceTypeGroup("Encoding")
@MuseValueSourceShortDescription("Returns a URL-encoded version of the sub-source.")
@MuseValueSourceLongDescription("Resolves the sub-source and resolve it to a string. Encode and return the result.")
@MuseStringExpressionSupportImplementation(UrlEncoderValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Value", description = "subsource to encode (expects a string value)", type = SubsourceDescriptor.Type.Single)
public class UrlEncoderValueSource extends BaseValueSource
    {
    public UrlEncoderValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        if (config.getSource() != null)
            _subsource = config.getSource().createSource(project);
        else
            throw new MuseInstantiationException("Missing required parameter (subsource)");
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String value = _subsource.resolveValue(context).toString();
        value = URLEncoder.encode(value, Charsets.UTF_8);
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), value));
        return value;
        }

    private MuseValueSource _subsource;

    public final static String TYPE_ID = UrlEncoderValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "urlEncode";
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
            return UrlEncoderValueSource.TYPE_ID;
            }
        }
    }