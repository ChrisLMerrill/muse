package org.musetest.builtins.condition;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
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
@MuseStringExpressionSupportImplementation(ListContainsSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "List", description = "List to examine", type = SubsourceDescriptor.Type.Named, name = "list")
@MuseSubsourceDescriptor(displayName = "Target", description = "Value to look for", type = SubsourceDescriptor.Type.Named, name = "target")
public class ListContainsSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public ListContainsSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _list = getValueSource(config, LIST_PARAM, true, project);
        _target = getValueSource(config, TARGET_PARAM, true, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Object target = _target.resolveValue(context);
        List list = getValue(_list, context, false, List.class);
        boolean contains = list.contains(target);
        context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), contains));
        return contains;
        }

    private MuseValueSource _list = null;
    private MuseValueSource _target = null;

    public final static String LIST_PARAM = "list";
    public final static String TARGET_PARAM = "target";

    public final static String TYPE_ID = ListContainsSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "listContains";
            }

        @Override
        protected int getNumberArguments()
            {
            return 2;
            }

        @Override
        protected String[] getArgumentNames()
            {
            return new String[] {"list", "target"};
            }

        @Override
        protected boolean storeArgumentsNamed()
            {
            return true;
            }

        @Override
        protected String getTypeId()
            {
            return ListContainsSource.TYPE_ID;
            }
        }
    }
