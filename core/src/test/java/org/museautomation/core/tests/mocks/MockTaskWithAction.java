package org.museautomation.core.tests.mocks;

import org.museautomation.core.task.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * For mocking a test that performs some action.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class MockTaskWithAction extends BaseMuseTask
    {
    protected MockTaskWithAction()
	    {
	    setId("mock-task-with-action");
	    }

    @Override
    public Map<String, ValueSourceConfiguration> getDefaultVariables()
        {
        return null;
        }

    @Override
    public void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables)
        {

        }

    @Override
    public void setDefaultVariable(String name, ValueSourceConfiguration source)
        {

        }
    }


