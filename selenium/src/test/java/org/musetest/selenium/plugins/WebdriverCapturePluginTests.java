package org.musetest.selenium.plugins;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
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
		checkForAll3Outputs();
	    }

	private void checkForAll3Outputs() throws IOException
		{
		boolean screenshot_found = false;
		boolean html_found = false;
		boolean log_found = false;
		for (TestResultData data : _plugin.getData())
			{
			if (data instanceof ScreenshotData)
				{
				screenshot_found = true;
				Assert.assertTrue(Arrays.equals(SCREENSHOT_BYTES, getBytes(data)));
				}
			else if (data instanceof HtmlData)
				{
				html_found = true;
				Assert.assertTrue(Arrays.equals(HTML_BYTES, getBytes(data)));
				}
			else if (data instanceof LogData)
				{
				log_found = true;
				Assert.assertTrue(new String(getBytes(data)).contains(new String(LOG_BYTES)));
				}
			}

		Assert.assertTrue("screenshot not captured", screenshot_found);
		Assert.assertTrue("HTML not captured", html_found);
		Assert.assertTrue("log not captured", log_found);
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
		checkForAll3Outputs();
	    }

	@Test
	public void testCaptureTwice()
	    {
	    setupPlugin(true, true, false, true, false);
	    MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.ERROR);
	    _context.raiseEvent(event);

	    event = MessageEventType.create("message");
	    event.addTag(MuseEvent.FAILURE);
	    _context.raiseEvent(event);

	    Assert.assertEquals(2, _plugin.getData().size());
	    }

	@Test
	public void captureOnlyScreenshot()
	    {
	    setupPlugin(true, true, true, false, false);
	    MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.ERROR);
	    _context.raiseEvent(event);

	    Assert.assertEquals(1, _plugin.getData().size());
	    Assert.assertTrue(_plugin.getData().get(0) instanceof ScreenshotData);
	    }

	@Test
	public void captureOnlyHtml()
	    {
	    setupPlugin(true, true, false, true, false);
	    MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.ERROR);
	    _context.raiseEvent(event);

	    Assert.assertEquals(1, _plugin.getData().size());
	    Assert.assertTrue(_plugin.getData().get(0) instanceof HtmlData);
	    }

	@Test
	public void captureOnlyLogs()
	    {
	    setupPlugin(true, true, false, false, true);
	    MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.ERROR);
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
		}

	private MuseMockDriver _driver = new MuseMockDriver();
	private SteppedTestExecutionContext _context = new DefaultSteppedTestExecutionContext(new SimpleProject(), new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));
	private WebdriverCapturePlugin _plugin;

	private final static byte[] SCREENSHOT_BYTES = "screenshot".getBytes();
	private final static byte[] HTML_BYTES = "content".getBytes();
	private final static byte[] LOG_BYTES = "log1".getBytes();
	}
