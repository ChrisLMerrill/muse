package org.musetest.core.step.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.util.*;

import java.lang.reflect.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ClasspathStepFactory implements StepFactory
    {
    public ClasspathStepFactory(TypeLocator types)
        {
        _types = types;
        }

    @Override
    @SuppressWarnings("unchecked")
    public MuseStep createStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        String type = configuration.getType();
        Class step_class = _types.getClassForTypeId(type);
        if (step_class == null)
            return null;

        try
            {
            Constructor constructor = step_class.getConstructor(StepConfiguration.class, MuseProject.class);
            try
                {
                Object instance = constructor.newInstance(configuration, project);
                if (!(instance instanceof MuseStep))
                    throw new MuseInstantiationException(String.format("Step type '%s' (class %s) must implement MuseStep.", type, step_class.getSimpleName()));
                return (MuseStep) instance;
                }
            catch (Exception e)
                {
                Throwable t = e;
                if (e instanceof InvocationTargetException)
                    t = ((InvocationTargetException)e).getTargetException();
                throw new MuseInstantiationException(String.format("Failed to instantiate step type '%s'. Does the configuration provide all required parameters? Exception is: %s", type, t.getMessage()));
                }
            }
        catch (NoSuchMethodException e)
            {
            throw new MuseInstantiationException(String.format("Step type '%s' (class %s) does not have a constructor with the required parameters (StepConfiguration config, MuseProject project)", type, step_class.getSimpleName()));
            }
        }

    private TypeLocator _types;
    }


