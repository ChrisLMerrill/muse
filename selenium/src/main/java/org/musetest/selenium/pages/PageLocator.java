package org.musetest.selenium.pages;

import org.musetest.core.*;

/**
 * @author ©2015 Web Performance, Inc
 */
public class PageLocator
    {
    public PageLocator(MuseProject project)
        {
        _project = project;
        }

    public WebPage find(String id)
        {
        return _project.findResource(id, WebPage.class);
        }

    MuseProject _project;
    }


