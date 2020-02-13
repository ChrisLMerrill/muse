package org.museautomation.core.step.factory;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CompoundStepFactory implements StepFactory
    {
    public CompoundStepFactory(StepFactory... factories)
        {
        Collections.addAll(_factories, factories);
        }

    @Override
    public MuseStep createStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        MuseInstantiationException thrown = null;
        for (StepFactory factory : _factories)
            {
            MuseStep step;
            try
                {
                step = factory.createStep(configuration, project);
                }
            catch (MuseInstantiationException e)
                {
                thrown = e;
                step = null;
                }
            if (step != null)
                return step;
            }

        if (thrown != null)
            throw thrown;

        throw new MuseInstantiationException("No StepFactory found for step type: " + configuration.getType());
        }

    private List<StepFactory> _factories = new ArrayList<>();
    }


