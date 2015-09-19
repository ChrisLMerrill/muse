package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;

/**
 * A compound step that creates a new variable scope for it's children (and descendents). Variables declared in
 * higher-level scopes are not visible in this scope (except for test-wide variables).  Likewise, variables decloared
 * in this scope are not visible to lower-level scopes. If a descendant of this step is a ScopedGroup, varaibles in
 * this scope will not be visible to children of that group (except under the following condition).  Thus it is much
 * like local variables of a method in Java, where a call to another method starts a new scope (which ends when the
 * method returns).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("scope")
@MuseStepName("Group (scoped)")
@MuseInlineEditString("group (scoped)")
@MuseStepIcon("glyph:FontAwesome:CLONE")
@MuseStepTypeGroup("Structure")
@MuseStepShortDescription("Group of steps (run in a new variable scope)")
@MuseStepLongDescription("Group of steps that are executed in a new variable scope. This is not generally expected to be used directly by the tester, but rather as a basis for other compound steps that need a new variable scope (such as a function call).")
@SuppressWarnings("unused")  // instantiated via reflection
public class ScopedGroup extends BasicCompoundStep
    {
    public ScopedGroup(StepConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    protected void start(StepExecutionContext context) throws StepConfigurationError
        {
        if (isCreateNewVariableScope())
            context.getTestExecutionContext().pushNewVariableScope();
        }

    @Override
    protected void finish(StepExecutionContext context) throws StepConfigurationError
        {
        if (isCreateNewVariableScope())
            context.getTestExecutionContext().popVariableScope();
        }

    protected boolean isCreateNewVariableScope()
        {
        return true;
        }
    }


