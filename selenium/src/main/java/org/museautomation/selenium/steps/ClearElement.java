package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("clear-element")
@MuseStepName("Clear")
@MuseInlineEditString("clear {element}")
@MuseStepIcon("glyph:FontAwesome:\uf12d")
@MuseStepTypeGroup("Selenium.Other")
@MuseStepShortDescription("Clear an element")
@MuseStepLongDescription("Resolves the 'element' source to a WebElement and then calls the clear() method. If the element has text entry, it will clear the value.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to clear", type = SubsourceDescriptor.Type.Named, name = ClearElement.ELEMENT_PARAM)
@SuppressWarnings("WeakerAccess,unused") // discovered and invoked via reflection
public class ClearElement extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public ClearElement(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        getElement(_element_source, context).clear();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _element_source;

    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = ClearElement.class.getAnnotation(MuseTypeId.class).value();
    }


