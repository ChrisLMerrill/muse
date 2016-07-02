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
        return find(subkeys[0], subkeys[1]);
        }

    public PageElement find(String page_id, String element_id)
        {
        // find the page
        WebPage page = new PageLocator(_project).find(page_id);
        if (page == null)
            {
            LOG.error("Page not found in the project using key: " + page_id);
            return null;
            }

        // find the element
        PageElement element = page.getElements().get(element_id);
        if (element == null)
            {
            LOG.error(String.format("Element '%s' was not found in page '%s'", element_id, page_id));
            return null;
            }
        return element;
        }

    MuseProject _project;

    final static Logger LOG = LoggerFactory.getLogger(PageElementLocator.class);
    }


