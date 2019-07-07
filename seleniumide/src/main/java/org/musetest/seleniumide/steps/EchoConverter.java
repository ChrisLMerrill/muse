package org.musetest.seleniumide.steps;

import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class EchoConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(String base_url, String command, String param1, String param2)
        {
        if (command.equals(ECHO))
            {
            StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
            step.addSource(LogMessage.MESSAGE_PARAM, ValueConverters.get().convert(param1));
            return step;
            }
        return null;
        }

    public static final String ECHO = "echo";
    }


