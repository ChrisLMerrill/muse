package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseMuseTest extends BaseMuseResource implements MuseTest
    {
    @Override
    public boolean execute(TestExecutionContext context)
        {
        try
            {
            return executeImplementation(context);
//            return true;
            }
        catch (Throwable e)
            {
            MuseEvent event = TestErrorEventType.create("An exception was thrown: " + e.getMessage());
            context.raiseEvent(event);
            return false;
            }
        }

    @Override
    public String getDescription()
        {
        return getId();
        }

    protected abstract boolean executeImplementation(TestExecutionContext context);

    @Override
    public ResourceType getType()
        {
        return new MuseTest.TestResourceType();
        }
    }


