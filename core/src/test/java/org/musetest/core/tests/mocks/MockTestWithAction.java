package org.musetest.core.tests.mocks;

import org.musetest.core.test.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * For mocking a test that performs some action.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class MockTestWithAction extends BaseMuseTest
    {
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


