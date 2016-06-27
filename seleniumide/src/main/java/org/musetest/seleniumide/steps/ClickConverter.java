package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class ClickConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(TestConverter converter, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.equals(CLICK) || command.equals(CLICK_AND_WAIT))
            {
            StepConfiguration step = new StepConfiguration(ClickElement.TYPE_ID);
            step.setSource(SendKeys.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
            return step;
            }
        return null;
        }

    @Override
    public String[] getCommands()
        {
        return new String[] { CLICK, CLICK_AND_WAIT };
        }     // not calling this, anyway

    public static final String CLICK = "click";
    public static final String CLICK_AND_WAIT = "clickAndWait";
    }


