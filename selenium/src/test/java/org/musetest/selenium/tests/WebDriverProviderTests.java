package org.musetest.selenium.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.json.*;
import org.musetest.core.util.*;
import org.musetest.selenium.*;
import org.musetest.selenium.mocks.*;
import org.musetest.selenium.providers.*;
import org.openqa.selenium.*;

import java.io.*;

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
        WebDriver driver = driver_providers.getDriver(new SeleniumBrowserCapabilities(MuseMockProvider.MUSE_BROWSER), new ProjectExecutionContext(new SimpleProject()));
        Assert.assertTrue(driver instanceof MuseMockDriver);
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
            if (provider instanceof GeckoDriverProvider)
                {
                Assert.assertEquals("path-to\\geckodriver.exe", ((GeckoDriverProvider) provider).getRelativePath());
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
            if (provider instanceof GeckoDriverProvider)
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

    @Test
    public void listListenerRegistrationAndAddEvents()
        {
        WebDriverProviderList list = new WebDriverProviderList();
        class MyChangeListener implements WebDriverProviderList.ChangeListener
            {
            @Override
            public void providerAdded(int index, WebDriverProvider provider)
                {
                _added_index = index;
                _added_provider = provider;
                }

            @Override
            public void providerRemoved(int index, WebDriverProvider provider) {}

            private void reset()
                {
                _added_index = null;
                _added_provider = null;
                }
            private Integer _added_index;
            private WebDriverProvider _added_provider;
            }
        MyChangeListener listener = new MyChangeListener();
        list.addListener(listener);

        RemoteDriverProvider new_provider = new RemoteDriverProvider();
        list.add(new_provider);

        Assert.assertEquals(0, listener._added_index.longValue());
        Assert.assertEquals(new_provider, listener._added_provider);

        // remove listener
        listener.reset();
        list.removeListener(listener);
        list.add(new_provider);
        Assert.assertEquals(null, listener._added_provider);
        }

    @Test
    public void listListenerRemoveEvents()
        {
        WebDriverProviderList list = new WebDriverProviderList();
        RemoteDriverProvider provider1 = new RemoteDriverProvider();
        list.add(provider1);
        RemoteDriverProvider provider2 = new RemoteDriverProvider();
        list.add(provider2);

        class MyChangeListener implements WebDriverProviderList.ChangeListener
            {
            @Override
            public void providerAdded(int index, WebDriverProvider provider) { }

            @Override
            public void providerRemoved(int index, WebDriverProvider provider)
                {
                _removed_index = index;
                _removed_provider = provider;
                }

            private Integer _removed_index;
            private WebDriverProvider _removed_provider;
            }
        MyChangeListener listener = new MyChangeListener();
        list.addListener(listener);

        list.remove(provider2);

        Assert.assertEquals(1, listener._removed_index.longValue());
        Assert.assertEquals(provider2, listener._removed_provider);
        }
    }
