package org.musetest.selenium.pages;

import org.musetest.core.*;
import org.slf4j.*;

/**
 * Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PageElementLocator
    {
    public PageElementLocator(MuseProject project)
        {
        _project = project;
        }

    public PageElement find(String key)
        {
        // split the key into separate keys for page and element
        String[] subkeys = key.split("\\.");
        if (subkeys.length < 2)
            {
            LOG.error("The key must have two parts, separated by a '.' - " + key);
            return null;
            }

        // find the page
        WebPage page = new PageLocator(_project).find(subkeys[0]);
        if (page == null)
            {
            LOG.error("Page not found in the project using key: " + subkeys[0]);
            return null;
            }

        // find the element
        PageElement element = page.getElements().get(subkeys[1]);
        if (element == null)
            {
            LOG.error(String.format("Element '%s' was not found in page '%s'", subkeys[1], subkeys[0]));
            return null;
            }
        return element;
        }

    MuseProject _project;

    final static Logger LOG = LoggerFactory.getLogger(PageElementLocator.class);
    }


