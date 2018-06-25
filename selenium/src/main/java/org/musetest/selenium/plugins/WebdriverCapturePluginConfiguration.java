package org.musetest.selenium.plugins;

import org.musetest.core.*;
import org.musetest.core.plugins.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("webdriver-capture-plugin")
@MuseSubsourceDescriptor(displayName = "Apply automatically?", description = "If this source resolves to true, this plugin configuration will be automatically applied to tests", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.AUTO_APPLY_PARAM)
@MuseSubsourceDescriptor(displayName = "Apply only if", description = "Apply only if this source resolves to true", type = SubsourceDescriptor.Type.Named, name = GenericConfigurablePlugin.APPLY_CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Collect on failures", description = "Collect data on each failure if this source is true", type = SubsourceDescriptor.Type.Named, name = WebdriverCapturePluginConfiguration.COLLECT_ON_FAIL_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Collect on errors", description = "Collect data on each error if this source is true", type = SubsourceDescriptor.Type.Named, name = WebdriverCapturePluginConfiguration.COLLECT_ON_ERROR_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Collect on successes", description = "Collect data on each successful step completion if this source is true", type = SubsourceDescriptor.Type.Named, name = WebdriverCapturePluginConfiguration.COLLECT_ON_SUCCESS_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Collect HTML", description = "Collect the HTML page source if this source is true", type = SubsourceDescriptor.Type.Named, name = WebdriverCapturePluginConfiguration.COLLECT_HTML_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Collect logs", description = "Collect the Webdriver logs if this source is true", type = SubsourceDescriptor.Type.Named, name = WebdriverCapturePluginConfiguration.COLLECT_LOGS_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Collect screenshot", description = "Collect a screenshot if this source is true", type = SubsourceDescriptor.Type.Named, name = WebdriverCapturePluginConfiguration.COLLECT_SCREENSHOT_PARAM, optional = true, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Ignore duplicates", description = "Do not collect screenshot or HTML if it has not changed since the last capture.", type = SubsourceDescriptor.Type.Named, name = WebdriverCapturePluginConfiguration.IGNORE_DUPLICATES_PARAM, optional = true, defaultValue = "true")
public class WebdriverCapturePluginConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new CaptureOnFailurePluginConfigurationType();
		}

	@Override
	public MusePlugin createPlugin()
		{
		return new WebdriverCapturePlugin(this);
		}

	public boolean isCollectOnFailure(MuseExecutionContext context)
		{
		return isParameterTrue(context, COLLECT_ON_FAIL_PARAM);
		}

	public boolean isCollectOnError(MuseExecutionContext context)
		{
		return isParameterTrue(context, COLLECT_ON_ERROR_PARAM);
		}

	public boolean isCollectOnSuccess(MuseExecutionContext context)
		{
		return isParameterTrue(context, COLLECT_ON_SUCCESS_PARAM);
		}

	public boolean isCollectScreenshot(MuseExecutionContext context)
		{
		return isParameterTrue(context, COLLECT_SCREENSHOT_PARAM);
		}

	public boolean isCollectHtml(MuseExecutionContext context)
		{
		return isParameterTrue(context, COLLECT_HTML_PARAM);
		}

	public boolean isCollectLogs(MuseExecutionContext context)
		{
		return isParameterTrue(context, COLLECT_LOGS_PARAM);
		}

	public boolean isIgnoreDuplicates(MuseExecutionContext context)
		{
		if (_parameters.getSource(IGNORE_DUPLICATES_PARAM) == null)
			return true;
		return isParameterTrue(context, IGNORE_DUPLICATES_PARAM);
		}

	public final static String TYPE_ID = WebdriverCapturePluginConfiguration.class.getAnnotation(MuseTypeId.class).value();
	public final static String COLLECT_ON_FAIL_PARAM = "collect-on-fail";
	public final static String COLLECT_ON_ERROR_PARAM = "collect-on-error";
	public final static String COLLECT_ON_SUCCESS_PARAM = "collect-on-success";
	public final static String COLLECT_HTML_PARAM = "collect-html";
	public final static String COLLECT_LOGS_PARAM = "collect-logs";
	public final static String COLLECT_SCREENSHOT_PARAM = "collect-screenshot";
	public final static String IGNORE_DUPLICATES_PARAM = "ignore-duplicates";

	public static class CaptureOnFailurePluginConfigurationType extends ResourceSubtype
		{
		@Override
		public WebdriverCapturePluginConfiguration create()
			{
			final WebdriverCapturePluginConfiguration config = new WebdriverCapturePluginConfiguration();
			config.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_ON_FAIL_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_ON_ERROR_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_HTML_PARAM, ValueSourceConfiguration.forValue(true));
			config.parameters().addSource(WebdriverCapturePluginConfiguration.COLLECT_SCREENSHOT_PARAM, ValueSourceConfiguration.forValue(true));
			return config;
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public CaptureOnFailurePluginConfigurationType()
			{
			super(TYPE_ID, "WebDriver Capture", WebdriverCapturePluginConfiguration.class, new PluginConfiguration.PluginConfigurationResourceType());
			}
		}
	}
