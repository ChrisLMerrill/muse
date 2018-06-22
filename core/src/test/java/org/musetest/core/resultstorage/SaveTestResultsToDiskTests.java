package org.musetest.core.resultstorage;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.util.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SaveTestResultsToDiskTests
	{
	@Test
	public void storeFiles() throws IOException
		{
		final File folder = FileUtils.createTempFolder("musetest", null);
		MockStorageLocationProvider location_plugin = new MockStorageLocationProvider(folder, folder);
		SteppedTestExecutionContext context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
		context.addPlugin(location_plugin);

		SaveTestResultsToDisk save_plugin = new SaveTestResultsToDiskConfiguration().createPlugin();
		context.addPlugin(save_plugin);

		context.addPlugin(new MockTestResultProducer(Collections.singletonList(new MockTestResultData("saved.txt", "result-data".getBytes()))));
		context.addPlugin(new MockTestResultProducer(Collections.singletonList(new MockTestResultData("saved.txt", "result-data2".getBytes()))));

		save_plugin.initialize(context);
		save_plugin.shutdown();

		final File saved = new File(folder, "saved.txt");
		Assert.assertTrue(saved.exists());
		Assert.assertEquals("result-data", FileUtils.readFileAsString(saved));

		final File saved2 = new File(folder, "saved2.txt");
		Assert.assertTrue(saved2.exists());
		Assert.assertEquals("result-data2", FileUtils.readFileAsString(saved2));
	    }
	}


