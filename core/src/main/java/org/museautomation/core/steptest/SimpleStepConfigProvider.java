package org.museautomation.core.steptest;

import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleStepConfigProvider implements StepConfigProvider
    {
    public SimpleStepConfigProvider(StepConfiguration config)
        {
        _config = config;
        }

    @Override
    public StepConfiguration popCurrentStep()
        {
        StepConfiguration config = _config;
        _config = null;
        return config;
        }

    @Override
    public StepConfiguration queryCurrentStep()
        {
        return _config;
        }

    private StepConfiguration _config;
    }


