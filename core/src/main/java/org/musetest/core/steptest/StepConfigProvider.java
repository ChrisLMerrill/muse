package org.musetest.core.steptest;

import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepConfigProvider
    {
    StepConfiguration popCurrentStep();
    StepConfiguration queryCurrentStep();
    }


