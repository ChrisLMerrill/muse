package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("foreach")
@MuseStepName("For Each")
@MuseInlineEditString("for each {name} in {list}")
@MuseStepIcon("glyph:FontAwesome:REPEAT")
@MuseStepTypeGroup("Conditionals & Looping")
@MuseStepShortDescription("For each item in the list, iterate the loop, setting a variable (name) to the item.")
@MuseStepLongDescription("The 'condition' source is resolved evaluated as a boolean. If true, the child steps will be executed. After they are executed, the source will be evaluated again. This repeats until the 'condition' source resolves to false.")
@MuseSubsourceDescriptor(displayName = "List", description = "List to iterate (expects List or Array)", type = SubsourceDescriptor.Type.Named, name = ForEachStep.LIST_PARAM, defaultValue = "[1, 2, 3]")
@MuseSubsourceDescriptor(displayName = "Item Name", description = "Name of variable to set the item into.", type = SubsourceDescriptor.Type.Named, name = ForEachStep.ITEM_NAME_PARAM, defaultValue = "index")
public class ForEachStep extends BasicCompoundStep
    {
    public ForEachStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _list_source = getValueSource(config, LIST_PARAM, true, project);
        _name_source = getValueSource(config, ITEM_NAME_PARAM, true, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws ValueSourceResolutionError
        {
        if (_iterator == null)
            {
            Object object = _list_source.resolveValue(context);
            List list;
            if (object instanceof List)
                list = (List) object;
            else if (object instanceof Array)
                list = Arrays.asList((Array) object);
            else
                throw new IllegalArgumentException("The list parameter of an ForEach step must resolve to a List or Array. The list parameter resolved to a " + object);

            _name = getValue(_name_source, context, false, String.class);
            _iterator = list.iterator();
            }
        return _iterator.hasNext();
        }

    @Override
    protected void beforeChildrenExecuted(StepExecutionContext context) throws MuseExecutionError
        {
        super.beforeChildrenExecuted(context);
        context.setVariable(_name, _iterator.next());
        }

    @Override
    protected void afterChildrenExecuted(StepExecutionContext context) throws MuseExecutionError
        {
        super.afterChildrenExecuted(context);
        context.setVariable(_name, null);
        }

    private MuseValueSource _list_source;
    private MuseValueSource _name_source;
    private Iterator _iterator;
    private String _name;

    public final static String LIST_PARAM = "list";
    public final static String ITEM_NAME_PARAM = "name";
    public final static String TYPE_ID = ForEachStep.class.getAnnotation(MuseTypeId.class).value();
    }


