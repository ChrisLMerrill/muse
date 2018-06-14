package org.musetest.selenium.plugins;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.logging.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class WebdriverCapturePlugin extends GenericConfigurableTestPlugin implements MuseEventListener, DataCollector
	{
	public WebdriverCapturePlugin(WebdriverCapturePluginConfiguration configuration)
		{
		super(configuration);
		_config = configuration;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{
		_context = context;
		_context.addEventListener(this);
		_collect_on_fail = _config.isCollectOnFailure(_context);
		_collect_on_error = _config.isCollectOnError(_context);
		_collect_on_success = _config.isCollectOnSuccess(_context);
		_collect_html = _config.isCollectHtml(_context);
		_collect_screenshot = _config.isCollectScreenshot(_context);
		_collect_logs = _config.isCollectLogs(_context);
		}

	@Override
	public void eventRaised(MuseEvent event)
		{
		if ((_collect_on_fail && event.hasTag(MuseEvent.FAILURE))
			|| (_collect_on_error && event.hasTag(MuseEvent.ERROR))
			|| (_collect_on_success && event.getTypeId().equals(EndStepEventType.TYPE_ID)))
			collect(_context, event);
		}

	private void collect(MuseExecutionContext context, MuseEvent event)
		{
		final WebDriver driver;
		try
			{
			driver = BrowserStepExecutionContext.getDriver(context);
			}
		catch (ValueSourceResolutionError e)
			{
			return;  // no driver...no capture
			}

		// capture screenshot
		if (_collect_screenshot)
			{
			if (driver instanceof TakesScreenshot)
				_data.add(new ScreenshotData(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));
			}

		// capture HTML
		if (_collect_html)
			_data.add(new HtmlData(driver.getPageSource().getBytes()));

		// capture logs
		if (_collect_logs)
			{
			final Logs logs = driver.manage().logs();
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
		}

	@Override
	public List<TestResultData> getData()
		{
		return _data;
		}

	private MuseExecutionContext _context;
	private WebdriverCapturePluginConfiguration _config;
	private boolean _collect_on_fail = false;
	private boolean _collect_on_error = false;
	private boolean _collect_on_success = false;
	private boolean _collect_html = false;
	private boolean _collect_screenshot = false;
	private boolean _collect_logs = false;

	private List<TestResultData> _data = new ArrayList<>();

	private final static Logger LOG = LoggerFactory.getLogger(WebdriverCapturePlugin.class);
	}
