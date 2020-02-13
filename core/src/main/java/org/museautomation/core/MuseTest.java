package org.museautomation.core;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * A Test is executed to return a result (pass or fail).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTest extends MuseResource
    {
    boolean execute(TestExecutionContext context);

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

