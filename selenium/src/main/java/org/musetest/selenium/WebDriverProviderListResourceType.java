package org.musetest.selenium;

import org.musetest.core.resource.types.*;

/**
 * This class declares a resource type available within a MuseProject.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // discovered via reflection
public class WebDriverProviderListResourceType extends ResourceType
    {
    public WebDriverProviderListResourceType()
        {
        super("weproviders", "WebDriver Provider List", WebDriverProviderList.class);
        }
    }


