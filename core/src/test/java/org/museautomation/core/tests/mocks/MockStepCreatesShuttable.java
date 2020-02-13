package org.museautomation.core.tests.mocks;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("MockStepCreatesShuttable")
public class MockStepCreatesShuttable extends BaseStep
    {
    @SuppressWarnings("unused")
    public MockStepCreatesShuttable(StepConfiguration configuration, MuseProject project)
        {
        super(configuration);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context)
        {
        MockShuttable shuttable = new MockShuttable();
        context.setVariable(SHUTTABLE_VAR_NAME, shuttable, ContextVariableScope.Execution);
        context.registerShuttable(shuttable);
        context.raiseEvent(MessageEventType.create(EXECUTE_MESSAGE));
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String EXECUTE_MESSAGE = "StepWithShuttableResource has run.";
    public final static String SHUTTABLE_VAR_NAME = "shuttable";
    public final static String TYPE_ID = MockStepCreatesShuttable.class.getAnnotation(MuseTypeId.class).value();
    }