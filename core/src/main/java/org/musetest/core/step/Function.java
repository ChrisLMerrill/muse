package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.resource.*;


/**
 * A step composed of other steps to be called from a step in a test.
 *
 * @see CallFunction
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("function")
public class Function implements MuseResource, ContainsStep
    {
    @SuppressWarnings("unused")   // required for Jackson serialization
    public Function()
        {
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

    private StepConfiguration _step;
    private ResourceMetadata _metadata;
    }


