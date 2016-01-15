package org.musetest.core.values;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseValueSource implements MuseValueSource
    {
    public BaseValueSource(ValueSourceConfiguration config, MuseProject project)
        {
        _config = config;
        _project = project;
        }

    public ValueSourceConfiguration getConfig()
        {
        return _config;
        }

    public MuseProject getProject()
        {
        return _project;
        }

    @Override
    public String getDescription()
        {
        return _project.getValueSourceDescriptors().get(_config.getType()).getInstanceDescription(_config);
        }

    private final ValueSourceConfiguration _config;
    private final MuseProject _project;
    }


