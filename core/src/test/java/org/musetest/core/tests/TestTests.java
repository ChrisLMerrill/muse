package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestTests
    {
    @Test
    public void testEventGeneration()
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        config.setSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("abc"));
        SteppedTest test = new SteppedTest(config);
        MuseTestResult result = test.execute(new DefaultTestExecutionContext());

        Assert.assertTrue(result.isPass());
        Assert.assertEquals(1, result.getLog().findEvents(MuseEventType.StartTest).size());
        Assert.assertEquals(1, result.getLog().findEvents(MuseEventType.EndTest).size());
        }
    }


