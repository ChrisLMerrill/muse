package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
abstract class BaseLocalDriverProvider implements WebDriverProvider
    {
    OperatingSystem getOs()
        {
        return _os;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setOs(OperatingSystem os)
        {
        _os = os;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public String getRelativePath()
        {
        return _relative_path;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setRelativePath(String relative_path)
        {
        _relative_path = relative_path;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public String getAbsolutePath()
        {
        return _absolute_path;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setAbsolutePath(String absolute_path)
        {
        _absolute_path = absolute_path;
        }

    File getDriverLocation(MuseExecutionContext context)
        {
        if (_relative_path != null)
            {
            ResourceStorage storage = context.getProject().getResourceStorage();
            if (storage instanceof FilesystemStorage)
                {
                if (_absolute_path != null)
                    LOG.warn(String.format("Both absolute (%s) and relative (%s) paths are configured. Using relative", _absolute_path, _relative_path));
                return new File(((FilesystemStorage)storage).getBaseLocation(), _relative_path);
                }
            else
                {
                if (_absolute_path == null)
                    {
                    LOG.warn("Project-relative path configured, but project is not on local filesystem.");
                    return null;
                    }
                else
                    LOG.warn("Project-relative path configured, but project is not on local filesystem. Using absolute path.");
                }
            }
        return new File(_absolute_path);
        }

    private OperatingSystem _os;
    private String _relative_path;
    private String _absolute_path;

    private final static Logger LOG = LoggerFactory.getLogger(BaseLocalDriverProvider.class);
    }


