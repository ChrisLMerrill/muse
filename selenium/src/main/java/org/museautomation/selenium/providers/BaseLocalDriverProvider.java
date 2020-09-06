package org.museautomation.selenium.providers;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.builtins.value.collection.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.storage.*;
import org.museautomation.core.util.*;
import org.museautomation.core.values.*;
import org.museautomation.selenium.*;
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

	@SuppressWarnings("unused") // required for Json de/serialization
	public void setArguments(String[] arguments)
		{
		// upgrade old versions to use the arguments value source
        if (arguments != null && arguments.length > 0)
            {
            if (arguments.length == 1)
                _argument_source = ValueSourceConfiguration.forValue(arguments[0]);
            else
                {
                List<ValueSourceConfiguration> list = new ArrayList<>();
                for (String argument : arguments)
                    list.add(ValueSourceConfiguration.forValue(argument));
                ValueSourceConfiguration source = ValueSourceConfiguration.forType(ListSource.TYPE_ID);
                source.setSourceList(list);
                _argument_source = source;
                }
            }
        else
            _argument_source = ValueSourceConfiguration.forValue(null);
		}

    @Deprecated // replaced by arguement source
	@SuppressWarnings("unused,WeakerAccess") // required for Json de/serialization
    @JsonInclude(JsonInclude.Include.NON_NULL)
	public String[] getArguments()
		{
		return null;
		}

    @SuppressWarnings("unused,WeakerAccess") // required for Json de/serialization
    public ValueSourceConfiguration getArgumentSource()
        {
        return _argument_source;
        }

    @SuppressWarnings("unused,WeakerAccess") // required for Json de/serialization
    public void setArgumentSource(ValueSourceConfiguration argument_source)
        {
        _argument_source = argument_source;
        }

    public String[] resolveArguments(MuseExecutionContext context)
        {
        try
            {
            MuseValueSource source = _argument_source.createSource(context.getProject());
            Object value = source.resolveValue(context);
            if (value == null)
                return new String[0];
            else if (value instanceof List)
                {
                List<Object> list = (List<Object>) value;
                String[] args = new String[list.size()];
                for (int i = 0; i <list.size(); i++)
                    args[i] = list.get(i).toString();
                return args;
                }
            else
                return new String[] {value.toString()};
            }
        catch (Exception e)
            {
            context.raiseEvent(MessageEventType.create("Unable to resolve arguments from driver provider configuration: " + e.getMessage()));
            return new String[0];
            }
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
				return new File(((FilesystemStorage) storage).getBaseLocation(), _relative_path);
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
	private ValueSourceConfiguration _argument_source = ValueSourceConfiguration.forValue(null);

	private transient Set<ChangeListener> _listeners = new HashSet<>();

	private final static Logger LOG = LoggerFactory.getLogger(BaseLocalDriverProvider.class);

	@SuppressWarnings("WeakerAccess")  // public API
	public interface ChangeListener
		{
		void absolutePathChanged(String old_path, String new_path);
		void relativePathChanged(String old_path, String new_path);
		void osChanged(OperatingSystem old_os, OperatingSystem new_os);
		@SuppressWarnings("unused") // used in UI
		void argumentSourceChanged(ValueSourceConfiguration old_args, ValueSourceConfiguration new_args);
		}
	}
