package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.events.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IndexedSourceContainerTests
	{
	@Test
	public void addAndGetSource()
		{
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("v1");

		IndexedSourcesContainer container = new IndexedSourcesContainer();
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);
		container.addSource(source);

		Assert.assertEquals(source, container.getSource(0));
		Assert.assertTrue(ev_holder.get() instanceof IndexedSourceAddedEvent);
		IndexedSourceAddedEvent event = (IndexedSourceAddedEvent) ev_holder.get();
		Assert.assertEquals(0, event.getIndex());
		Assert.assertEquals(source, event.getSource());
		}

	@Test
	public void getNonexistentSource()
		{
		Assert.assertNull(new IndexedSourcesContainer().getSource(5));
		}

	@Test
	public void addNullSource()
		{
		try
			{
			new IndexedSourcesContainer().addSource(null);
			Assert.assertTrue("Should have thrown an exception", false);
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
	public void insertNullSource()
		{
		try
			{
			new IndexedSourcesContainer().addSource(3, null);
			Assert.assertTrue("Should have thrown an exception", false);
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
	public void replaceWithNullSource()
		{
		try
			{
			new IndexedSourcesContainer().replaceSource(3, null);
			Assert.assertTrue("Should have thrown an exception", false);
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
	public void replaceNonExistentSource()
		{
		try
			{
			new IndexedSourcesContainer().replaceSource(5, ValueSourceConfiguration.forValue("na"));
			Assert.assertTrue("Should have thrown an exception", false);
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
	public void insertSourceAtStart()
	    {
	    setupListOf3();
	    _container.addSource(0, ValueSourceConfiguration.forValue("new"));

	    Assert.assertEquals(4, _container.getSourceList().size());
	    Assert.assertEquals("new", _container.getSourceList().get(0).getValue().toString());
	    Assert.assertEquals(1L, _container.getSourceList().get(1).getValue());
	    Assert.assertEquals(2L, _container.getSourceList().get(2).getValue());
	    Assert.assertEquals(3L, _container.getSourceList().get(3).getValue());
	    }
	
	@Test
	public void insertSourceAtEnd()
	    {
	    setupListOf3();
	    _container.addSource(3, ValueSourceConfiguration.forValue("new"));

	    Assert.assertEquals(4, _container.getSourceList().size());
	    Assert.assertEquals(1L, _container.getSourceList().get(0).getValue());
	    Assert.assertEquals(2L, _container.getSourceList().get(1).getValue());
	    Assert.assertEquals(3L, _container.getSourceList().get(2).getValue());
	    Assert.assertEquals("new", _container.getSourceList().get(3).getValue().toString());
	    }
	
	@Test
	public void insertSourceInMiddle()
	    {
	    setupListOf3();
	    _container.addSource(1, ValueSourceConfiguration.forValue("new"));

	    Assert.assertEquals(4, _container.getSourceList().size());
	    Assert.assertEquals(1L, _container.getSourceList().get(0).getValue());
	    Assert.assertEquals("new", _container.getSourceList().get(1).getValue().toString());
	    Assert.assertEquals(2L, _container.getSourceList().get(2).getValue());
	    Assert.assertEquals(3L, _container.getSourceList().get(3).getValue());
	    }
	
	@Test
	public void removeSource()
		{
		final IndexedSourcesContainer container = new IndexedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		container.addSource(source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		ValueSourceConfiguration removed = container.removeSource(0);

		Assert.assertTrue(removed == source);  // looking for reference equality here
		Assert.assertNull(container.getSource(0));

		// check the event
		Assert.assertTrue(ev_holder.get() instanceof IndexedSourceRemovedEvent);
		IndexedSourceRemovedEvent event = (IndexedSourceRemovedEvent) ev_holder.get();
		Assert.assertEquals(0, event.getIndex());
		Assert.assertEquals(source, event.getSource());
		}

	@Test
	public void removeNonexistentSource()
		{
		Assert.assertNull(new IndexedSourcesContainer().removeSource(0));
		}

	@Test
	public void replaceSource()
		{
		final IndexedSourcesContainer container = new IndexedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		final ValueSourceConfiguration new_source = ValueSourceConfiguration.forValue("222");
		container.addSource(source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		ValueSourceConfiguration replaced_source = container.replaceSource(0, new_source);

		Assert.assertTrue(source == replaced_source);  // looking for reference equality
		Assert.assertTrue(new_source == container.getSource(0));  // looking for reference equality

		// check the event
		Assert.assertTrue(ev_holder.get() instanceof IndexedSourceReplacedEvent);
		IndexedSourceReplacedEvent event = (IndexedSourceReplacedEvent) ev_holder.get();
		Assert.assertEquals(0, event.getIndex());
		Assert.assertEquals(source, event.getSource());
		Assert.assertEquals(new_source, event.getNewSource());
		}

	@Test
	public void getSources()
	    {
	    final IndexedSourcesContainer container = new IndexedSourcesContainer();
	    container.addSource(ValueSourceConfiguration.forValue("111"));
	    container.addSource(ValueSourceConfiguration.forValue("222"));

	    List<ValueSourceConfiguration> sources = container.getSourceList();
	    Assert.assertEquals(2, sources.size());
	    Assert.assertEquals("111", sources.get(0).getValue().toString());
	    Assert.assertEquals("222", sources.get(1).getValue().toString());
	    }

	@Test
	public void changeSubsource()
		{
		setupListOf3();
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		_container.addChangeListener(ev_holder::set);
		_source1.setValue("newval");

		// check the event
		Assert.assertTrue(ev_holder.get() instanceof ValueChangeEvent);
		ValueChangeEvent event = (ValueChangeEvent) ev_holder.get();
		Assert.assertEquals(1L, event.getOldValue());
		Assert.assertEquals("newval", event.getNewValue());
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
