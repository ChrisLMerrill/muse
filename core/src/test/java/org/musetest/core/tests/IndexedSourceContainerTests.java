package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.events.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class IndexedSourceContainerTests
	{
	@Test
    void addAndGetSource()
		{
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("v1");

		IndexedSourcesContainer container = new IndexedSourcesContainer();
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);
		container.addSource(source);

		Assertions.assertEquals(source, container.getSource(0));
		Assertions.assertTrue(ev_holder.get() instanceof IndexedSourceAddedEvent);
		IndexedSourceAddedEvent event = (IndexedSourceAddedEvent) ev_holder.get();
		Assertions.assertEquals(0, event.getIndex());
		Assertions.assertEquals(source, event.getSource());
		}

	@Test
    void getNonexistentSource()
		{
		Assertions.assertNull(new IndexedSourcesContainer().getSource(5));
		}

	@Test
    void addNullSource()
		{
		try
			{
			new IndexedSourcesContainer().addSource(null);
            Assertions.fail("Should have thrown an exception");
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
    void insertNullSource()
		{
		try
			{
			new IndexedSourcesContainer().addSource(3, null);
            Assertions.fail("Should have thrown an exception");
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
    void replaceWithNullSource()
		{
		try
			{
			new IndexedSourcesContainer().replaceSource(3, null);
            Assertions.fail("Should have thrown an exception");
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
    void replaceNonExistentSource()
		{
		try
			{
			new IndexedSourcesContainer().replaceSource(5, ValueSourceConfiguration.forValue("na"));
            Assertions.fail("Should have thrown an exception");
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
    void insertSourceAtStart()
	    {
	    setupListOf3();
	    _container.addSource(0, ValueSourceConfiguration.forValue("new"));

	    Assertions.assertEquals(4, _container.getSourceList().size());
	    Assertions.assertEquals("new", _container.getSourceList().get(0).getValue().toString());
	    Assertions.assertEquals(1L, _container.getSourceList().get(1).getValue());
	    Assertions.assertEquals(2L, _container.getSourceList().get(2).getValue());
	    Assertions.assertEquals(3L, _container.getSourceList().get(3).getValue());
	    }
	
	@Test
    void insertSourceAtEnd()
	    {
	    setupListOf3();
	    _container.addSource(3, ValueSourceConfiguration.forValue("new"));

	    Assertions.assertEquals(4, _container.getSourceList().size());
	    Assertions.assertEquals(1L, _container.getSourceList().get(0).getValue());
	    Assertions.assertEquals(2L, _container.getSourceList().get(1).getValue());
	    Assertions.assertEquals(3L, _container.getSourceList().get(2).getValue());
	    Assertions.assertEquals("new", _container.getSourceList().get(3).getValue().toString());
	    }
	
	@Test
    void insertSourceInMiddle()
	    {
	    setupListOf3();
	    _container.addSource(1, ValueSourceConfiguration.forValue("new"));

	    Assertions.assertEquals(4, _container.getSourceList().size());
	    Assertions.assertEquals(1L, _container.getSourceList().get(0).getValue());
	    Assertions.assertEquals("new", _container.getSourceList().get(1).getValue().toString());
	    Assertions.assertEquals(2L, _container.getSourceList().get(2).getValue());
	    Assertions.assertEquals(3L, _container.getSourceList().get(3).getValue());
	    }
	
	@Test
    void removeSource()
		{
		final IndexedSourcesContainer container = new IndexedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		container.addSource(source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		ValueSourceConfiguration removed = container.removeSource(0);

        Assertions.assertSame(removed, source);  // looking for reference equality here
		Assertions.assertNull(container.getSource(0));

		// check the event
		Assertions.assertTrue(ev_holder.get() instanceof IndexedSourceRemovedEvent);
		IndexedSourceRemovedEvent event = (IndexedSourceRemovedEvent) ev_holder.get();
		Assertions.assertEquals(0, event.getIndex());
		Assertions.assertEquals(source, event.getSource());
		}

	@Test
    void removeNonexistentSource()
		{
		Assertions.assertNull(new IndexedSourcesContainer().removeSource(0));
		}

	@Test
    void replaceSource()
		{
		final IndexedSourcesContainer container = new IndexedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		final ValueSourceConfiguration new_source = ValueSourceConfiguration.forValue("222");
		container.addSource(source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		ValueSourceConfiguration replaced_source = container.replaceSource(0, new_source);

        Assertions.assertSame(source, replaced_source);  // looking for reference equality
        Assertions.assertSame(new_source, container.getSource(0));  // looking for reference equality

		// check the event
		Assertions.assertTrue(ev_holder.get() instanceof IndexedSourceReplacedEvent);
		IndexedSourceReplacedEvent event = (IndexedSourceReplacedEvent) ev_holder.get();
		Assertions.assertEquals(0, event.getIndex());
		Assertions.assertEquals(source, event.getSource());
		Assertions.assertEquals(new_source, event.getNewSource());
		}

	@Test
    void getSources()
	    {
	    final IndexedSourcesContainer container = new IndexedSourcesContainer();
	    container.addSource(ValueSourceConfiguration.forValue("111"));
	    container.addSource(ValueSourceConfiguration.forValue("222"));

	    List<ValueSourceConfiguration> sources = container.getSourceList();
	    Assertions.assertEquals(2, sources.size());
	    Assertions.assertEquals("111", sources.get(0).getValue().toString());
	    Assertions.assertEquals("222", sources.get(1).getValue().toString());
	    }

	@Test
    void changeSubsource()
		{
		setupListOf3();
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		_container.addChangeListener(ev_holder::set);
		_source1.setValue("newval");

		// check the event
		Assertions.assertTrue(ev_holder.get() instanceof ValueChangeEvent);
		ValueChangeEvent event = (ValueChangeEvent) ev_holder.get();
		Assertions.assertEquals(1L, event.getOldValue());
		Assertions.assertEquals("newval", event.getNewValue());
		}

	private void setupListOf3()
		{
		_container.addSource(_source1);
		_container.addSource(_source2);
		_container.addSource(_source3);
		}

	private IndexedSourcesContainer _container = new IndexedSourcesContainer();
	private ValueSourceConfiguration _source1 = ValueSourceConfiguration.forValue(1);
	private ValueSourceConfiguration _source2 = ValueSourceConfiguration.forValue(2);
	private ValueSourceConfiguration _source3 = ValueSourceConfiguration.forValue(3);
	}
