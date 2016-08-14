package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.SteppedTest;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestTests
    {
    @Test
    public void eventGeneration()
        {
        SteppedTest test = setup();
        MuseTestResult result = test.execute(new DefaultTestExecutionContext());

        Assert.assertTrue(result.isPass());
        Assert.assertEquals(1, result.getLog().findEvents(MuseEventType.StartTest).size());
        Assert.assertEquals(1, result.getLog().findEvents(MuseEventType.EndTest).size());
        }

    @Test
    public void initializationFailure()
        {
        SteppedTest test = setup();
        test.setDefaultVariable("default1", ValueSourceConfiguration.forType(ProjectResourceValueSource.TYPE_ID));
        MuseTestResult result = test.execute(new DefaultTestExecutionContext());

        Assert.assertFalse(result.isPass());
        }

    @Test
    public void stepSetupFailure()
        {
        SteppedTest test = setup();
        test.getStep().setSource(LogMessage.MESSAGE_PARAM, null);
        MuseTestResult result = test.execute(new DefaultTestExecutionContext());

        Assert.assertFalse(result.isPass());
        }

    private SteppedTest setup()
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        config.setSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("abc"));
        return new SteppedTest(config);
        }
    }


