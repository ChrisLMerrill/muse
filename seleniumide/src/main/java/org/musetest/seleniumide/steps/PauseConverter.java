package org.musetest.seleniumide.steps;

import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class PauseConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(String base_url, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.equals(PAUSE))
            {
            StepConfiguration step = new StepConfiguration(WaitTimed.TYPE_ID);
            step.addSource(WaitTimed.DURATION_PARAM, ValueSourceConfiguration.forValue(Integer.parseInt(param1)));
            return step;
            }
        return null;
        }

    private static final String PAUSE = "pause";
    }


