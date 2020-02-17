package org.museautomation.core.steptask;

import org.museautomation.core.step.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LinearListStepConfigurationProvider implements StepConfigProvider
    {
    public LinearListStepConfigurationProvider(List<StepConfiguration> steps)
        {
        _steps = steps;
        }

    @Override
    public StepConfiguration popCurrentStep()
        {
        StepConfiguration config = queryCurrentStep();
        _index++;
        return config;
        }

    @Override
    public StepConfiguration queryCurrentStep()
        {
        if (_steps == null || _index >= _steps.size())
            return null;
        return _steps.get(_index);
        }

    private List<StepConfiguration> _steps;
    private int _index = 0;
    }


