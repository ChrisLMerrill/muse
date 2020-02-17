package org.museautomation.builtins.plugins.delay;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.step.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class StepWaitPluginTests
    {
    @Test
    void testDefaultDelay() throws MuseExecutionError
        {
        testDelay(new StepWaitPluginConfiguration(), 1000);
        }

    @Test
    void testCustomDelay() throws MuseExecutionError
        {
        StepWaitPluginConfiguration config = new StepWaitPluginConfiguration();
        config.parameters().addSource(StepWaitPluginConfiguration.DELAY_TIME, ValueSourceConfiguration.forValue(400));
        testDelay(config, 400);
        }

    private void testDelay(StepWaitPluginConfiguration config, long expected_delay) throws MuseExecutionError
        {
        StepWaitPlugin plugin = new StepWaitPlugin(config);
        SteppedTaskExecutionContext context = new MockSteppedTaskExecutionContext();
        plugin.initialize(context);

        long start = System.currentTimeMillis();
        context.raiseEvent(EndStepEventType.create(new StepConfiguration(ReturnStep.TYPE_ID), new MockStepExecutionContext(context), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE)));
        long end = System.currentTimeMillis();
        long delay = end - start;
        Assertions.assertTrue(delay > (expected_delay - 50) && delay < (expected_delay + 200));
        }

    }