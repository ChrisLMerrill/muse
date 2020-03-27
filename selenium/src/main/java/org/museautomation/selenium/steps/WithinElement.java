package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("within-element")
@MuseStepName("Within Element")
@MuseInlineEditString("Within {element}")
@MuseStepIcon("glyph:FontAwesome:DOT_CIRCLE_ALT")
@MuseStepTypeGroup("Selenium.Other")
@MuseStepShortDescription("Execute child steps within the scope of an element")
@MuseStepLongDescription("Resolves the 'element' source to a WebElement. Element location for steps within this step will be limited to this element and its children.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to work in", type = SubsourceDescriptor.Type.Named, name = WithinElement.ELEMENT_PARAM)
@SuppressWarnings("unused")  // instantiated by reflection
public class WithinElement extends BasicCompoundStep
    {
    @SuppressWarnings("unused") // called via reflection
    public WithinElement(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws MuseExecutionError
        {
        if (_element == null)
            _element = getValue(_element_source, context, false, WebElement.class);
        return super.shouldEnter(context);
        }

    @Override
    protected void beforeChildrenExecuted(StepExecutionContext context) throws MuseExecutionError
        {
        super.beforeChildrenExecuted(context);
        BrowserStepExecutionContext.pushSearchContext(context, _element);
        }

    @Override
    protected void afterChildrenExecuted(StepExecutionContext context) throws MuseExecutionError
        {
        if (BrowserStepExecutionContext.popSearchContext(context) != _element)
            throw new MuseExecutionError("The wrong search context was popped from the stack. Report this failure to the developers.");
        super.afterChildrenExecuted(context);
        }

    private MuseValueSource _element_source;
    private WebElement _element;

    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = WithinElement.class.getAnnotation(MuseTypeId.class).value();
    }


