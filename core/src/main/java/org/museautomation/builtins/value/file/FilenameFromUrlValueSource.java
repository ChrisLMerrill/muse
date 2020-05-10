package org.museautomation.builtins.value.file;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("filename-from-URL")
@MuseValueSourceName("Filename from URL")
@MuseValueSourceTypeGroup("File")
@MuseValueSourceShortDescription("Returns the filename part of the URL path")
@MuseValueSourceLongDescription("Resolves the sub-source and resolve it to a string. Interprets that as URL and then returns the last path segment, which is frequently a filename, particularly for static resources.")
@MuseStringExpressionSupportImplementation(FilenameFromUrlValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "URL", description = "URL to extract from", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused")  // used via reflection
public class FilenameFromUrlValueSource extends BaseValueSource
    {
    public FilenameFromUrlValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
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
        try
            {
            URL url = new URL(value);
            String[] paths = url.getPath().split("/");
            String filename = paths[paths.length - 1];
            context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), filename));
            return filename;
            }
        catch (MalformedURLException e)
            {
            String message = "URL parameter is not a valid URL: " + value;
            MessageEventType.raiseError(context, message);
            throw new ValueSourceResolutionError(message);
            }
        }

    private final MuseValueSource _subsource;

    public final static String TYPE_ID = FilenameFromUrlValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "filenameFromURL";
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
            return FilenameFromUrlValueSource.TYPE_ID;
            }
        }
    }