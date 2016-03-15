package org.musetest.selenium.pages;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;

/**
 * @author ©2015 Web Performance, Inc
 */
@MuseTypeId("page-element")
@MuseValueSourceName("Element by page/element lookup")
@MuseValueSourceShortDescription("Locates a Selenium WebElement from the page/element specified by the subsource")
@MuseValueSourceTypeGroup("Element.Locate")
@MuseStringExpressionSupportImplementation(PagesElementValueSourceStringExpressionSupport.class)
public class PagesElementValueSource extends BaseSeleniumValueSource
    {
    public PagesElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        ValueSourceConfiguration locator_config = config.getSource();
        if (locator_config == null)
            throw new MuseInstantiationException("PagesElementValueSource requires a sub-source to find the page/element in the project.");
        _locator_source = locator_config.createSource(project);
        }

    @Override
    public Object resolveValue(StepExecutionContext context) throws StepConfigurationError
        {
        String page_element_key = _locator_source.resolveValue(context).toString();

        int page_element_separator = page_element_key.indexOf(".");
        if (page_element_separator < 0)
            throw new ValueSourceResolutionError(String.format("The page/element key must contain a '.' to separate the page and element names.  (key = %s)", page_element_key));

        MuseProject project = context.getTestExecutionContext().getProject();
        PageElement element = new PageElementLocator(project).find(page_element_key);
        if (element == null)
            throw new ValueSourceResolutionError(String.format("Element not found in the project...unable to lookup locator by key '%s'", page_element_key));

        ValueSourceConfiguration element_locator_config = element.getLocator();
        if (element_locator_config == null)
            throw new ValueSourceResolutionError(String.format("No locator configured for element '%s'", page_element_key));

        return element_locator_config.createSource(project).resolveValue(context);
        }

    MuseValueSource _locator_source;

    public final static String TYPE_ID = PagesElementValueSource.class.getAnnotation(MuseTypeId.class).value();

    }


