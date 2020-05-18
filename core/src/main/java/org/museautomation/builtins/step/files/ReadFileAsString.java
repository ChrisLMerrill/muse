package org.museautomation.builtins.step.files;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.slf4j.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("read-file-string")
@MuseInlineEditString("read {file} into {varname}")
@MuseStepName("Read a text file")
@MuseStepTypeGroup("Files")
@MuseStepIcon("glyph:FontAwesome:FILE")
@MuseStepShortDescription("Read a file from the local filesystem")
@MuseStepLongDescription("The 'Filename' source will be resolved as a string and converted to a file path. If the file exists, it will be read as a text file.")
@MuseSubsourceDescriptor(displayName = "Filename", description = "The full path and name of the file to read", type = SubsourceDescriptor.Type.Named, name = ReadFileAsString.FILE_PARAM, defaultValue = "/path/to/file.txt")
@MuseSubsourceDescriptor(displayName = "Variable Name", description = "The name of the variable to store the file contents in", type = SubsourceDescriptor.Type.Named, name = ReadFileAsString.VARIABLE_PARAM, defaultValue = "file-contents")
@SuppressWarnings("unused")
public class ReadFileAsString extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public ReadFileAsString(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _file_path = getValueSource(config, FILE_PARAM, true, project);
        _var_name = getValueSource(config, VARIABLE_PARAM, true, project);
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

        String varname = getValue(_var_name, context, false, String.class);
        try
            {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            context.setVariable(varname, content);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        catch (IOException e)
            {
            LOG.error("Unable to read file", e);
            context.raiseEvent(MessageEventType.create("Unable to read file contents: " + path));
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
            }
        }

    private final MuseValueSource _file_path;
    private final MuseValueSource _var_name;

    final static String FILE_PARAM = "file";
    final static String VARIABLE_PARAM = "varname";
    public final static String TYPE_ID = ReadFileAsString.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(ReadFileAsString.class);
    }