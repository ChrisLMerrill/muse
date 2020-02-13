package org.museautomation.core.util;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection (in Stringifiers)
public class MapStringifier extends Stringifier
    {
    @Override
    public Object create(Object target)
        {
        if (target instanceof Map)
            {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            Map map = (Map) target;
            for (Object key : map.keySet())
                {
                if (first)
                    first = false;
                else
                    builder.append(", ");
                builder.append(key.toString());
                builder.append("=");
                builder.append(map.get(key));
                }
            return builder.toString();
            }
        return null;
        }
    }


