package org.musetest.selenium.providers;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseLocalDriverProvider implements WebDriverProvider
    {
    @SuppressWarnings("WeakerAccess")  // part of API
    public OperatingSystem getOs()
        {
        return _os;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setOs(OperatingSystem os)
        {
        OperatingSystem old_os = _os;
        _os = os;
        for (ChangeListener listener : _listeners)
            listener.osChanged(old_os, _os);
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public String getRelativePath()
        {
        return _relative_path;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setRelativePath(String new_path)
        {
        String old_path = _relative_path;
        if (Objects.equals(old_path, new_path))
            return;
        _relative_path = new_path;
        for (ChangeListener listener : _listeners)
            listener.relativePathChanged(old_path, _relative_path);
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public String getAbsolutePath()
        {
        return _absolute_path;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setAbsolutePath(String new_path)
        {
        String old_path = _absolute_path;
        if (Objects.equals(old_path, new_path))
            return;
        _absolute_path = new_path;
        for (ChangeListener listener : _listeners)
            listener.absolutePathChanged(old_path, _absolute_path);
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

    @SuppressWarnings("unused")  // public API
    public void addChangeListener(ChangeListener listener)
        {
        if (_listeners == null)
            _listeners = new HashSet<>();
        _listeners.add(listener);
        }

    @SuppressWarnings("unused")  // public API
    public void removeChangeListener(ChangeListener listener)
        {
        if (_listeners == null)
            return;
        _listeners.remove(listener);
        }

    private OperatingSystem _os;
    private String _relative_path;
    private String _absolute_path;

    private transient Set<ChangeListener> _listeners = new HashSet<>();

    private final static Logger LOG = LoggerFactory.getLogger(BaseLocalDriverProvider.class);

    @SuppressWarnings("WeakerAccess")  // public API
    public interface ChangeListener
        {
        void absolutePathChanged(String old_path, String new_path);
        void relativePathChanged(String old_path, String new_path);
        void osChanged(OperatingSystem old_os, OperatingSystem new_os);
        }
    }


