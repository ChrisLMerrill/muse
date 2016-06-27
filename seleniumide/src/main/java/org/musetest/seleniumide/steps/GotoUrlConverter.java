package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class GotoUrlConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(TestConverter converter, String command, String param1, String param2)
        {
        if (command.equals(OPEN))
            {
            String path = param1;
            if (path.startsWith("/"))
                path = path.substring(1);
            StepConfiguration step = new StepConfiguration(GotoUrl.TYPE_ID);
            step.setSource(GotoUrl.URL_PARAM, ValueSourceConfiguration.forValue(converter.getBaseUrl() + path));
            return step;
            }
        return null;
        }

    @Override
    public String[] getCommands()
        {
        return new String[] { OPEN };
        }

    private static final String OPEN = "open";
    }


