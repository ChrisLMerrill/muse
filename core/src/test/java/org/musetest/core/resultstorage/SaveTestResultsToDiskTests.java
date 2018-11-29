package org.musetest.core.resultstorage;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.builtins.step.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
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
	public void saveResultsFromPluginsAtShutdown()
		{
		_context.addPlugin(new MockTestResultProducer(Collections.singletonList(new MockTestResultData("saved.txt", "result-data".getBytes()))));
		_context.addPlugin(new MockTestResultProducer(Collections.singletonList(new MockTestResultData("saved.txt", "result-data2".getBytes()))));

		_save_plugin.initialize(_context);
		_save_plugin.shutdown();

		final File saved = new File(_folder, "saved.txt");
		Assertions.assertTrue(saved.exists());
		Assertions.assertEquals("result-data", FileUtils.readFileAsString(saved));

		// test results with duplicate names do not overwrite each other
		final File saved2 = new File(_folder, "saved2.txt");
		Assertions.assertTrue(saved2.exists());
		Assertions.assertEquals("result-data2", FileUtils.readFileAsString(saved2));
	    }

	@Test
	public void saveResultsFromEvents()
	    {
	    _save_plugin.initialize(_context);

	    TestResultData result = new MockTestResultData("saved.txt", "result-data".getBytes());
	    _context.setVariable("result1", result, ContextVariableScope.Execution);
	    _context.raiseEvent(TestResultStoredEventType.create("result1", "the_result"));

	    // test a second event with same filename
	    TestResultData result2 = new MockTestResultData("saved.txt", "result-data2".getBytes());
	    _context.setVariable("result2", result2, ContextVariableScope.Execution);
	    _context.raiseEvent(TestResultStoredEventType.create("result2", "the_result"));

	    // test a third event with same filename
	    TestResultData result3 = new MockTestResultData("saved.txt", "result-data3".getBytes());
	    _context.setVariable("result3", result3, ContextVariableScope.Execution);
	    _context.raiseEvent(TestResultStoredEventType.create("result3", "the_result"));

	    final File saved = new File(_folder, "saved.txt");
        Assertions.assertTrue(saved.exists());
        Assertions.assertEquals("result-data", FileUtils.readFileAsString(saved));
        Assertions.assertNull(_context.getVariable("result1"));  // was the data removed from the context?

        // verify the second result is store (not overwriting 1st file)
	    final File saved2 = new File(_folder, "saved2.txt");
        Assertions.assertTrue(saved2.exists());
        Assertions.assertEquals("result-data2", FileUtils.readFileAsString(saved2));
	    Assertions.assertNull(_context.getVariable("result2"));

        // verify the second result is store (not overwriting 1st file)
	    final File saved3 = new File(_folder, "saved3.txt");
        Assertions.assertTrue(saved3.exists());
        Assertions.assertEquals("result-data3", FileUtils.readFileAsString(saved3));
	    Assertions.assertNull(_context.getVariable("result3"));
	    }

	@BeforeEach
	public void setup() throws IOException
		{
		_folder = FileUtils.createTempFolder("musetest", null);
		MockStorageLocationProvider location_plugin = new MockStorageLocationProvider(_folder, _folder);
		_context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
		_context.addPlugin(location_plugin);
		_save_plugin = new SaveTestResultsToDiskConfiguration().createPlugin();
		_context.addPlugin(_save_plugin);
		}

	private File _folder;
	private SteppedTestExecutionContext _context;
	private SaveTestResultsToDisk _save_plugin;
	}


