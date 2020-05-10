package org.museautomation.builtins.value.file;

import org.museautomation.builtins.plugins.resultstorage.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("task-filepath")
@MuseValueSourceName("Create a file path in the task folder")
@MuseValueSourceTypeGroup("File")
@MuseValueSourceShortDescription("Create a file path in the task output folder")
@MuseValueSourceLongDescription("Creates a full file path/name in the task output folder. This relies on the Local Storage Location plugin.")
@MuseStringExpressionSupportImplementation(TaskFilenameValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "filename", description = "Name for the file", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused")  // used via reflection
public class TaskFilenameValueSource extends BaseValueSource
    {
    public TaskFilenameValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _filename = getValueSource(config, true, getProject());
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String filename = getValue(_filename, context, false, String.class);

        LocalStorageLocationPlugin storage = Plugins.findType(LocalStorageLocationPlugin.class, context);
        if (storage == null)
            {
            String message = "This value source requires a LocalStorageLocation plugin, but none was found.";
            MessageEventType.raiseError(context, message);
            throw new ValueSourceResolutionError(message);
            }
        TaskExecutionContext task_context = MuseExecutionContext.findAncestor(context, TaskExecutionContext.class);
        if (task_context == null)
            {
            String message = "This value source can only execute within a TaskExecutionContext.";
            MessageEventType.raiseError(context, message);
            throw new ValueSourceResolutionError(message);
            }

        String path = new File(storage.getTaskFolder(task_context), filename).getAbsolutePath();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), path));
        return path;
        }

    private final MuseValueSource _filename;

    public final static String TYPE_ID = TaskFilenameValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "taskFilePath";
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
            return TaskFilenameValueSource.TYPE_ID;
            }
        }
    }