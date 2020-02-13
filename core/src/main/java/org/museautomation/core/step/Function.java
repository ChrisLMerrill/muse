package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;

import java.util.*;


/**
 * A step composed of other steps to be called from a step in a test.
 *
 * @see CallFunction
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("function")
public class Function extends BaseMuseResource implements MuseResource, ContainsStep
    {
    @SuppressWarnings("unused")   // required for Jackson serialization
    public Function()
        {
        }

    public StepConfiguration getStep()
        {
        return _step;
        }

    public void setStep(StepConfiguration step)
        {
        _step = step;
        }

    @Override
    public boolean equals(Object obj)
        {
        return (obj instanceof Function && Objects.equals(_step, ((Function)obj)._step));
        }

    @Override
    public ResourceType getType()
        {
        return new FunctionResourceType();
        }

    private StepConfiguration _step;

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class FunctionResourceType extends ResourceType
        {
        public FunctionResourceType()
            {
            super(Function.class.getAnnotation(MuseTypeId.class).value(), "Function", Function.class);
            }
        @Override
        public MuseResource create()
            {
            org.museautomation.core.step.Function function = new org.museautomation.core.step.Function();
            function.setStep(ContainsStep.createStarterStep());
            return function;
            }
        }
    }


