package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class SendKeysConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(TestConverter converter, String command, String param1, String param2) throws UnsupportedError
        {
        if (!TYPE.equals(command) && !SENDKEYS.equals(command))
            return null;
        StepConfiguration step = new StepConfiguration(SendKeys.TYPE_ID);
        step.addSource(SendKeys.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
        step.addSource(SendKeys.KEYS_PARAM, ValueSourceConfiguration.forValue(param2));
        if (TYPE.equals(command))
            step.addSource(SendKeys.CLEAR_PARAM, ValueSourceConfiguration.forValue(true));
        return step;
        }

    private final static String TYPE = "type";
    private final static String SENDKEYS = "sendKeys";
    }


