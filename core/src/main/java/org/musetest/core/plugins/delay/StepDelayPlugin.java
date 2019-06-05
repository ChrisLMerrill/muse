package org.musetest.core.plugins.delay;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.values.*;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepDelayPlugin extends GenericConfigurableTestPlugin
    {
    public StepDelayPlugin(StepDelayPluginConfiguration configuration)
        {
        super(configuration);
        }

    @Override
    public void initialize(MuseExecutionContext context) throws MuseExecutionError
        {
        Long delay_param = BaseValueSource.getValue(BaseValueSource.getValueSource(_configuration.parameters(), StepDelayPluginConfiguration.DELAY_TIME, false, context.getProject()), context, true, Long.class);
        if (delay_param != null)
            _delay_ms = delay_param;

//        SteppedTestExecutionContext step_context = (SteppedTestExecutionContext) context;
        context.addEventListener(event ->
            {
            if (EndStepEventType.TYPE_ID.equals(event.getTypeId()))
                {
//                StepConfiguration step = step_context.getStepLocator().findStep(EndStepEventType.getStepId(event));
                try
                    {
                    Thread.sleep(_delay_ms);
                    }
                catch (InterruptedException e)
                    {
                    System.out.println("interrupted");
                    }
                }
            });
        }

    private long _delay_ms = StepDelayPluginConfiguration.DEFAULT_DELAY_TIME;
    }