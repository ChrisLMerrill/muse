package org.musetest.core.events;

import org.apache.commons.io.*;
import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.plugins.*;
import org.musetest.core.project.*;
import org.musetest.core.resultstorage.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventLogWriterTests
	{
	@Test
	public void writeEventsAsTheyAreReceived() throws MuseExecutionError, IOException
		{
		MuseExecutionContext context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
		EventLogWriter writer = new EventLogWriter();
		context.addPlugin(writer);
		context.initializePlugins();

		File log = File.createTempFile("muse", "log.txt");
		writer.setLogFile(log);

		// nothing will be written until the start event is received
		context.raiseEvent(StartTestEventType.create("test1", "Test #1"));
		context.raiseEvent(MessageEventType.create("message1"));

		Assert.assertTrue(log.exists());
		Assert.assertTrue(log.length() > 0);
		Assert.assertTrue(new String(FileUtils.readFileToByteArray(log)).contains("message1"));

		final long length = log.length();
		context.raiseEvent(MessageEventType.create("message2"));
		Assert.assertTrue(log.length() > length);
		Assert.assertTrue(new String(FileUtils.readFileToByteArray(log)).contains("message2"));
		}

	@Test
	public void writeToDefaultLocation() throws MuseExecutionError
		{
		MuseExecutionContext context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
		EventLogWriter writer = new EventLogWriter();
		context.addPlugin(writer);
		context.initializePlugins();

		// nothing will be written until the start event is received
		context.raiseEvent(StartTestEventType.create("test1", "Test #1"));

		File log = writer.getLogfile();
		Assert.assertTrue(log.exists());
		Assert.assertTrue(log.length() > 0);
		Assert.assertEquals(log.getPath(), new File("events.json").getPath());
		}

	@Test
	public void writeToConfiguredLocalStorageLocation() throws MuseExecutionError
		{
		MuseExecutionContext context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
		EventLogWriter writer = new EventLogWriter();
		context.addPlugin(writer);
		context.addPlugin(new StoragePlugin());
		context.initializePlugins();

		// nothing will be written until the start event is received
		context.raiseEvent(StartTestEventType.create("test1", "Test #1"));

		File log = writer.getLogfile();
		Assert.assertTrue(log.exists());
		Assert.assertTrue(log.length() > 0);
		Assert.assertEquals(log.getPath(), new File(new File(System.getProperty("java.io.tmpdir")), "events.json").getPath());
		}

	@Test
	public void cacheEventsUntilTestStart() throws MuseExecutionError
		{
		MuseExecutionContext context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
		EventLogWriter writer = new EventLogWriter();
		context.addPlugin(writer);
		context.addPlugin(new StoragePlugin());
		context.initializePlugins();

		context.raiseEvent(MessageEventType.create("message1"));
		File log = writer.getLogfile();
		Assert.assertNull(log);

		// cached events are written when the start event is received
		context.raiseEvent(StartTestEventType.create("test1", "Test #1"));
		log = writer.getLogfile();
		Assert.assertTrue(log.exists());
		}

	class StoragePlugin implements LocalStorageLocationProvider, MusePlugin
		{
		@Override
		public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
			{
			return true;
			}

		@Override
		public void initialize(MuseExecutionContext context) {}

		@Override
		public void shutdown() {}

		@Override
		public String getId()
			{
			return null;
			}

		@Override
		public File getBaseFolder()
			{
			return null;
			}

		@Override
		public File getTestFolder(TestExecutionContext test_context)
			{
			return new File(System.getProperty("java.io.tmpdir"));
			}
		}
	}