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
public class SelectConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(String base_url, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.equals(SELECT))
            {
            StepConfiguration step;
            if (param2.startsWith(BY_LABEL))
                {
                step = new StepConfiguration(SelectOptionByText.TYPE_ID);
                step.addSource(SelectOptionByText.TEXT_PARAM, ValueSourceConfiguration.forValue(param2.substring(BY_LABEL.length())));
                step.addSource(SelectOptionByText.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
                return step;
                }
            else if (param2.startsWith(BY_INDEX))
                {
                step = new StepConfiguration(SelectOptionByIndex.TYPE_ID);
                step.addSource(SelectOptionByIndex.INDEX_PARAM, ValueSourceConfiguration.forValue(param2.substring(BY_INDEX.length())));
                step.addSource(SelectOptionByIndex.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
                return step;
                }
            }
        return null;
        }

    private static final String SELECT = "select";

    private static final String BY_LABEL = "label=";
    private static final String BY_INDEX = "index=";
    }


