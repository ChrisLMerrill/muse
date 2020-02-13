package org.museautomation.core;

import org.museautomation.core.resource.types.*;
import org.museautomation.core.test.*;

import java.util.*;

/**
 * A test suite provides a list of MuseTests to be executed.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestSuite extends MuseResource
    {
    Iterator<TestConfiguration> getTests(MuseProject project);

    /**
     * Returns null if the test suite cannot calculate its total (e.g. it could execute infinitely).
     */
    Integer getTotalTestCount(MuseProject project);

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    class TestSuiteResourceType extends ResourceType
        {
        public TestSuiteResourceType()
            {
            super("testsuite", "Test Suite", MuseTestSuite.class);
            }
        }
    }