package org.musetest.builtins.step.files;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("file-delete")
@MuseInlineEditString("delete {file}")
@MuseStepName("Delete a File")
@MuseStepTypeGroup("Files")
@MuseStepIcon("glyph:FontAwesome:REMOVE")
@MuseStepShortDescription("Delete a file from the local filesystem")
@MuseStepLongDescription("The 'message' source will be resolved and converted to a string. The result is added to a MessageEvent and sent to the event log for the test. It is also sent to the logging output (by default, standard out) at INFO level.")
@MuseSubsourceDescriptor(displayName = "Filename", description = "The full path and name of the file to delete", type = SubsourceDescriptor.Type.Named, name = DeleteFile.FILE_PARAM, defaultValue = "/path/to/file.txt")
@SuppressWarnings("unused")
public class DeleteFile extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public DeleteFile(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _file_path = getValueSource(config, FILE_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        String path = getValue(_file_path, context, false, String.class);
        File file = new File(path);
        if (!file.exists())
            {
            context.raiseEvent(MessageEventType.create("File does not exist: " + path));
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
            }
        if (file.delete())
            {
            context.raiseEvent(FileDeletedEventType.create(path));
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        else
            {
            context.raiseEvent(MessageEventType.create("Unable to delete file: " + path));
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
            }
        }

    private MuseValueSource _file_path;

    final static String FILE_PARAM = "file";
    public final static String TYPE_ID = DeleteFile.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(DeleteFile.class);
    }