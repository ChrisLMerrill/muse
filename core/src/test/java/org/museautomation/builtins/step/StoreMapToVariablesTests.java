package org.museautomation.builtins.step;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.context.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.values.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StoreMapToVariablesTests
    {
    @Test
    public void storeVars() throws MuseInstantiationException, ValueSourceResolutionError
        {
        StepConfiguration config = new StepConfiguration(StoreMapToVariablesStep.TYPE_ID);
        config.addSource(StoreMapToVariablesStep.MAP_PARAM, ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, "map"));
        SteppedTaskExecutionContext context = new MockSteppedTaskExecutionContext();
        Map<String, Object> map = new HashMap<>();
        map.put("a","bc");
        map.put("b",12);
        context.setVariable("map", map);
        StoreMapToVariablesStep step = new StoreMapToVariablesStep(config, context.getProject());
        step.executeImplementation(new MockStepExecutionContext(context));
        assertEquals("bc", context.getVariable("a"));
        assertEquals(12, context.getVariable("b"));
        }
    }