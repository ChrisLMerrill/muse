package org.musetest.builtins.value.property;

import org.slf4j.*;

import java.lang.reflect.*;

/**
 * Resolves a property to a method of an object using reflection.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MethodByReflectionResolver implements PropertyResolver
    {
    @Override
    public boolean canResolve(Object target, String name)
        {
        if (target == null || name == null)
            return false;

        Class target_class = target.getClass();
        Method method = findMethod(target_class, name);
        return method != null;
        }

    private Method findMethod(Class target_class, String name)
        {
        try
            {
            return target_class.getMethod(name);
            }
        catch (NoSuchMethodException e)
            {
            // ok
            }
        try
            {
            String getter_name = "get" + new String(new char[] {name.charAt(0)}).toUpperCase() + name.substring(1);
            return target_class.getMethod(getter_name);
            }
        catch (NoSuchMethodException e)
            {
            // ok
            }
        return null;
        }

    @Override
    public Object resolve(Object target, String name) throws PropertyResolutionError
        {
        if (target == null)
            throw new PropertyResolutionError("target parameter cannot be null");
        if (name == null)
            throw new PropertyResolutionError("name parameter cannot be null");

        Method method = findMethod(target.getClass(), name);
        if (method == null)
            throw new PropertyResolutionError(String.format("cannot locate property %s in target (%s): %s", name, target.getClass().getSimpleName(), target.toString()));

        try
            {
            return method.invoke(target);
            }
        catch (Exception e)
            {
            String message = String.format("caught an exception while resolving property %s in target (%s): %s", name, target.getClass().getSimpleName(), target.toString());
            LOG.error(message, e);
            throw new PropertyResolutionError(message, e);
            }
        }

    private final static Logger LOG = LoggerFactory.getLogger(MethodByReflectionResolver.class);
    }