package org.musetest.seleniumide.steps;

import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.conditions.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class VerifyOrAssert implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(TestConverter test_converter, String command, String param1, String param2) throws UnsupportedError
        {
        if (command.startsWith(VERIFY) || command.startsWith(ASSERT))
            {
            boolean is_assert = command.startsWith(ASSERT);
            String condition_id;
            if (is_assert)
                condition_id = command.substring(ASSERT.length());
            else
                condition_id = command.substring(VERIFY.length());

            ConditionConverter condition_converter = ConditionConverters.getInstance().find(condition_id);
            if (condition_converter == null)
                return null;

            StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
            step.addSource(Verify.CONDITION_PARAM, condition_converter.createConditionSource(param1, param2));
            if (is_assert)
                step.addSource(Verify.TERMINATE_PARAM, ValueSourceConfiguration.forValue(true));
            return step;
            }
        return null;
        }

    private static final String VERIFY = "verify";
    private static final String ASSERT = "assert";
    }


