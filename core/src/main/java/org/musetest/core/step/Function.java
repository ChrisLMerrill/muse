package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;


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
//        getMetadata().setType(ResourceTypes.Function);
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        if (_metadata == null)
            _metadata = new ResourceMetadata();
        return _metadata;
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
        return (obj instanceof Function && _step.equals(((Function)obj)._step));
        }

    @Override
    public ResourceType getType()
        {
        return new FunctionResourceType();
        }

    private StepConfiguration _step;
    private ResourceMetadata _metadata;

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
            org.musetest.core.step.Function function = new org.musetest.core.step.Function();
            function.setStep(ContainsStep.createStarterStep());
            return function;
            }
        }
    }


