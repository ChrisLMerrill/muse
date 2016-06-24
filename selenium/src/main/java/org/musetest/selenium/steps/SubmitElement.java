package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("submit-element")
@MuseStepName("Submit")
@MuseInlineEditString("submit {element}")
@MuseStepIcon("glyph:FontAwesome:\uf090")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Submit a form")
@MuseStepLongDescription("Resolves the 'element' source to a WebElement and then calls the submit() method. If the element is a form or an input element within the form, the form will be submitted.")
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to submit", type = SubsourceDescriptor.Type.Named, name = SubmitElement.ELEMENT_PARAM)
@SuppressWarnings("unused")  // invoked via reflection
public class SubmitElement extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public SubmitElement(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _element_source = getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
        {
        getElement(_element_source, context).submit();
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _element_source;

    public final static String ELEMENT_PARAM = "element";

    public final static String TYPE_ID = SubmitElement.class.getAnnotation(MuseTypeId.class).value();
    }


