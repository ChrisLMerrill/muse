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

    @SuppressWarnings("unused")  // public API
    public boolean addTestId(String id)
        {
        boolean added = _test_ids.add(id);
        for (ChangeListener listener : _listeners)
            listener.testIdAdded(id);
        return added;
        }

    @SuppressWarnings("unused")  // public API
    public boolean removeTestId(String id)
        {
        boolean added = _test_ids.remove(id);
        for (ChangeListener listener : _listeners)
            listener.testIdRemoved(id);
        return added;
        }

    @Override
    public ResourceType getType()
        {
        return new TestSuiteResourceType();
        }

    public void addChangeListener(ChangeListener listener)
        {
        _listeners.add(listener);
        }

    public boolean removeChangeListener(ChangeListener listener)
        {
        return _listeners.remove(listener);
        }

    @SuppressWarnings("unused")  // public API
    public interface ChangeListener
        {
        void testIdAdded(String id);
        void testIdRemoved(String id);
        }

    @SuppressWarnings("unused,WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class IdListTestSuiteSubtype extends ResourceSubtype
        {
        public IdListTestSuiteSubtype()
            {
            super(TYPE_ID, "List of tests", IdListTestSuite.class, new MuseTestSuite.TestSuiteResourceType());
            }
        }

    private List<String> _test_ids = new ArrayList<>();

    private transient Set<ChangeListener> _listeners = new HashSet<>();

    public final static String TYPE_ID = IdListTestSuite.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(IdListTestSuite.class);
    }


