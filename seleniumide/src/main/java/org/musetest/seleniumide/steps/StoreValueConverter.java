package org.musetest.seleniumide.steps;

import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // invoked via reflection from StepConverters
public class StoreValueConverter implements StepConverter
    {
    @Override
    public StepConfiguration convertStep(TestConverter converter, String command, String param1, String param2) throws UnsupportedError
        {
        if (!STORE.equals(command))
            return null;
        StepConfiguration step = new StepConfiguration(StoreVariable.TYPE_ID);
        step.addSource(StoreVariable.NAME_PARAM, ValueConverters.get().convert(param1));
        step.addSource(StoreVariable.VALUE_PARAM, ValueConverters.get().convert(param2));
        return step;
        }

    private final static String STORE = "store";
    }


