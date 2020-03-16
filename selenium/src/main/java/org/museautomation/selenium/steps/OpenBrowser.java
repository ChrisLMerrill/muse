package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.core.values.strings.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("open-browser")
@MuseStepName("Open a browser")
@MuseInlineEditString("open {browser} using {provider}")
@MuseStepIcon("glyph:FontAwesome:GLOBE")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Open a new browser session")
@MuseStepLongDescription("Opens a browser using the 'browser' and 'provider' sources. The 'browser' should resolve to a SeleniumBrowserCapabilities object. Likewise, the 'provider' should resolve to a WebDriverProviderConfiguration, which is used to instantiate a WebDriver using the supplied capabilities.")
@MuseSubsourceDescriptor(displayName = "Browser", description = "The capabilities of browser to open (expects a project resource of type SeleniumBrowserCapabilities)", type = SubsourceDescriptor.Type.Named, name = OpenBrowser.BROWSER_PARAM)
@MuseSubsourceDescriptor(displayName = "Provider", description = "The browser provider to use (expects a project resource of type WebDriverProviderConfiguration)", type = SubsourceDescriptor.Type.Named, name = OpenBrowser.PROVIDER_PARAM)
@MuseSubsourceDescriptor(displayName = "Close on exit", description = "If true, the browser will automatically be closed when the task ends.", type = SubsourceDescriptor.Type.Named, name = OpenBrowser.AUTOCLOSE_PARAM, defaultValue = "true")
public class OpenBrowser extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public OpenBrowser(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _project = project;
        _browser = getValueSource(config, BROWSER_PARAM, true, project);
        _provider = getValueSource(config, PROVIDER_PARAM, true, project);
        _autoclose = getValueSource(config, AUTOCLOSE_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        // find the provider
        WebDriverProviderConfiguration provider = getValue(_provider, context, false, WebDriverProviderConfiguration.class);
        final StepExpressionContext expression_context = new StepExpressionContext(_project, getConfiguration());
        if (provider == null)
            {
            ValueSourceConfiguration provider_config = getConfiguration().getSource(PROVIDER_PARAM);
            throw new StepConfigurationError("Unable to locate WebdriverProvider from source: " + context.getProject().getValueSourceDescriptors().get(provider_config).getInstanceDescription(provider_config, expression_context));
            }

        SeleniumBrowserCapabilities capabilities = getValue(_browser, context, false, SeleniumBrowserCapabilities.class);
        if (capabilities == null)
            {
            ValueSourceConfiguration browser_config = getConfiguration().getSource(PROVIDER_PARAM);
            throw new StepConfigurationError("Unable to locate SeleniumBrowserCapabilities from source: " + context.getProject().getValueSourceDescriptors().get(browser_config).getInstanceDescription(browser_config, expression_context));
            }

        WebDriver driver = provider.getDriver(capabilities, context);
        if (driver == null)
            {
            ValueSourceConfiguration provider_config = getConfiguration().getSource(PROVIDER_PARAM);
            throw new StepConfigurationError(String.format("The WebdriverProvider (%s) was not able to provide a browser matching the specified capabilities (%s).", context.getProject().getValueSourceDescriptors().get(provider_config).getInstanceDescription(provider_config, expression_context), capabilities.toDesiredCapabilities().toString()));
            }

        BrowserStepExecutionContext.putDriver(driver, context);

        Boolean close = getValue(_autoclose, context, Boolean.class, true);
        if (close)
            {
            context.registerShuttable(() ->
                {
                try
                    {
                    driver.quit();
                    }
                catch (Exception e)
                    {
                    LOG.error("Exception encountered while cleaning up the driver", e);
                    }
                });
            }

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private final MuseValueSource _browser;
    private final MuseValueSource _provider;
    private final MuseValueSource _autoclose;
    private final MuseProject _project;

    public final static String BROWSER_PARAM = "browser";
    public final static String PROVIDER_PARAM = "provider";
    final static String AUTOCLOSE_PARAM = "autoclose";

    public final static String TYPE_ID = OpenBrowser.class.getAnnotation(MuseTypeId.class).value();

    private final static Logger LOG = LoggerFactory.getLogger(OpenBrowser.class);
    }


