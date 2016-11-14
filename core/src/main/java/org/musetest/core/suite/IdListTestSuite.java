package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import java.util.*;

/**
 * A MuseTestSuite that creates tests based on a list of test IDs.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("suite_of_ids")
public class IdListTestSuite extends BaseMuseResource implements MuseTestSuite
    {
    @Override
    public List<TestConfiguration> generateTestList(MuseProject project)
        {
        List<TestConfiguration> tests = new ArrayList<>(_test_ids.size());
        for (String id : _test_ids)
            {
            MuseTest test = project.getResourceStorage().getResource(id, MuseTest.class);
            if (test == null)
                {
                LOG.error("Test with id {} was not found in the project", id);
                tests.add(new TestConfiguration(new MissingTest(id)));
                }
            else
                tests.add(new TestConfiguration(test));
            }
        return tests;
        }

    public List<String> getTestIds()
        {
        return Collections.unmodifiableList(_test_ids);
        }

    @SuppressWarnings("unused")  // used by Jackson
    public void setTestIds(List<String> ids)
        {
        _test_ids = ids;
        }

    @Override
    public ResourceType getType()
        {
        return new TestSuiteResourceType();
        }

    private List<String> _test_ids = new ArrayList<>();

    public final static String TYPE_ID = IdListTestSuite.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(IdListTestSuite.class);
    }


