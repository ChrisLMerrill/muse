package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.musetest.core.values.*;
import org.musetest.core.values.factory.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("steppedtest")
public class SteppedTest extends BaseMuseTest implements ContainsStep
    {
    @SuppressWarnings("unused")   // required for Jackson serialization
    public SteppedTest()
        {
        }

    public SteppedTest(StepConfiguration step)
        {
        _step = step;
        }

    @Override
    protected MuseTestResult executeImplementation(TestExecutionContext context)
        {
        if (!(context instanceof SteppedTestExecutionContext))
            context = new DefaultSteppedTestExecutionContext(context);
        SteppedTestExecutor executor = new SteppedTestExecutor(this, (SteppedTestExecutionContext)context);
        return executor.executeAll();
        }

    public StepConfiguration getStep()
        {
        return _step;
        }

    public void setStep(StepConfiguration step)
        {
        _step = step;
        }

    public Map<String, ValueSourceConfiguration> getDefaultVariables()
        {
        return _default_variables;
        }

    public void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables)
        {
        _default_variables = default_variables;
        }

    public void setDefaultVariable(String name, ValueSourceConfiguration source)
        {
        if (_default_variables == null)
            _default_variables = new HashMap<>();
        _default_variables.put(name, source);
        }

    @Override
    public boolean equals(Object obj)
        {
        return (obj instanceof SteppedTest && _step.equals(((SteppedTest)obj)._step));
        }

    public boolean initializeContext(SteppedTestExecutionContext context)
        {
        MuseProject project = context.getProject();
        if (_default_variables != null)
            for (String name : _default_variables.keySet())
                if (context.getVariable(name) == null)
                    try
                        {
                        MuseValueSource source = ValueSourceFactory.getDefault(project).createSource(getDefaultVariables().get(name), project);
                        Object value = source.resolveValue(new SingleStepExecutionContext(context, null, false));
                        context.setVariable(name, value);
                        }
                    catch (StepExecutionError e)
                        {
                        context.raiseEvent(new TestErrorEvent("Unable to set initial test variables due to: " + e.getMessage()));
                        return false;
                        }
        return true;
        }

    private StepConfiguration _step;
    private Map<String, ValueSourceConfiguration> _default_variables;
    }


