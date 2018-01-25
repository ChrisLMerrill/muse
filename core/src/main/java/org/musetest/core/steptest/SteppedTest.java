package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.musetest.core.values.*;

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
    protected boolean executeImplementation(TestExecutionContext context)
        {
        if (!(context instanceof SteppedTestExecutionContext))
            context = new DefaultSteppedTestExecutionContext(context);
        SteppedTestExecutor executor = new SteppedTestExecutor(this, (SteppedTestExecutionContext)context);
        context.addEventListener(new TerminateOnError(executor));
        return executor.executeAll();
        }

    @Override
    public String getDescription()
        {
        Object description = _step.getMetadataField(StepConfiguration.META_DESCRIPTION);
        if (description != null)
            return description.toString();
        else
            return super.getDescription();
        }

    public StepConfiguration getStep()
        {
        return _step;
        }

    public void setStep(StepConfiguration step)
        {
        _step = step;
        }

    @SuppressWarnings("WeakerAccess")  // used by the UI
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
        return (obj instanceof SteppedTest && Objects.equals(_step, ((SteppedTest)obj)._step));
        }

    @Override
    public String toString()
        {
        return getId();
        }

    private StepConfiguration _step;
    private Map<String, ValueSourceConfiguration> _default_variables;
    }


