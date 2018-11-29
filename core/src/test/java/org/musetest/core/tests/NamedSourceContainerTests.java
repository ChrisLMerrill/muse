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
class NamedSourceContainerTests
	{
	@Test
    void addSource()
		{
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("v1");
		final String name = "s1";

		NamedSourcesContainer container = new NamedSourcesContainer();
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);
		container.addSource(name, source);

		Assertions.assertEquals(source, container.getSource(name));
		Assertions.assertTrue(ev_holder.get() instanceof NamedSourceAddedEvent);
		NamedSourceAddedEvent event = (NamedSourceAddedEvent) ev_holder.get();
		Assertions.assertEquals(name, event.getName());
		Assertions.assertEquals(source, event.getAddedSource());
		}

	@Test
    void setNullSource()
		{
		try
			{
			new NamedSourcesContainer().addSource("abc", null);
            Assertions.fail("Should have thrown an exception");
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
    void setDuplicateSource()
		{
		try
			{
			final NamedSourcesContainer container = new NamedSourcesContainer();
			container.addSource("abc", ValueSourceConfiguration.forValue("111"));
			container.addSource("abc", ValueSourceConfiguration.forValue("222"));
            Assertions.fail("Should have thrown an exception");
			}
		catch (IllegalArgumentException e)
			{
			// correct
			}
		}

	@Test
    void removeSource()
		{
		final NamedSourcesContainer container = new NamedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		final String name = "abc";
		container.addSource(name, source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		ValueSourceConfiguration removed = container.removeSource(name);

        Assertions.assertSame(removed, source);  // looking for reference equality here
		Assertions.assertNull(container.getSource(name));

		// check the event
		Assertions.assertTrue(ev_holder.get() instanceof NamedSourceRemovedEvent);
		NamedSourceRemovedEvent event = (NamedSourceRemovedEvent) ev_holder.get();
		Assertions.assertEquals(name, event.getName());
		Assertions.assertEquals(source, event.getRemovedSource());
		}

	@Test
    void renameSource()
		{
		final NamedSourcesContainer container = new NamedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		final String name = "abc";
		final String new_name = "newname";
		container.addSource(name, source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		boolean renamed = container.renameSource(name, new_name);

		Assertions.assertTrue(renamed);
		Assertions.assertNull(container.getSource(name));
        Assertions.assertSame(source, container.getSource(new_name));  // looking for reference equality

		// check the event
		Assertions.assertTrue(ev_holder.get() instanceof NamedSourceRenamedEvent);
		NamedSourceRenamedEvent event = (NamedSourceRenamedEvent) ev_holder.get();
		Assertions.assertEquals(name, event.getOldName());
		Assertions.assertEquals(new_name, event.getNewName());
		}

	@Test
    void replaceSource()
		{
		final NamedSourcesContainer container = new NamedSourcesContainer();
		final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("111");
		final ValueSourceConfiguration new_source = ValueSourceConfiguration.forValue("222");
		final String name = "abc";
		container.addSource(name, source);
		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);

		ValueSourceConfiguration replaced_source = container.replaceSource(name, new_source);

        Assertions.assertSame(source, replaced_source);  // looking for reference equality
        Assertions.assertSame(new_source, container.getSource(name));  // looking for reference equality

		// check the event
		Assertions.assertTrue(ev_holder.get() instanceof NamedSourceReplacedEvent);
		NamedSourceReplacedEvent event = (NamedSourceReplacedEvent) ev_holder.get();
		Assertions.assertEquals(name, event.getName());
		Assertions.assertEquals(source, event.getOldSource());
		Assertions.assertEquals(new_source, event.getNewSource());
		}

	@Test
    void getSourceNames()
		{
		final NamedSourcesContainer container = new NamedSourcesContainer();
		container.addSource("name1", ValueSourceConfiguration.forValue("111"));
		container.addSource("name2", ValueSourceConfiguration.forValue("222"));

		Set<String> names = container.getSourceNames();
		Assertions.assertEquals(2, names.size());
		Assertions.assertTrue(names.contains("name1"));
		Assertions.assertTrue(names.contains("name2"));
		}

	@Test
    void subsourceChangeEvent()
		{
		final NamedSourcesContainer container = new NamedSourcesContainer();
		final ValueSourceConfiguration source1 = ValueSourceConfiguration.forValue("111");
		container.addSource("name1", source1);
		container.addSource("name2", ValueSourceConfiguration.forValue("222"));

		AtomicReference<ChangeEvent> ev_holder = new AtomicReference<>(null);
		container.addChangeListener(ev_holder::set);
		source1.setValue("newval");

		// check the event
		Assertions.assertTrue(ev_holder.get() instanceof ValueChangeEvent);
		ValueChangeEvent event = (ValueChangeEvent) ev_holder.get();
		Assertions.assertEquals("111", event.getOldValue());
		Assertions.assertEquals("newval", event.getNewValue());
		}
	}
