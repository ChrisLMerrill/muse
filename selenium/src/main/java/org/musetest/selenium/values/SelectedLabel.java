package org.musetest.selenium.values;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("element-selected-label")
@MuseValueSourceName("Selected Label")
@MuseValueSourceTypeGroup("Element.Value")
@MuseValueSourceShortDescription("Returns the label of the selected choice in the elment")
@MuseValueSourceLongDescription("Resolves the supplied element source, verifies that it is a select element, then returns the label of the selected choice (if any).")
@MuseStringExpressionSupportImplementation(SelectedLabel.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to get value from", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused")  // instantiated via reflection
public class SelectedLabel extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public SelectedLabel(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public String resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        Select select;
        try
            {
            select = new Select(element);
            }
        catch (UnexpectedTagNameException e)
            {
            throw new ValueSourceResolutionError(String.format("The element must be a <select> - found a %s", element.getTagName()));
            }
        String label = select.getFirstSelectedOption().getText();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), label));
        return label;
        }

    @Override
    public String getDescription()
        {
        return "selectedLabel(" + getElementSource().getDescription() + ")";
        }

    public final static String TYPE_ID = SelectedLabel.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "selectedLabel";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return SelectedLabel.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }