package org.musetest.selenium.plugins;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
import org.musetest.core.resultstorage.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.musetest.selenium.mocks.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class WebdriverCapturePluginTests
	{
	@Test
	public void testCaptureOnFailure() throws IOException
		{
	    setupPlugin(true, false, true, true, true);
	    final MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.FAILURE);
	    _context.raiseEvent(event);
		checkForHtmlCaptureEvent(1);
		checkForScreenshotCaptureEvent();
	    }

	private void checkForScreenshotCaptureEvent() throws IOException
		{
		boolean screenshot_found = false;
		for (MuseEvent event : _storage_events)
			if (event.getAttributeAsString(TestResultStoredEventType.RESULT_DESCRIPTION).toLowerCase().contains("screenshot"))
				{
				TestResultData data = (TestResultData) _context.getVariable(event.getAttributeAsString(TestResultStoredEventType.VARIABLE_NAME));
				Assert.assertNotNull(data);
				Assert.assertTrue(Arrays.equals(SCREENSHOT_BYTES, getBytes(data)));
				screenshot_found = true;
				}

		Assert.assertTrue("screenshot not captured", screenshot_found);
		}

	private void checkForHtmlCaptureEvent(int count) throws IOException
		{
		int html_found = 0;
		for (MuseEvent event : _storage_events)
			if (event.getAttribute(TestResultStoredEventType.RESULT_DESCRIPTION).toString().toLowerCase().contains("html"))
				{
				TestResultData data = (TestResultData) _context.getVariable(event.getAttributeAsString(TestResultStoredEventType.VARIABLE_NAME));
				Assert.assertNotNull(data);
				Assert.assertTrue(Arrays.equals(HTML_BYTES, getBytes(data)));
				html_found++;
				}

		Assert.assertTrue("HTML not captured", html_found == count);
		}

	private byte[] getBytes(TestResultData data) throws IOException
		{
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        data.write(outstream);
		return outstream.toByteArray();
		}

	@Test
	public void testCaptureOnError() throws IOException
		{
	    setupPlugin(false, true, true, true, true);
	    final MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.ERROR);
	    _context.raiseEvent(event);
		checkForHtmlCaptureEvent(1);
		checkForScreenshotCaptureEvent();
	    }

	@Test
	public void testCaptureTwice() throws IOException
	    {
	    setupPlugin(true, true, false, true, false);
	    MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.ERROR);
	    _context.raiseEvent(event);

	    event = MessageEventType.create("message");
	    event.addTag(MuseEvent.FAILURE);
	    _context.raiseEvent(event);

	    checkForHtmlCaptureEvent(2);
	    }

	@Test
	public void captureOnlyScreenshot()  throws IOException
	    {
	    setupPlugin(true, true, true, false, false);
	    MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.ERROR);
	    _context.raiseEvent(event);

        checkForScreenshotCaptureEvent();
	    }

	@Test
	public void captureOnlyHtml() throws IOException
	    {
	    setupPlugin(true, true, false, true, false);
	    MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.ERROR);
	    _context.raiseEvent(event);

	    checkForHtmlCaptureEvent(1);
	    }

	@Test
	public void captureOnlyLogs()
	    {
	    setupPlugin(true, true, false, false, true);
	    MuseEvent event = EndTestEventType.create();
	    _context.raiseEvent(event);

	    Assert.assertEquals(1, _plugin.getData().size());
	    Assert.assertTrue(_plugin.getData().get(0) instanceof LogData);
	    }

	@Test
	public void testCaptureNothing()
	    {
	    setupPlugin(false, false, true, true, true);
	    _context.raiseEvent(MessageEventType.create("message"));
	    Assert.assertEquals(0, _plugin.getData().size());
	    }

	private void setupPlugin(boolean on_failure, boolean on_error, boolean capture_screenshot, boolean capture_html, boolean capture_logs)
		{
		WebdriverCapturePluginConfiguration config = new WebdriverCapturePluginConfiguration();
		if (on_failure)
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_ON_FAIL_PARAM, ValueSourceConfiguration.forValue(true));
		if (on_error)
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_ON_ERROR_PARAM, ValueSourceConfiguration.forValue(true));
		if (capture_screenshot)
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_SCREENSHOT_PARAM, ValueSourceConfiguration.forValue(true));
		if (capture_html)
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_HTML_PARAM, ValueSourceConfiguration.forValue(true));
		if (capture_logs)
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_LOGS_PARAM, ValueSourceConfiguration.forValue(true));
		_plugin = new WebdriverCapturePlugin(config);
		_plugin.initialize(_context);
		}

	@Before
	public void setup()
		{
		BrowserStepExecutionContext.putDriver(_driver, _context);
		_driver.putScreenshot(SCREENSHOT_BYTES);
		_driver.putHtml(HTML_BYTES);
		_driver.putLog(LOG_BYTES);
		_context.addEventListener((event) ->
			{
			if (TestResultStoredEventType.TYPE_ID.equals(event.getTypeId()))
				_storage_events.add(event);
			});
		}

	private MuseMockDriver _driver = new MuseMockDriver();
	private SteppedTestExecutionContext _context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
	private WebdriverCapturePlugin _plugin;
	private List<MuseEvent> _storage_events = new ArrayList<>();

	private final static byte[] SCREENSHOT_BYTES = "screenshot".getBytes();
	private final static byte[] HTML_BYTES = "content".getBytes();
	private final static byte[] LOG_BYTES = "log1".getBytes();
	}
