package org.museautomation.core.test;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;

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
    public boolean execute(TestExecutionContext context)
        {
        final MuseEvent event = StartTestEventType.create(getId(), getDescription());
        event.addTag(MuseEvent.ERROR);
        context.raiseEvent(event);
        return false;
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


