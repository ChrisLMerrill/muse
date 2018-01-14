package org.musetest.core.test.plugins;

import org.musetest.core.test.plugin.*;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TypeChangeEvent extends ChangeEvent
    {
    public TypeChangeEvent(TestPluginConfiguration config, String old_type, String new_type)
        {
        super(config);
        _old_type = old_type;
        _new_type = new_type;
        }

    public TestPluginConfiguration getConfig()
	    {
	    return (TestPluginConfiguration) _target;
	    }

    @SuppressWarnings("WeakerAccess")
    public String getOldType()
        {
        return _old_type;
        }

    @SuppressWarnings("WeakerAccess")
    public String getNewType()
        {
        return _new_type;
        }

    private final String _old_type;
    private final String _new_type;
    }


