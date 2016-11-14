package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MissingTest extends BaseMuseResource implements MuseTest
    {
    public MissingTest(String id)
        {
        _id = id;
        }

    @Override
    public MuseTestResult execute(TestExecutionContext context)
        {
        return new BaseMuseTestResult(this, new EventLog(), new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, "test is missing"));
        }

    @Override
    public String getDescription()
        {
        return "MissingTest (id=" + _id + ")";
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

    @Override
    public ResourceType getType()
        {
        return new MuseTest.TestResourceType();
        }

    private String _id;
    }


