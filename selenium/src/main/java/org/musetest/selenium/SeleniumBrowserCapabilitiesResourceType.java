package org.musetest.selenium;

import org.musetest.core.resource.types.*;

/**
 * This class declares a resource type available within a MuseProject.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // discovered via reflection
public class SeleniumBrowserCapabilitiesResourceType extends ResourceType
    {
    public SeleniumBrowserCapabilitiesResourceType()
        {
        super(SeleniumBrowserCapabilities.TYPE_ID, "Browser", SeleniumBrowserCapabilities.class);
        }
    }


