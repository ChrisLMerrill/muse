package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("list-contains")
@MuseValueSourceName("List Contains")
@MuseValueSourceShortDescription("returns true if the list contains the specified value")
@MuseValueSourceLongDescription("Evaluates the 'list' and 'target' parameters. If the former is a list and it contains the latter, this source returns true.")
@MuseStringExpressionSupportImplementation(ListContainsSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "List", description = "List to examine", type = SubsourceDescriptor.Type.Named, name = "list")
@MuseSubsourceDescriptor(displayName = "Target", description = "Value to look for", type = SubsourceDescriptor.Type.Named, name = "target")
public class ListContainsSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ListContainsSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        ValueSourceConfiguration list_source = config.getSource(LIST_PARAM);
        if (list_source != null)
            _list = list_source.createSource(project);
        ValueSourceConfiguration target_source = config.getSource(TARGET_PARAM);
        if (target_source != null)
            _target = target_source.createSource(project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Object list_object = _list.resolveValue(context);
        if (!(list_object instanceof List))
            throw new ValueSourceResolutionError(String.format("'%s' source must evaluate to a list. Instead it is a %s (%s).", LIST_PARAM, list_object.getClass().getSimpleName(), list_object.toString()));
        List list = (List) list_object;

        Object target = _target.resolveValue(context);
        return list.contains(target);
        }

    private MuseValueSource _list = null;
    private MuseValueSource _target = null;

    public final static String LIST_PARAM = "list";
    public final static String TARGET_PARAM = "target";

    public final static String TYPE_ID = ListContainsSource.class.getAnnotation(MuseTypeId.class).value();
    }
