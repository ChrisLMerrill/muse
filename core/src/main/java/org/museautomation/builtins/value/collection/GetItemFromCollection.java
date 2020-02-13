package org.museautomation.builtins.value.collection;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Retrieves an item from a collection. Currently only supports index access to lists.
 * TODO Future: support indexed access to Arrays
 * TODO Future: support keyed access to maps
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("get-item")
@MuseValueSourceName("Get Item from Collection")
@MuseValueSourceShortDescription("get an item from a collection (list or map)")
@MuseValueSourceLongDescription("Evaluates the 'Selector' source to an integer or string and then looks for an item in the 'collection' source. If the collection is a list, it expects the selector to be an integer. If the collection is a map, then it will evaulate the selector as an integer or string and use that as the key to search the map.")
@MuseStringExpressionSupportImplementation(GetItemFromCollection.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Collection", description = "Name of the property to access", type = SubsourceDescriptor.Type.Named, name = "collection")
@MuseSubsourceDescriptor(displayName = "Selector", description = "The object to look for the property in", type = SubsourceDescriptor.Type.Named, name = "selector")
public class GetItemFromCollection extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public GetItemFromCollection(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _collection = getValueSource(config, COLLECTION_PARAM, true, project);
        _selector = getValueSource(config, SELECTOR_PARAM, true, project);
        }

    public MuseValueSource getCollection()
        {
        return _collection;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Object collection = getValue(_collection, context, false, Object.class);
        Object selector = getValue(_selector, context, false);

        if (collection instanceof List)
            {
            if (selector instanceof Number)
                {
                long index = ((Number) selector).longValue();
                if (index > (long) Integer.MAX_VALUE)
                    throw new ValueSourceResolutionError(String.format("ListItem unable to proceed: index exceeds the maximum list index: %s (maximum is %s)", index, Integer.MAX_VALUE));
                try
                    {
                    Object result = ((List) collection).get((int) index);
                    context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), result));
                    return result;
                    }
                catch (IndexOutOfBoundsException e)
                    {
                    throw new ValueSourceResolutionError("ListItem unable to proceed: index out of bounds: " + index);
                    }
                }
            }
        else if (collection instanceof Map)
            {
            Map map = (Map) collection;
            return map.get(map.keySet().iterator().next()); // TODO access something other than the first element
            }
        else
            {
            // TODO do we really need to deal with arrays?  I think not - stick with lists and maps!
            try
                {
                return Array.get(collection, 0); // TODO access something other than the first element
                }
            catch (IllegalArgumentException e)
                {
                // it wasn't an array :(
                }
            }

        // TODO log an error - the arguemnts were not as expected
        return null;
        }

    private MuseValueSource _collection;
    private MuseValueSource _selector;

    public final static String COLLECTION_PARAM = "collection";
    public final static String SELECTOR_PARAM = "selector";

    public final static String TYPE_ID = GetItemFromCollection.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromArrayItemExpression(ValueSourceConfiguration collection, ValueSourceConfiguration selector, MuseProject project)
            {
            ValueSourceConfiguration config = ValueSourceConfiguration.forType(GetItemFromCollection.TYPE_ID);
            config.addSource(GetItemFromCollection.SELECTOR_PARAM, selector);
            config.addSource(GetItemFromCollection.COLLECTION_PARAM, collection);
            return config;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(GetItemFromCollection.TYPE_ID))
                {
                String collection = context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(GetItemFromCollection.COLLECTION_PARAM), context, depth + 1);
                String selector = context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(GetItemFromCollection.SELECTOR_PARAM), context, depth + 1);
                String expression = String.format("%s[%s]", collection, selector);
                if (depth == 0)
                    return expression;
                }
            return null;
            }
        }
    }
