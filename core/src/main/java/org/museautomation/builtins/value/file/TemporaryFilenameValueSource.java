package org.museautomation.builtins.value.file;

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
@MuseTypeId("temporary-filepath")
@MuseValueSourceName("Generate a temporary file path")
@MuseValueSourceTypeGroup("File")
@MuseValueSourceShortDescription("Create a file path in the temporary folder")
@MuseValueSourceLongDescription("Creates a full file path/name in the users temporary folder using the filename parameter")
@MuseStringExpressionSupportImplementation(TemporaryFilenameValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "filename", description = "Name for the file", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused")  // used via reflection
public class TemporaryFilenameValueSource extends BaseValueSource
    {
    public TemporaryFilenameValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _filename = getValueSource(config, true, getProject());
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String filename = getValue(_filename, context, false, String.class);
        String path = new File(new File(System.getProperty("java.io.tmpdir")), filename).getAbsolutePath();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), path));
        return path;
        }

    private final MuseValueSource _filename;

    public final static String TYPE_ID = TemporaryFilenameValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "tempFilePath";
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
            return TemporaryFilenameValueSource.TYPE_ID;
            }
        }
    }