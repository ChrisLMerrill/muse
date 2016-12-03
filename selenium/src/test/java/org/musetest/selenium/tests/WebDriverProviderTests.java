package org.musetest.selenium.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.execution.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.json.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.SteppedTest;
import org.musetest.core.tests.utils.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.musetest.selenium.mocks.*;
import org.musetest.selenium.providers.*;
import org.openqa.selenium.*;

import java.io.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class WebDriverProviderTests
    {
    @Test
    public void mockProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assert.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriver driver = driver_providers.getDriver(new SeleniumBrowserCapabilities(MuseMockProvider.MUSE_BROWSER), new BaseExecutionContext(new SimpleProject()));
        Assert.assertTrue(driver instanceof MuseMockDriver);
        }

    @Test
    public void closeShuttables() throws URISyntaxException, InterruptedException, ValueSourceResolutionError
        {
        MuseProject project = SeleniumStepTests.createSeleniumTestProject();
        StepConfiguration open_step = SeleniumStepTests.createOpenBrowserStep();
        SteppedTest test = new SteppedTest(open_step);
        InteractiveTestController controller = new InteractiveTestController();
        controller.runPastStep(new SteppedTestProviderImpl(project, test), open_step);
        new TestStateBlocker(controller).blockUntil(InteractiveTestState.PAUSED);

        MuseMockDriver driver = (MuseMockDriver) BrowserStepExecutionContext.getDriver(controller.getTestRunner().getTestContext());
        Assert.assertNotNull(driver);

        controller.resume();
        new TestStateBlocker(controller).blockUntil(InteractiveTestState.IDLE);

        Thread.sleep(50); // allow time for the test executor thread to finish and driver to stop.
        Assert.assertTrue(driver._is_quitted);
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    public void chromeDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assert.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof ChromeDriverProvider)
                {
                Assert.assertEquals("path-to\\chromedriver.exe", ((ChromeDriverProvider) provider).getRelativePath());
                return;
                }

        Assert.assertTrue("no provider found for Chrome", false);
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    public void firefoxMarionetteDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assert.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof FirefoxMarionetteDriverProvider)
                {
                Assert.assertEquals("path-to\\geckodriver.exe", ((FirefoxMarionetteDriverProvider) provider).getRelativePath());
                return;
                }

        Assert.assertTrue("no provider found for Chrome", false);
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    public void iexploreDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assert.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof IExploreDriverProvider)
                {
                Assert.assertEquals("path-to\\iexploredriver.exe", ((IExploreDriverProvider) provider).getRelativePath());
                return;
                }

        Assert.assertTrue("no provider found for IE", false);
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    public void firefoxDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assert.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof FirefoxDriverProvider || provider instanceof FirefoxMarionetteDriverProvider)
                return;

        Assert.assertTrue("no provider found for Firefox", false);
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    public void safariDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assert.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof SafariDriverProvider)
                return;

        Assert.assertTrue("no provider found for Safari", false);
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    public void remoteDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("remote-provider.json"), WebDriverProviderConfiguration.class);

        Assert.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        WebDriverProvider provider = provider_list.getProviders().get(0);
        if (provider instanceof RemoteDriverProvider)
            {
            Assert.assertEquals("http://url/of/driver/service", ((RemoteDriverProvider) provider).getUrl());
            return;
            }

        Assert.assertTrue("remote provider not found", false);
        }

    }


