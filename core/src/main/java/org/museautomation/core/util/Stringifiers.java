package org.museautomation.core.util;

import org.reflections.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Stringifiers
    {
    public static Object find(Object target)
        {
        if (STRINGIFIERS == null)
            {
            STRINGIFIERS = new HashSet<>();
            Set<Class<? extends Stringifier>> implementers = DEFAULT_REFLECTIONS.getSubTypesOf(Stringifier.class);
            for (Class<? extends Stringifier> implementation : implementers)
                try
                    {
                    STRINGIFIERS.add(implementation.newInstance());
                    }
                catch (Exception e)
                    {
                    LOG.error("Unable to instantiate a Stringifier implementation for class " + implementation.getSimpleName() + ", does it implement a no-args constructor?");
                    }
            }

        for (Stringifier stringifier : STRINGIFIERS)
            {
            Object result = stringifier.create(target);
            if (result != null)
                return result;
            }
        return target;
        }

    private static Set<Stringifier> STRINGIFIERS = null;
    private final static Reflections DEFAULT_REFLECTIONS = new Reflections("org.museautomation");

    private final static Logger LOG = LoggerFactory.getLogger(Stringifiers.class);
    }


