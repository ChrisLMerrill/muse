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
public class NamedSourceContainerTests
	{
	@Test
	public void addSource()
		{
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("v1");
		final String name = "s1";

		NamedSourcesContainer container = new NamedSourcesContainer();
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);
		container.addSource(name, source);

		Assert.assertEquals(source, container.getSource(name));
		Assert.assertTrue(ev_holder.get() instanceof NamedSourceAddedEvent);
		NamedSourceAddedEvent event = (NamedSourceAddedEvent) ev_holder.get();
		Assert.assertEquals(name, event.getName());
		Assert.assertEquals(source, event.getAddedSource());
		}

	@Test
	public void setNullSource()
		{
		try
			{
			new NamedSourcesContainer().addSource("abc", null);
			Assert.assertTrue("Should have thrown an exception", false);
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
	public void setDuplicateSource()
		{
		try
			{
			final NamedSourcesContainer container = new NamedSourcesContainer();
			container.addSource("abc", ValueSourceConfiguration.forValue("111"));
			container.addSource("abc", ValueSourceConfiguration.forValue("222"));
			Assert.assertTrue("Should have thrown an exception", false);
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
	public void removeSource()
		{
		final NamedSourcesContainer container = new NamedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		final String name = "abc";
		container.addSource(name, source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		ValueSourceConfiguration removed = container.removeSource(name);

		Assert.assertTrue(removed == source);  // looking for reference equality here
		Assert.assertNull(container.getSource(name));

		// check the event
		Assert.assertTrue(ev_holder.get() instanceof NamedSourceRemovedEvent);
		NamedSourceRemovedEvent event = (NamedSourceRemovedEvent) ev_holder.get();
		Assert.assertEquals(name, event.getName());
		Assert.assertEquals(source, event.getRemovedSource());
		}

	@Test
	public void renameSource()
		{
		final NamedSourcesContainer container = new NamedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		final String name = "abc";
		final String new_name = "newname";
		container.addSource(name, source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		boolean renamed = container.renameSource(name, new_name);

		Assert.assertTrue(renamed);
		Assert.assertNull(container.getSource(name));
		Assert.assertTrue(source == container.getSource(new_name));  // looking for reference equality

		// check the event
		Assert.assertTrue(ev_holder.get() instanceof NamedSourceRenamedEvent);
		NamedSourceRenamedEvent event = (NamedSourceRenamedEvent) ev_holder.get();
		Assert.assertEquals(name, event.getOldName());
		Assert.assertEquals(new_name, event.getNewName());
		}

	@Test
	public void replaceSource()
		{
		final NamedSourcesContainer container = new NamedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		final ValueSourceConfiguration new_source = ValueSourceConfiguration.forValue("222");
		final String name = "abc";
		container.addSource(name, source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		ValueSourceConfiguration replaced_source = container.replaceSource(name, new_source);

		Assert.assertTrue(source == replaced_source);  // looking for reference equality
		Assert.assertTrue(new_source == container.getSource(name));  // looking for reference equality

		// check the event
		Assert.assertTrue(ev_holder.get() instanceof NamedSourceReplacedEvent);
		NamedSourceReplacedEvent event = (NamedSourceReplacedEvent) ev_holder.get();
		Assert.assertEquals(name, event.getName());
		Assert.assertEquals(source, event.getOldSource());
		Assert.assertEquals(new_source, event.getNewSource());
		}

	@Test
	public void getSourceNames()
	    {
	    final NamedSourcesContainer container = new NamedSourcesContainer();
	    container.addSource("name1", ValueSourceConfiguration.forValue("111"));
	    container.addSource("name2", ValueSourceConfiguration.forValue("222"));

	    Set<String> names = container.getSourceNames();
	    Assert.assertEquals(2, names.size());
	    Assert.assertTrue(names.contains("name1"));
	    Assert.assertTrue(names.contains("name2"));
	    }
	}


