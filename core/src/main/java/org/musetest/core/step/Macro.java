package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

import java.util.*;


/**
 * A step composed of other steps to be called from a step in a test.  The steps are executed in the
 * same variable scope as the parent, which means that it can see the same variables.
 *
 * @see CallMacroStep
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("macro")
public class Macro extends BaseMuseResource implements ContainsStep
    {
    @SuppressWarnings("unused")   // required for Jackson serialization
    public Macro()
        {
        }

    @Override
    public ResourceType getType()
        {
        return new MacroResourceType();
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
        return (obj instanceof Macro && Objects.equals(_step, ((Macro)obj)._step));
        }

    private StepConfiguration _step;

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class MacroResourceType extends ResourceType
        {
        public MacroResourceType()
            {
            super(Macro.class.getAnnotation(MuseTypeId.class).value(), "Macro", Macro.class);
            }

        @Override
        public MuseResource create()
            {
            Macro macro = new Macro();
            macro.setStep(ContainsStep.createStarterStep());
            return macro;
            }
        }
    }


