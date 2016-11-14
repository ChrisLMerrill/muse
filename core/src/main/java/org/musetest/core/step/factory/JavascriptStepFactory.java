package org.musetest.core.step.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.javascript.factory.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptStepFactory implements StepFactory
    {
    @Override
    public MuseStep createStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        if (project == null)
            return null;

        // look for javascript-steps in the project
        JavascriptStepResource builder = project.getResourceStorage().getResource(configuration.getType(), JavascriptStepResource.class);
        if (builder == null)
            return null;

        return builder.createStep(configuration);
        }
    }


