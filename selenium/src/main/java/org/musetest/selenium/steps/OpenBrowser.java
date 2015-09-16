package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
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
@MuseStepLongDescription("Opens a browser using the browser and provider configured in the project, as identified by the 'browser' and 'provider' sources. The 'browser' should resolve to the id of a SeleniumBrowserCapabilities project resource, which will be converted to a Selenium DesiredCapabilities object. Likewise, the 'provider' should resolve to the id of a to a WebDriverProviderConfiguration resource in the project, which is used to instantiate a WebDriver.")
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
    public StepExecutionResult execute(StepExecutionContext context) throws StepConfigurationError
        {
        // find the provider
        String provider_id = getValue(_provider, context, false, String.class);
        WebDriverProviderConfiguration provider = context.getTestExecutionContext().getProject().findResource(provider_id, WebDriverProviderConfiguration.class);
        if (provider == null)
            throw new StepConfigurationError("Unable to locate WebdriverProvider " + provider_id);

        String capabilities_id = getValue(_browser, context, false, String.class);
        SeleniumBrowserCapabilities capabilities = context.getTestExecutionContext().getProject().findResource(capabilities_id, SeleniumBrowserCapabilities.class);
        if (capabilities == null)
            throw new StepConfigurationError("Unable to locate SeleniumBrowserCapabilities " + capabilities_id);

        WebDriver driver = provider.getDriver(capabilities);
        if (driver == null)
            throw new StepConfigurationError(String.format("The WebdriverProvider (%s) was not able to provide a browser matching the specified capabilities (%s).", provider_id, capabilities.toDesiredCapabilities().toString()));

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


