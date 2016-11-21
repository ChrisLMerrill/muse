package org.musetest.core;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * A Test is executed to return a result (pass or fail).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTest extends MuseResource
    {
    MuseTestResult execute(TestExecutionContext context);

    @JsonIgnore
    String getDescription();

    Map<String, ValueSourceConfiguration> getDefaultVariables();
    void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables);
    void setDefaultVariable(String name, ValueSourceConfiguration source);

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    class TestResourceType extends ResourceType
        {
        public TestResourceType()
            {
            super("test", "Test", MuseTest.class);
            }

        @Override
        public MuseResource create()
            {
            SteppedTest test = new SteppedTest();
            test.setStep(ContainsStep.createStarterStep());
            return test;
            }
        }
    }

