package org.musetest.seleniumide.steps;

import org.musetest.core.step.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class SubmitConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(String base_url, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.equals(SUBMIT))
            {
            StepConfiguration step = new StepConfiguration(SubmitElement.TYPE_ID);
            step.addSource(SubmitElement.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
            return step;
            }
        return null;
        }

    public static final String SUBMIT = "submit";
    }


