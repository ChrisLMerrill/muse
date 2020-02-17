package org.museautomation.core.execution;

import org.museautomation.core.*;
import org.museautomation.core.steptask.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SteppedTaskProviderImpl implements SteppedTaskProvider
    {
    public SteppedTaskProviderImpl(MuseProject project, SteppedTask task)
        {
        _project = project;
        _task = task;
        }

    @Override
    public MuseProject getProject()
        {
        return _project;
        }

    @Override
    public SteppedTask getTask()
        {
        return _task;
        }

    private MuseProject _project;
    private SteppedTask _task;
    }


