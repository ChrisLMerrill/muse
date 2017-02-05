package org.musetest.core.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.variables.*;

import java.util.concurrent.atomic.*;

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
    protected StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        MockShuttable shuttable = new MockShuttable();
        context.setVariable(SHUTTABLE_VAR_NAME, shuttable, VariableScope.Execution);
        context.registerShuttable(shuttable);
        context.raiseEvent(new MessageEvent(EXECUTE_MESSAGE));
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    public final static String EXECUTE_MESSAGE = "StepWithShuttableResource has run.";
    public final static String SHUTTABLE_VAR_NAME = "shuttable";
    public final static String TYPE_ID = MockStepCreatesShuttable.class.getAnnotation(MuseTypeId.class).value();
    }


