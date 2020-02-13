package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resultstorage.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.diagnostic.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("capture-dom")
@MuseStepName("Capture DOM")
@MuseInlineEditString("Capture DOM from {element}")
@MuseStepIcon("glyph:FontAwesome:FILE_CODE_ALT")
@MuseStepTypeGroup("Selenium.Other")
@MuseStepShortDescription("Capture the DOM")
@MuseStepLongDescription("Resolves the 'element' source to a WebElement and then captures the DOM below that element, to be saved to local storage.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to click", type = SubsourceDescriptor.Type.Named, name = CaptureDom.ELEMENT_PARAM)
public class CaptureDom extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public CaptureDom(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        WebElement start_element = getElement(_element_source, context);

        // capture the DOM
        DomElement root = captureElement(start_element, context);

        // store as a variable
        final String varname = context.createVariable("__dom_capture", new DomCapture(root));
        context.raiseEvent(TestResultStoredEventType.create(varname, "DOM capture"));

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private DomElement captureElement(WebElement start_element, StepExecutionContext context) throws ValueSourceResolutionError
        {
        DomElement captured = new DomElement();

        captured.setTag(start_element.getTagName());

        // get list of attributes
        Map<String,String> attributes = (Map) ((JavascriptExecutor) getDriver(context)).executeScript("var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;", start_element);
        for (String name : attributes.keySet())
            captured.getAttributes().put(name, start_element.getAttribute(name));

        // find children
//        StringBuilder all_text = new StringBuilder();
        List<WebElement> children = start_element.findElements(By.xpath("*"));
        for (WebElement child : children)
            {
            DomElement captured_child = captureElement(child, context);
            captured.getChildren().add(captured_child);
//            String child_text = captured_child.getText();
//            if (child_text != null)
//                all_text.append(child_text);
            }
//        captured.getAttributes().put("all_text", all_text.toString());

        // Compute and de-duplicate the text for this element. The API returns the combined text of all children. This
        // will attempt to determine if the text comes only from the children and, if so, ignore it.
        String element_text = start_element.getText();
        if (element_text != null && !(element_text.equals(captured.getChildText())))
            captured.setText(element_text);
        return captured;
        }

    private MuseValueSource _element_source;

    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = CaptureDom.class.getAnnotation(MuseTypeId.class).value();
    }


