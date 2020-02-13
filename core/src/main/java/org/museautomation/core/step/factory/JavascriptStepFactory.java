package org.museautomation.core.step.factory;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.javascript.factory.*;

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


