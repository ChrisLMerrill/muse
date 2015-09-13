package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.resource.*;


/**
 * A step composed of other steps to be called from a step in a test.  The steps are executed in the
 * same variable scope as the parent, which means that it can see the same variables.
 *
 * @see CallMacroStep
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("macro")
public class Macro implements MuseResource, ContainsStep
    {
    @SuppressWarnings("unused")   // required for Jackson serialization
    public Macro()
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
        return (obj instanceof Macro && _step.equals(((Macro)obj)._step));
        }

    private StepConfiguration _step;
    private ResourceMetadata _metadata;
    }


