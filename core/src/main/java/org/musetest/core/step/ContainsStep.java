package org.musetest.core.step;

import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ContainsStep extends MuseResource
    {
    StepConfiguration getStep();

    static StepConfiguration createStarterStep()
        {
        StepConfiguration step = new StepConfiguration();
        step.setType(ScopedGroup.TYPE_ID);
        StepConfiguration step1 = new StepConfiguration();
        step1.setType(LogMessage.TYPE_ID);
        step1.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("replace this with some useful steps"));
        step.addChild(step1);
        return step;
        }
    }

