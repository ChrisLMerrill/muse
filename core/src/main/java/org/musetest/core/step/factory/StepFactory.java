package org.musetest.core.step.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.util.*;

/**
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepFactory
    {
    MuseStep createStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException;
    }

