package org.musetest.selenium.plugins;

import net.openhft.hashing.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resultstorage.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.musetest.selenium.steps.*;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class WebdriverCapturePlugin extends GenericConfigurableTestPlugin implements MuseEventListener, DataCollector
	{
	WebdriverCapturePlugin(WebdriverCapturePluginConfiguration configuration)
		{
		super(configuration);
		_config = configuration;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		if (context instanceof SteppedTestExecutionContext)
			{
			_context = (SteppedTestExecutionContext) context;
			_context.addEventListener(this);
			_collect_on_fail = _config.isCollectOnFailure(_context);
			_collect_on_error = _config.isCollectOnError(_context);
			_collect_on_success = _config.isCollectOnSuccess(_context);
			_collect_html = _config.isCollectHtml(_context);
			_collect_screenshot = _config.isCollectScreenshot(_context);
			_collect_logs = _config.isCollectLogs(_context);
			_ignore_duplicates = _config.isIgnoreDuplicates(_context);
			}
		else
			LOG.error("WebdriverCapturePlugin can only be used in a SteppedTestExecutionContext. But was initialized into a " + context.getClass().getSimpleName());
		}

	@Override
	public void eventRaised(MuseEvent event)
		{
		if (StartStepEventType.TYPE_ID.equals(event.getTypeId()) || EndTestEventType.TYPE_ID.equals(event.getTypeId()))
			{
			if (_logs_collected)
				return;
			if (EndTestEventType.TYPE_ID.equals(event.getTypeId())
				|| CloseBrowser.TYPE_ID.equals(_context.getStepLocator().findStep(StepEventType.getStepId(event)).getType()))
				{
				collectLogs();
				return;
				}
			}

		if ((_collect_on_fail && event.hasTag(MuseEvent.FAILURE))
			|| (_collect_on_error && event.hasTag(MuseEvent.ERROR))
			|| (_collect_on_success && event.getTypeId().equals(EndStepEventType.TYPE_ID)))
			collectScreenshotAndPageContent();
		}

	private void collectScreenshotAndPageContent()
		{
		WebDriver driver = getDriver();
		if (driver == null)
			return;

		// capture screenshot
		if (_collect_screenshot)
			{
			if (driver instanceof TakesScreenshot)
				{
				final byte[] screenshot_bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
				if (shouldSaveScreenshot(screenshot_bytes))
					{
					final TestResultData data = new ScreenshotData(screenshot_bytes);
					final String varname = _context.createVariable("_captured_screenshot", data);
					_context.raiseEvent(TestResultStoredEventType.create(varname, "screenshot"));
					}
				}
			}

		// capture HTML
		if (_collect_html)
			{
			final byte[] html_bytes = driver.getPageSource().getBytes();
			if (shouldSaveHtml(html_bytes))
				{
				final HtmlData data = new HtmlData(html_bytes);
				final String varname = _context.createVariable("_captured_html", data);
				_context.raiseEvent(TestResultStoredEventType.create(varname, "page HTML"));
				}
			}
		}

	private boolean shouldSaveScreenshot(byte[] screenshot)
		{
		if (!_ignore_duplicates)
			return true;

		final long new_hash = LongHashFunction.xx().hashBytes(screenshot);
		if (new_hash == _last_screenshot_hash)
			return false;
		_last_screenshot_hash = new_hash;
		return true;
		}

	private boolean shouldSaveHtml(byte[] html)
		{
		if (!_ignore_duplicates)
			return true;

		final long new_hash = LongHashFunction.xx().hashBytes(html);
		if (new_hash == _last_HTML_hash)
			return false;
		_last_HTML_hash = new_hash;
		return true;
		}

	private WebDriver getDriver()
		{
		WebDriver driver = null;
		try
			{
			driver = BrowserStepExecutionContext.getDriver(_context);
			}
		catch (ValueSourceResolutionError e)
			{
			// no driver
			}
		return driver;
		}

	private void collectLogs()
		{
		if (_logs_collected)
			return;
		if (_collect_logs)
			{
            try
                {
                final Logs logs = getDriver().manage().logs();
                for (String type : logs.getAvailableLogTypes())
                    {
                    StringBuilder builder = new StringBuilder();
                    for (LogEntry entry : logs.get(type).getAll())
                        {
                        if (builder.length() > 0)
                            builder.append("\n");
                        builder.append(entry.toString());
                        }
                    _data.add(new LogData(type, builder.toString().getBytes()));
                    }
                }
            catch (Exception e)
                {
                LOG.error("Unable to access WebDriver logs", e);
                }
            _logs_collected = true;
			}
		}

	@Override
	public List<TestResultData> getData()
		{
		return _data;
		}

	private SteppedTestExecutionContext _context;
	private WebdriverCapturePluginConfiguration _config;
	private boolean _collect_on_fail = false;
	private boolean _collect_on_error = false;
	private boolean _collect_on_success = false;
	private boolean _collect_html = false;
	private boolean _collect_screenshot = false;
	private boolean _collect_logs = false;
	private boolean _ignore_duplicates = false;
	private boolean _logs_collected = false;
	private long _last_screenshot_hash = 0L;
	private long _last_HTML_hash = 0L;

	private List<TestResultData> _data = new ArrayList<>();

	private final static Logger LOG = LoggerFactory.getLogger(WebdriverCapturePlugin.class);
	}
