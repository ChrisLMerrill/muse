package org.museautomation.core.tests.mocks;

import org.museautomation.core.task.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockShuttable implements Shuttable
    {
    @Override
    public void shutdown()
        {
        _is_shutdown = true;
        }

    public boolean isShutdown()
        {
        return _is_shutdown;
        }

    private boolean _is_shutdown = false;
    }


