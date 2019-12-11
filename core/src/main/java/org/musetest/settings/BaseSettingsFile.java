package org.musetest.settings;

import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used in UI and other derivative projects
public abstract class BaseSettingsFile implements Closeable
    {
    protected static Object load(Class<? extends BaseSettingsFile> type, String filename, String subfolder, ObjectMapper mapper)
        {
        return load(type, subfolder + "/" + filename, mapper);
        }

    protected static Object load(Class<? extends BaseSettingsFile> type, String filename, ObjectMapper mapper)
        {
        return load(type, filename, mapper, null);
        }

    protected static <T extends BaseSettingsFile> T load(Class<T> type, String filename, ObjectMapper mapper, File folder)
        {
        if (mapper == null)
            mapper = DEFAULT_MAPPER;

        File file;
        if (folder == null)
            file = Settings.getPreferenceFileLocation(filename);
        else
            file = new File(folder, filename);

        if (file.exists())
            {
            try (FileInputStream instream = new FileInputStream(file))
                {
                T settings = mapper.readValue(instream, type);
                settings.setFilename(file.getAbsolutePath());
                settings.readComplete();
                return settings;
                }
            catch (Exception e)
                {
                LOG.error("Unable to load settings from " + file.getPath(), e);
                }
            }
        else
            try
                {
                T settings = type.getDeclaredConstructor().newInstance();
                settings.setFilename(filename);
                settings.save();
                return settings;
                }
            catch (Exception e)
                {
                LOG.error(String.format("Can't instantiate %s. Is it missing a no-args constructor?", type.getSimpleName()));
                }

        return null;
        }

    protected void setFilename(String filename)
        {
        _filename = filename;
        }

    protected String getFilename()
        {
        return _filename;
        }

    private void setSubfolderName(String subfolder)
        {
        _subfolder = subfolder;
        }

    protected String getSubfolderName()
        {
        return null;
        }

    protected ObjectMapper getMapper()
        {
        return DEFAULT_MAPPER;
        }

    protected void readComplete() {}

    @Override
    public void close() throws IOException
        {
        if (!shouldSave())
            return;
        save();
        }

    protected void save()
        {
        File file = new File(getFilename());
        try
            {
            FileOutputStream outstream = new FileOutputStream(file);
            getMapper().writeValue(outstream, this);
            outstream.close();
            }
        catch (Exception e)
            {
            LOG.error(String.format("Unable to save %s settings to %s", getClass().getSimpleName(), file.getPath()), e);
            }
        }

    protected boolean shouldSave()
        {
        return true;
        }

    private String _filename;
    private String _subfolder;

    private static ObjectMapper DEFAULT_MAPPER = new ObjectMapper();
    static
        {
        DEFAULT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        }

    final static Logger LOG = LoggerFactory.getLogger(BaseSettingsFile.class);
    }


