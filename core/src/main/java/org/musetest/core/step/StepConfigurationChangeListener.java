package org.musetest.core.step;

import org.musetest.core.step.events.*;

/**
 * Listen for change events on StepConfigurations.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepConfigurationChangeListener
    {
    void changed(StepChangeEvent event);
    }

