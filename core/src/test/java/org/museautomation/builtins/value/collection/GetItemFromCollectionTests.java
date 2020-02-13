package org.museautomation.builtins.value.collection;

import org.jetbrains.annotations.*;
import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class GetItemFromCollectionTests
    {
    @Test
    void first() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = createSource(0);
        Assertions.assertEquals("abc", source.createSource(_project).resolveValue(new ProjectExecutionContext(_project)));
        }

    @Test
    void last() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = createSource(2);
        Assertions.assertEquals("xyz", source.createSource(_project).resolveValue(new ProjectExecutionContext(_project)));
        }

    @Test
    void negativeIndex() throws MuseInstantiationException
        {
        ValueSourceConfiguration source = createSource(-1);
        try
            {
            source.createSource(_project).resolveValue(new ProjectExecutionContext(_project));
            Assertions.fail();
            }
        catch (ValueSourceResolutionError e)
            {
            /* expected */
            }
        }

    @Test
    void indexOutOfBounds() throws MuseInstantiationException
        {
        ValueSourceConfiguration source = createSource(5000);
        try
            {
            source.createSource(_project).resolveValue(new ProjectExecutionContext(_project));
            Assertions.fail();
            }
        catch (ValueSourceResolutionError e)
            {
            /* expected */
            }
        }


    @Test
    void listSizeExceeded() throws MuseInstantiationException
        {
        ValueSourceConfiguration source = createSource(((long) Integer.MAX_VALUE) + 1L);
        try
            {
            source.createSource(_project).resolveValue(new ProjectExecutionContext(_project));
            Assertions.fail();
            }
        catch (ValueSourceResolutionError e)
            {
            /* expected */
            }
        }

    @NotNull
    private ValueSourceConfiguration createSource(long index)
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(GetItemFromCollection.TYPE_ID);
        source.addSource(GetItemFromCollection.SELECTOR_PARAM, ValueSourceConfiguration.forValue(index));
        ValueSourceConfiguration list = ValueSourceConfiguration.forType(ListSource.TYPE_ID);
        list.addSource(0, ValueSourceConfiguration.forValue("abc"));
        list.addSource(1, ValueSourceConfiguration.forValue("def"));
        list.addSource(2, ValueSourceConfiguration.forValue("xyz"));
        source.addSource(GetItemFromCollection.COLLECTION_PARAM, list);
        return source;
        }

    private MuseProject _project = new SimpleProject();
    }


