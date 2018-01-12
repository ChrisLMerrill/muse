package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import java.util.*;

/**
 * A MuseTestSuite that creates tests based on a list of test IDs and/or test suite ids.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("suite_of_ids")
public class IdListTestSuite extends BaseMuseResource implements MuseTestSuite
    {
    @Override
    public Iterator<TestConfiguration> getTests(MuseProject project)
	    {
	    List<TestConfiguration> direct_tests = new ArrayList<>(_test_ids.size());
	    List<Iterator<TestConfiguration>> suite_iterators = new ArrayList<>();
	    for (String id : _test_ids)
		    {
		    MuseResource resource = project.getResourceStorage().getResource(id);
		    if (resource instanceof MuseTest)
			    direct_tests.add(new BasicTestConfiguration(id));
		    else if (resource instanceof MuseTestSuite)
			    suite_iterators.add(((MuseTestSuite) resource).getTests(project));
		    else
			    {
			    LOG.error("Test with id {} was not found in the project", id);
			    direct_tests.add(new BasicTestConfiguration(id));
			    }
		    }

	    CompoundTestConfigurationIterator all = new CompoundTestConfigurationIterator();
	    all.add(direct_tests.iterator());
	    for (Iterator<TestConfiguration> iterator : suite_iterators)
	    	all.add(iterator);

	    return all;
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

    @SuppressWarnings({"unused", "UnusedReturnValue"})  // public API
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
        return new IdListTestSuiteSubtype();
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
            super(TYPE_ID, "List of tests suite", IdListTestSuite.class, new MuseTestSuite.TestSuiteResourceType());
            }
        }

    private List<String> _test_ids = new ArrayList<>();

    private transient Set<ChangeListener> _listeners = new HashSet<>();

    public final static String TYPE_ID = IdListTestSuite.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(IdListTestSuite.class);
    }