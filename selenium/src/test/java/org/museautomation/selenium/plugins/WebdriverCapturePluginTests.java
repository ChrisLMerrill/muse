package org.museautomation.selenium.plugins;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.step.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.events.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resultstorage.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.*;
import org.museautomation.selenium.*;
import org.museautomation.selenium.mocks.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class WebdriverCapturePluginTests
	{
	@Test
    void testCaptureOnFailure() throws IOException
		{
	    setupPlugin(true, false, true, true, true, false);
	    final MuseEvent event = MessageEventType.create("message");
	    event.addTag(MuseEvent.FAILURE);
	    _context.raiseEvent(event);
		checkForHtmlCaptureEvent(1);
		checkForScreenshotCaptureEvent();
	    }

	@Test
    void testCaptureOnError() throws IOException
		{
	    setupPlugin(false, true, true, true, true, false);
		raiseErrorEvent();
		checkForHtmlCaptureEvent(1);
		checkForScreenshotCaptureEvent();
	    }

	@Test
    void testCaptureTwice() throws IOException
	    {
	    setupPlugin(true, true, false, true, false, true);
	    raiseErrorEvent();
	    raiseErrorEvent();

	    checkForHtmlCaptureEvent(2);
	    }

	@Test
    void dontCaptureDuplicateScreenshots() throws IOException
		{
	    setupPlugin(true, true, true, false, false, false);
	    _driver.putScreenshot("screenshot1".getBytes());
	    raiseErrorEvent();

		Assertions.assertEquals(1, _storage_events.size());
	    checkForScreenshotCaptureEvent("screenshot1".getBytes());

		_driver.putScreenshot("screenshot1".getBytes());
	    raiseErrorEvent();
	    Assertions.assertEquals(1, _storage_events.size(), "duplicate screenshot was captured");

		_driver.putScreenshot("screenshot2".getBytes());
	    raiseErrorEvent();
	    Assertions.assertEquals(2, _storage_events.size(), "second (different) screenshot was NOT captured");
		checkForScreenshotCaptureEvent("screenshot2".getBytes());
	    }

	@Test
    void dontCaptureDuplicateHtml() throws IOException
		{
	    setupPlugin(true, true, false, true, false, false);
	    _driver.putHtml("html1".getBytes());
	    raiseErrorEvent();

		Assertions.assertEquals(1, _storage_events.size());
		checkForHtmlCaptureEvent("html1".getBytes());

		_driver.putHtml("html1".getBytes());
	    raiseErrorEvent();
	    Assertions.assertEquals(1, _storage_events.size(), "duplicate HTML was captured");

		_driver.putHtml("html2".getBytes());
	    raiseErrorEvent();
	    Assertions.assertEquals(2, _storage_events.size(), "second (different) HTML was NOT captured");
		checkForHtmlCaptureEvent("html2".getBytes());
	    }

	private void raiseErrorEvent()
		{
		MuseEvent event = MessageEventType.create("message");
		event.addTag(MuseEvent.ERROR);
		_context.raiseEvent(event);
		}

	@Test
    void captureOnlyScreenshot()  throws IOException
	    {
	    setupPlugin(true, true, true, false, false, false);
	    raiseErrorEvent();

        checkForScreenshotCaptureEvent();
	    }

	@Test
    void captureOnlyHtml() throws IOException
	    {
	    setupPlugin(true, true, false, true, false, false);
	    raiseErrorEvent();

	    checkForHtmlCaptureEvent(1);
	    }

	@Test
    void captureOnlyLogs()
	    {
	    setupPlugin(true, true, false, false, true, false);
	    MuseEvent event = EndTaskEventType.create();
	    _context.raiseEvent(event);

	    Assertions.assertEquals(1, _plugin.getData().size());
	    Assertions.assertTrue(_plugin.getData().get(0) instanceof LogData);
	    }

	@Test
    void testCaptureNothing()
	    {
	    setupPlugin(false, false, true, true, true, false);
	    _context.raiseEvent(MessageEventType.create("message"));
	    Assertions.assertEquals(0, _plugin.getData().size());
	    }

	private void checkForScreenshotCaptureEvent() throws IOException
		{
		checkForScreenshotCaptureEvent(SCREENSHOT_BYTES);
		}

	private void checkForScreenshotCaptureEvent(byte[] screenshot) throws IOException
		{
		boolean screenshot_found = false;
		for (MuseEvent event : _storage_events)
			if (event.getAttributeAsString(TaskResultStoredEventType.RESULT_DESCRIPTION).toLowerCase().contains("screenshot"))
				{
				TaskResultData data = (TaskResultData) _context.getVariable(event.getAttributeAsString(TaskResultStoredEventType.VARIABLE_NAME));
				Assertions.assertNotNull(data);
				if (Arrays.equals(screenshot, getBytes(data)))
					screenshot_found = true;
				}

		Assertions.assertTrue(screenshot_found, "screenshot not captured");
		}

	private void checkForHtmlCaptureEvent(int count) throws IOException
		{
		int html_found = 0;
        html_found = getHtmlFound(HTML_BYTES, html_found);

        Assertions.assertEquals(count, html_found, "HTML not captured");
		}

	private void checkForHtmlCaptureEvent(byte[] content) throws IOException
		{
		int html_found = 0;
        html_found = getHtmlFound(content, html_found);

        Assertions.assertEquals(1, html_found, "HTML not captured");
		}

    private int getHtmlFound(byte[] content, int html_found) throws IOException
        {
        for (MuseEvent event : _storage_events)
            if (event.getAttribute(TaskResultStoredEventType.RESULT_DESCRIPTION).toString().toLowerCase().contains("html"))
                {
                TaskResultData data = (TaskResultData) _context.getVariable(event.getAttributeAsString(TaskResultStoredEventType.VARIABLE_NAME));
                Assertions.assertNotNull(data);
                if (Arrays.equals(content, getBytes(data)))
                    html_found++;
                }
        return html_found;
        }

    private byte[] getBytes(TaskResultData data) throws IOException
		{
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        data.write(outstream);
		return outstream.toByteArray();
		}

	private void setupPlugin(boolean on_failure, boolean on_error, boolean capture_screenshot, boolean capture_html, boolean capture_logs, boolean record_duplicates)
		{
		WebdriverCapturePluginConfiguration config = new WebdriverCapturePluginConfiguration();
		config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_ON_FAIL_PARAM, ValueSourceConfiguration.forValue(on_failure));
		config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_ON_ERROR_PARAM, ValueSourceConfiguration.forValue(on_error));
		config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_SCREENSHOT_PARAM, ValueSourceConfiguration.forValue(capture_screenshot));
		config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_HTML_PARAM, ValueSourceConfiguration.forValue(capture_html));
		config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_LOGS_PARAM, ValueSourceConfiguration.forValue(capture_logs));
		config.parameters().addSource(WebdriverCapturePluginConfiguration.IGNORE_DUPLICATES_PARAM, ValueSourceConfiguration.forValue(!record_duplicates));
		_plugin = new WebdriverCapturePlugin(config);
		_plugin.initialize(_context);
		}

	@BeforeEach
    void setup()
		{
		BrowserStepExecutionContext.putDriver(_driver, _context);
		_driver.putScreenshot(SCREENSHOT_BYTES);
		_driver.putHtml(HTML_BYTES);
		_driver.putLog(LOG_BYTES);
		_context.addEventListener((event) ->
			{
			if (TaskResultStoredEventType.TYPE_ID.equals(event.getTypeId()))
				_storage_events.add(event);
			});
		}

	private MuseMockDriver _driver = new MuseMockDriver();
	private SteppedTaskExecutionContext _context = new DefaultSteppedTaskExecutionContext(new SimpleProject(), new SteppedTask(new StepConfiguration(LogMessage.TYPE_ID)));
	private WebdriverCapturePlugin _plugin;
	private List<MuseEvent> _storage_events = new ArrayList<>();

	private final static byte[] SCREENSHOT_BYTES = "screenshot".getBytes();
	private final static byte[] HTML_BYTES = "content".getBytes();
	private final static byte[] LOG_BYTES = "log1".getBytes();
	}
