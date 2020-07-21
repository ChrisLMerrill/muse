package org.museautomation.builtins.plugins.resultstorage;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LocalStorageLocationPlugin extends GenericConfigurableTaskPlugin implements LocalStorageLocationProvider
    {
    LocalStorageLocationPlugin(LocalStorageLocationPluginConfiguration configuration)
        {
        super(configuration);
        }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseInstantiationException, ValueSourceResolutionError
        {
        if (_initialized)
            return;

        _context = context;
        _initialized = true;
        MuseValueSource output_folder_source = BaseValueSource.getValueSource(_configuration.parameters(), LocalStorageLocationPluginConfiguration.BASE_LOCATION_PARAM_NAME, true, context.getProject());
        String output_folder_path = BaseValueSource.getValue(output_folder_source, context, false, String.class);
        _output_folder = new File(output_folder_path);
        if (context instanceof TaskExecutionContext)
            _is_task_context = true;
        if (!_output_folder.exists())
            if (!_output_folder.mkdirs())
                {
                context.raiseEvent(LocalStorageLocationEventType.create(_output_folder.getAbsolutePath(), "Unable to create output folder. Results will not be stored."));
                _output_folder = null;
                }
        if (_output_folder != null)
            context.raiseEvent(LocalStorageLocationEventType.create(_output_folder.getAbsolutePath(), null));
        }

    @Override
    protected boolean applyToContextType(MuseExecutionContext context)
        {
        if (Plugins.findType(this.getClass(), context) != null)
            return false;

        return context instanceof TaskSuiteExecutionContext || context instanceof TaskExecutionContext;
        }

    @Override
    public File getBaseFolder()
        {
        return _output_folder;
        }

    @Override
    synchronized public File getTaskFolder(TaskExecutionContext task_context)
        {
        final File folder;
        if (_is_task_context)
            folder = _output_folder;
        else
            folder = new File(_output_folder, task_context.getTaskExecutionId());
        if (!folder.exists())
            if (!folder.mkdirs())
                _context.raiseEvent(LocalStorageLocationEventType.create(folder.getAbsolutePath(), "Unable to create output folder. Results will not be stored."));
        return folder;
        }

    private MuseExecutionContext _context;
    private File _output_folder = null;
    private boolean _is_task_context;
    private boolean _initialized = false;
    }