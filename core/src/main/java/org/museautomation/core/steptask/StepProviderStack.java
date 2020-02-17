package org.museautomation.core.steptask;

import org.museautomation.core.step.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepProviderStack implements StepConfigProvider
    {
    @Override
    public StepConfiguration popCurrentStep()
        {
        StepConfigProvider provider = _provider_stack.peek();
        if (provider == null)
            return null;
        return provider.popCurrentStep();
        }

    @Override
    public StepConfiguration queryCurrentStep()
        {
        while (!_provider_stack.isEmpty())
            {
            StepConfigProvider provider = _provider_stack.peek();
            StepConfiguration step = provider.queryCurrentStep();
            if (step != null)
                return step;

            _provider_stack.pop();
            }
        return null;
        }

    public void pushProvider(StepConfigProvider provider)
        {
        _provider_stack.push(provider);
        }

    private Stack<StepConfigProvider> _provider_stack = new Stack<>();
    }


