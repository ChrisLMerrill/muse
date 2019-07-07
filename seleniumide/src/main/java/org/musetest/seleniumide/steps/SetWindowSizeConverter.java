package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class SetWindowSizeConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(String base_url, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.equals(COMMAND))
            {
            String[] dimensions = param1.split("x");
            if (dimensions.length != 2)
                throw new UnsupportedError(String.format("The %s command parameter must be of the format '111x222'.", COMMAND));
            Long x;
            Long y;
            try
                {
                x = Long.parseLong(dimensions[0]);
                y = Long.parseLong(dimensions[1]);
                }
            catch (NumberFormatException e)
                {
                throw new UnsupportedError(String.format("The %s command parameter must be of the format '111x222'.", COMMAND));
                }
            StepConfiguration step = new StepConfiguration(SetBrowserSize.TYPE_ID);
            step.addSource(SetBrowserSize.WIDTH_PARAM, ValueSourceConfiguration.forValue(x));
            step.addSource(SetBrowserSize.HEIGHT_PARAM, ValueSourceConfiguration.forValue(y));
            return step;
            }
        return null;
        }

    public static final String COMMAND = "setWindowSize";
    }


