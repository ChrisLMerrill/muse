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
@MuseTypeId("file-download")
@MuseInlineEditString("download {file} from {url}")
@MuseStepName("Download a File")
@MuseStepTypeGroup("Files")
@MuseStepIcon("glyph:FontAwesome:CLOUD_DOWNLOAD")
@MuseStepShortDescription("Download a file via URL to the local filesystem")
@MuseStepLongDescription("The 'message' source will be resolved and converted to a string. The result is added to a MessageEvent and sent to the event log for the test. It is also sent to the logging output (by default, standard out) at INFO level.")
@MuseSubsourceDescriptor(displayName = "Filename", description = "The full path and name of the file where the download will be stored", type = SubsourceDescriptor.Type.Named, name = DownloadFile.FILE_PARAM, defaultValue = "/path/to/file.txt")
@MuseSubsourceDescriptor(displayName = "URL", description = "The URL of the file to download", type = SubsourceDescriptor.Type.Named, name = DownloadFile.URL_PARAM, defaultValue = "http://")
@SuppressWarnings("unused")
public class DownloadFile extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public DownloadFile(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _url = getValueSource(config, URL_PARAM, true, project);
        _file_path = getValueSource(config, FILE_PARAM, true, project);
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
            byte[] chunk = new byte[4096];
            int bytesRead;
            while ((bytesRead = instream.read(chunk)) > 0)
                outstream.write(chunk, 0, bytesRead);

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

    final static String FILE_PARAM = "file";
    final static String URL_PARAM = "url";
    public final static String TYPE_ID = DownloadFile.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(DownloadFile.class);
    }