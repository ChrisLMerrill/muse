package org.museautomation.core.steptask;

import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepConfigProvider
    {
    StepConfiguration popCurrentStep();
    StepConfiguration queryCurrentStep();
    }


