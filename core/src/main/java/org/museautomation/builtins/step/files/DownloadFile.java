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
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("file-download")
@MuseInlineEditString("download {file} from {url}")
@MuseStepName("Download a File")
@MuseStepTypeGroup("Files")
@MuseStepIcon("glyph:FontAwesome:CLOUD_DOWNLOAD")
@MuseStepShortDescription("Download a file via URL to the local filesystem")
@MuseStepLongDescription("The 'file' and 'url' sources will be resolved and converted to strings. The URL is used to download content that will be stored in the file provided.")
@MuseSubsourceDescriptor(displayName = "Filename", description = "The full path and name of the file where the download will be stored", type = SubsourceDescriptor.Type.Named, name = DownloadFile.FILE_PARAM, defaultValue = "/path/to/file.txt")
@MuseSubsourceDescriptor(displayName = "URL", description = "The URL of the file to download", type = SubsourceDescriptor.Type.Named, name = DownloadFile.URL_PARAM, defaultValue = "http://")
@MuseSubsourceDescriptor(displayName = "Create folder", description = "Create the folder to hold the file, if needed?", type = SubsourceDescriptor.Type.Named, name = DownloadFile.CREATE_FOLDER_PARAM, optional = true, defaultValue = "true")
@SuppressWarnings("unused")
public class DownloadFile extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public DownloadFile(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _url = getValueSource(config, URL_PARAM, true, project);
        _file_path = getValueSource(config, FILE_PARAM, true, project);
        _create_folder = getValueSource(config, CREATE_FOLDER_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        String url_string = getValue(_url, context, false, String.class);
        String path = getValue(_file_path, context, false, String.class);
        File file = new File(path);
        if (file.exists())
            {
            context.raiseEvent(MessageEventType.create("File already exists: " + path));
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
            }

        File folder = file.getParentFile();
        if (!folder.exists())
            {
            boolean create = true;
            if (_create_folder != null)
                create = getValue(_create_folder, context, Boolean.class, true);
            if (create)
                {
                if (folder.mkdirs())
                    context.raiseEvent(FileCreatedEventType.create(folder.getAbsolutePath(), 0));
                else
                    {
                    context.raiseEvent(MessageEventType.create("Unable to create folder: " + folder.getAbsolutePath()));
                    return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
                    }
                }
            else
                {
                context.raiseEvent(MessageEventType.create("Target folder does not exist and step is configured to not create it: " + folder.getAbsolutePath()));
                return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
                }
            }

        URL url;
        try
            {
            url = new URL(url_string);
            }
        catch (MalformedURLException e)
            {
            context.raiseEvent(MessageEventType.create("Url parameter is not a valid URL: " + url_string));
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
            }

        try (FileOutputStream outstream = new FileOutputStream(file);
             InputStream instream = url.openStream())
            {
            context.raiseEvent(DownloadStartedEventType.create(url_string));
            byte[] chunk = new byte[4096];
            int chunk_length;
            int bytes_read = 0;
            while ((chunk_length = instream.read(chunk)) > 0)
                {
                outstream.write(chunk, 0, chunk_length);
                bytes_read += chunk_length;
                }
            context.raiseEvent(DownloadCompletedEventType.create(url_string, bytes_read));

            if (file.length() > 0)
                {
                context.raiseEvent(FileCreatedEventType.create(path, file.length()));
                return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
                }
            else
                {
                context.raiseEvent(MessageEventType.create("0 bytes downloaded from: " + url_string));
                return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
                }
            }
        catch (IOException e)
            {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
            String message = String.format("Unable to download %s to %s due to %s", url_string, path, e.getClass().getSimpleName());
            LOG.error(message, e);
            context.raiseEvent(MessageEventType.create(message));
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR);
            }
        }

    private MuseValueSource _file_path;
    private MuseValueSource _url;
    private MuseValueSource _create_folder;

    final static String FILE_PARAM = "file";
    final static String URL_PARAM = "url";
    final static String CREATE_FOLDER_PARAM = "create-folder";
    public final static String TYPE_ID = DownloadFile.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(DownloadFile.class);
    }