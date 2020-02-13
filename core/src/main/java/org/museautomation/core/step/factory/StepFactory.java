package org.museautomation.core.step.factory;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;

/**
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepFactory
    {
    MuseStep createStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException;
    }

