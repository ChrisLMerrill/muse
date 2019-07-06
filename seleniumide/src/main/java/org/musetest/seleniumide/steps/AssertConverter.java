package org.musetest.seleniumide.steps;

import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.selenium.steps.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class AssertConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(String base_url, String command, String param1, String param2)
        {
        if (command.equals(ASSERT_TITLE))
            {
            ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
            condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forType(PageTitleValueSource.TYPE_ID));
            condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue(param1));

            StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
            step.addSource(Verify.CONDITION_PARAM, condition);
            return step;
            }
        return null;
        }

    private static final String ASSERT_TITLE = "assertTitle";
    }


