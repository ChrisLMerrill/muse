package org.museautomation.core.context;

import org.museautomation.core.steptask.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SteppedTaskExecutionContext extends TaskExecutionContext, StepsExecutionContext
    {
    @Override
    SteppedTask getTask();
    }

