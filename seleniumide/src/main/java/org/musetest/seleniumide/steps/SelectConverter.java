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
        if (command.equals(SELECT) || command.equals(ADD_SELECTION))
            {
            StepConfiguration step;
            if (param2.startsWith(BY_INDEX))
                {
                step = new StepConfiguration(SelectOptionByIndex.TYPE_ID);
                step.addSource(SelectOptionByIndex.INDEX_PARAM, ValueSourceConfiguration.forValue(param2.substring(BY_INDEX.length())));
                step.addSource(SelectOptionByIndex.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
                return step;
                }
            else if (param2.startsWith(BY_VALUE))
                {
                step = new StepConfiguration(SelectOptionByValue.TYPE_ID);
                step.addSource(SelectOptionByValue.VALUE_PARAM, ValueSourceConfiguration.forValue(param2.substring(BY_VALUE.length())));
                step.addSource(SelectOptionByValue.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
                return step;
                }
            else
                {
                String label = param2;
                if (param2.startsWith(BY_LABEL))
                    label = param2.substring(BY_LABEL.length());
                step = new StepConfiguration(SelectOptionByText.TYPE_ID);
                step.addSource(SelectOptionByText.TEXT_PARAM, ValueSourceConfiguration.forValue(label));
                step.addSource(SelectOptionByText.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
                return step;
                }
            }
        else if (command.equals(DESELECT))
            {
            StepConfiguration step;
            if (param2.startsWith(BY_INDEX))
                {
                step = new StepConfiguration(DeselectOptionByIndex.TYPE_ID);
                step.addSource(DeselectOptionByIndex.INDEX_PARAM, ValueSourceConfiguration.forValue(param2.substring(BY_INDEX.length())));
                step.addSource(DeselectOptionByIndex.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
                return step;
                }
            else if (param2.startsWith(BY_VALUE))
                {
                step = new StepConfiguration(DeselectOptionByValue.TYPE_ID);
                step.addSource(DeselectOptionByValue.VALUE_PARAM, ValueSourceConfiguration.forValue(param2.substring(BY_VALUE.length())));
                step.addSource(DeselectOptionByValue.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
                return step;
                }
            else
                {
                String label = param2;
                if (param2.startsWith(BY_LABEL))
                    label = param2.substring(BY_LABEL.length());
                step = new StepConfiguration(DeselectOptionByText.TYPE_ID);
                step.addSource(DeselectOptionByText.TEXT_PARAM, ValueSourceConfiguration.forValue(label));
                step.addSource(DeselectOptionByText.ELEMENT_PARAM, LocatorConverters.get().convert(param1));
                return step;
                }
            }
        return null;
        }

    public static final String SELECT = "select";
    public static final String ADD_SELECTION = "addSelection";
    public static final String DESELECT = "removeSelection";

    private static final String BY_LABEL = "label=";
    private static final String BY_INDEX = "index=";
    private static final String BY_VALUE = "value=";
    }


