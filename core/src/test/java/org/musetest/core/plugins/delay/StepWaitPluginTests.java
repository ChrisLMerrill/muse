package org.musetest.core.plugins.delay;

import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.mocks.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;

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
        SteppedTestExecutionContext context = new MockSteppedTestExecutionContext();
        plugin.initialize(context);

        long start = System.currentTimeMillis();
        context.raiseEvent(EndStepEventType.create(new StepConfiguration(ReturnStep.TYPE_ID), new MockStepExecutionContext(context), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE)));
        long end = System.currentTimeMillis();
        long delay = end - start;
        Assertions.assertTrue(delay > (expected_delay - 50) && delay < (expected_delay + 200));
        }

    }