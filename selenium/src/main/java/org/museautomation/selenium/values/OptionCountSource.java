package org.museautomation.selenium.values;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("option-count")
@MuseValueSourceName("Option Count")
@MuseValueSourceTypeGroup("Selenium.Element.Value")
@MuseValueSourceShortDescription("Returns the number of options in a select element")
@MuseValueSourceLongDescription("Resolves the supplied element as a select control and then returns the number of options.")
@MuseStringExpressionSupportImplementation(OptionCountSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to get value from", name = "element", type = SubsourceDescriptor.Type.Single)
@SuppressWarnings("unused")  // instantiated via reflection
public class OptionCountSource extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public OptionCountSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Integer resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        try
            {
            Select select = new Select(element);
            return select.getOptions().size();
            }
        catch (UnexpectedTagNameException e)
            {
            throw new ValueSourceResolutionError("The element is not a <select> tag.");
            }
        }

    @Override
    public String getDescription()
        {
        return String.format("elementAttribute(%s,%s)", getElementSource().getDescription(), _attribute_name_source.getDescription());
        }

    private MuseValueSource _attribute_name_source;

    public final static String TYPE_ID = OptionCountSource.class.getAnnotation(MuseTypeId.class).value();
    public final static String ELEMENT_PARAM = "element";

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "optionCount";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return OptionCountSource.TYPE_ID;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }
        }
    }
