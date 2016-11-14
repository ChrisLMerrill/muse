package org.musetest.core;

import org.musetest.core.resource.types.*;
import org.musetest.core.suite.*;

import java.util.*;

/**
 * A test suite provides a list of MuseTests to be executed.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestSuite extends MuseResource
    {
    List<TestConfiguration> generateTestList(MuseProject project);

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    class TestSuiteResourceType extends ResourceType
        {
        public TestSuiteResourceType()
            {
            super("testsuite", "Macro", MuseTestSuite.class);
            }
        }
    }

