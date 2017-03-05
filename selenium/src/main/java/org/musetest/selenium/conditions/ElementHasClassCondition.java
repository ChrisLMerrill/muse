package org.musetest.selenium.conditions;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("has-class")
@MuseValueSourceName("Element has class")
@MuseValueSourceTypeGroup("Element.Condition")
@MuseValueSourceShortDescription("Returns true if the element has the provided class")
@MuseValueSourceLongDescription("Returns true if the sub-source returns a Selenium WebElement that has a class attribute containing 'class' parameter.")
@MuseStringExpressionSupportImplementation(ElementHasClassCondition.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Element", description = "The element to inspect", type = SubsourceDescriptor.Type.Named, name = ElementHasClassCondition.ELEMENT_PARAM)
@MuseSubsourceDescriptor(displayName = "Class", description = "The class to look for", type = SubsourceDescriptor.Type.Named, name = ElementHasClassCondition.CLASS_PARAM)
@SuppressWarnings("unused,WeakerAccess")  // discovered by reflection
public class ElementHasClassCondition extends BaseElementValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ElementHasClassCondition(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _class_source = getValueSource(config, CLASS_PARAM, true, project);
        }

    @Override
    protected MuseValueSource findElementSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        return getValueSource(config, ELEMENT_PARAM, true, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        WebElement element = resolveElementSource(context, true);
        String class_to_find = getValue(_class_source, context, false, String.class);

        String class_attribute = element.getAttribute("class");
        boolean has_class = false;
        if (class_attribute != null)
            {
            StringTokenizer tokenizer = new StringTokenizer(class_attribute, " ");
            while (tokenizer.hasMoreTokens())
                if (tokenizer.nextToken().equals(class_to_find))
                    {
                    has_class = true;
                    break;
                    }
            }

        context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), has_class));
        return has_class;
        }

    @Override
    public String getDescription()
        {
        return METHOD_HANE + "(" + getElementSource().getDescription() + "," + _class_source.getDescription() + ")";
        }

    private MuseValueSource _class_source;

    public final static String ELEMENT_PARAM = "element";
    public final static String CLASS_PARAM = "class";
    public final static String TYPE_ID = ElementHasClassCondition.class.getAnnotation(MuseTypeId.class).value();
    private final static String METHOD_HANE = "elementHasClass";

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return METHOD_HANE;
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {ELEMENT_PARAM, CLASS_PARAM};
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String getTypeId()
            {
            return ElementHasClassCondition.TYPE_ID;
            }

        }
    }
