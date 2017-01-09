package org.musetest.seleniumide.steps;

import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.locators.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class VerifyElementPresent implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(TestConverter converter, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.equals(VERIFY) || command.equals(ASSERT))
            {
            StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
            ValueSourceConfiguration element_present = ValueSourceConfiguration.forTypeWithSource(ElementExistsCondition.TYPE_ID, LocatorConverters.get().convert(param1));
            step.addSource(Verify.CONDITION_PARAM, element_present);
            if (command.equals(ASSERT))
                step.addSource(Verify.TERMINATE_PARAM, ValueSourceConfiguration.forValue(true));
            return step;
            }
        return null;
        }

    @Override
    public String[] getCommands()
        {
        return new String[] { VERIFY, ASSERT};
        }     // not calling this, anyway

    private static final String VERIFY = "verifyElementPresent";
    private static final String ASSERT = "assertElementPresent";
    }


