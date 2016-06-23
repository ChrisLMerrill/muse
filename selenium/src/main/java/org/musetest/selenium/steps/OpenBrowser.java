package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;

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
public class OpenBrowser extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public OpenBrowser(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _browser = getValueSource(config, BROWSER_PARAM, true, project);
        _provider = getValueSource(config, PROVIDER_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        // find the provider
        WebDriverProviderConfiguration provider = getValue(_provider, context, false, WebDriverProviderConfiguration.class);
        if (provider == null)
            {
            ValueSourceConfiguration provider_config = getConfiguration().getSource(PROVIDER_PARAM);
            throw new StepConfigurationError("Unable to locate WebdriverProvider from source: " + context.getTestExecutionContext().getProject().getValueSourceDescriptors().get(provider_config).getInstanceDescription(provider_config));
            }

        SeleniumBrowserCapabilities capabilities = getValue(_browser, context, false, SeleniumBrowserCapabilities.class);
        if (capabilities == null)
            {
            ValueSourceConfiguration browser_config = getConfiguration().getSource(PROVIDER_PARAM);
            throw new StepConfigurationError("Unable to locate SeleniumBrowserCapabilities from source: " + context.getTestExecutionContext().getProject().getValueSourceDescriptors().get(browser_config).getInstanceDescription(browser_config));
            }

        WebDriver driver = provider.getDriver(capabilities);
        if (driver == null)
            {
            ValueSourceConfiguration provider_config = getConfiguration().getSource(PROVIDER_PARAM);
            throw new StepConfigurationError(String.format("The WebdriverProvider (%s) was not able to provide a browser matching the specified capabilities (%s).", context.getTestExecutionContext().getProject().getValueSourceDescriptors().get(provider_config).getInstanceDescription(provider_config), capabilities.toDesiredCapabilities().toString()));
            }

        BrowserStepExecutionContext.putDriver(driver, context);

        context.getTestExecutionContext().registerShuttable(driver::quit);

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _browser;
    private MuseValueSource _provider;

    public final static String BROWSER_PARAM = "browser";
    public final static String PROVIDER_PARAM = "provider";

    public final static String TYPE_ID = OpenBrowser.class.getAnnotation(MuseTypeId.class).value();
    }


