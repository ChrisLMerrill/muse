package org.musetest.selenium.pages;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("page")
public class WebPage implements MuseResource
    {
    public Map<String, PageElement> getElements()
        {
        return _elements;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setElements(Map<String, PageElement> elements)
        {
        _elements = elements;
        }

    public void addElement(String id, PageElement element)
        {
        if (_elements == null)
            _elements = new HashMap<>();
        _elements.put(id, element);
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        return _metadata;
        }

    private Map<String, PageElement> _elements;
    private ResourceMetadata _metadata = new ResourceMetadata();

    @SuppressWarnings("unused")  // discovered via reflection
    public static class WebPageType extends ResourceType
        {
        public WebPageType()
            {
            super("page", "Web Page", WebPage.class);
            }
        }
    }


