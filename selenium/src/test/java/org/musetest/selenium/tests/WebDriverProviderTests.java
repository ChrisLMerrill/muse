package org.musetest.selenium.tests;

import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
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
class WebDriverProviderTests
    {
    @Test
    void mockProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assertions.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriver driver = driver_providers.getDriver(new SeleniumBrowserCapabilities(MuseMockProvider.MUSE_BROWSER), new ProjectExecutionContext(new SimpleProject()));
        Assertions.assertTrue(driver instanceof MuseMockDriver);
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    void chromeDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assertions.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof ChromeDriverProvider)
                {
                Assertions.assertEquals("path-to\\chromedriver.exe", ((ChromeDriverProvider) provider).getRelativePath());
                return;
                }

        Assertions.fail("no provider found for Chrome");
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    void firefoxMarionetteDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assertions.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof GeckoDriverProvider)
                {
                Assertions.assertEquals("path-to\\geckodriver.exe", ((GeckoDriverProvider) provider).getRelativePath());
                return;
                }

        Assertions.fail("no provider found for Chrome");
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    void iexploreDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assertions.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof IExploreDriverProvider)
                {
                Assertions.assertEquals("path-to\\iexploredriver.exe", ((IExploreDriverProvider) provider).getRelativePath());
                return;
                }

        Assertions.fail("no provider found for IE");
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    void firefoxDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assertions.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof GeckoDriverProvider)
                return;

        Assertions.fail("no provider found for Firefox");
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    void safariDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("driver-providers.json"), WebDriverProviderConfiguration.class);

        Assertions.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        for (WebDriverProvider provider : provider_list.getProviders())
            if (provider instanceof SafariDriverProvider)
                return;

        Assertions.fail("no provider found for Safari");
        }

    /**
     * Ensure the serialization format remains compatible
     */
    @Test
    void remoteDriverProvider() throws IOException
        {
        ObjectMapper mapper = JsonMapperFactory.createMapper(new TypeLocator((MuseProject)null));
        WebDriverProviderConfiguration driver_providers = mapper.readValue(getClass().getResourceAsStream("remote-provider.json"), WebDriverProviderConfiguration.class);

        Assertions.assertTrue(driver_providers instanceof WebDriverProviderList);
        WebDriverProviderList provider_list = (WebDriverProviderList) driver_providers;
        WebDriverProvider provider = provider_list.getProviders().get(0);
        if (provider instanceof RemoteDriverProvider)
            {
            Assertions.assertEquals("http://url/of/driver/service", ((RemoteDriverProvider) provider).getUrl());
            return;
            }

        Assertions.fail("remote provider not found");
        }

    @Test
    void listListenerRegistrationAndAddEvents()
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

        Assertions.assertEquals(0, listener._added_index.longValue());
        Assertions.assertEquals(new_provider, listener._added_provider);

        // remove listener
        listener.reset();
        list.removeListener(listener);
        list.add(new_provider);
        Assertions.assertNull(listener._added_provider);
        }

    @Test
    void listListenerRemoveEvents()
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

        Assertions.assertEquals(1, listener._removed_index.longValue());
        Assertions.assertEquals(provider2, listener._removed_provider);
        }
    }
