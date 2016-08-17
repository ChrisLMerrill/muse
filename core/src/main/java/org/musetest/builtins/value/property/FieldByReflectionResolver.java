package org.musetest.builtins.value.property;

import java.lang.reflect.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FieldByReflectionResolver implements PropertyResolver
    {
    @Override
    public boolean canResolve(Object target, String name)
        {
        if (target == null || name == null)
            return false;

        Class target_class = target.getClass();
        Field field = findField(target_class, name);
        return field != null;
        }

    private Field findField(Class target_class, String name)
        {
        try
            {
            return target_class.getField(name);
            }
        catch (NoSuchFieldException e)
            {
            return null;
            }
        }

    @Override
    public Object resolve(Object target, String name) throws PropertyResolutionError
        {
        if (target == null)
            throw new PropertyResolutionError("target parameter cannot be null");
        if (name == null)
            throw new PropertyResolutionError("name parameter cannot be null");

        Field field = findField(target.getClass(), name);
        if (field == null)
            throw new PropertyResolutionError(String.format("cannot locate property %s in target (%s): %s", name, target.getClass().getSimpleName(), target.toString()));

        try
            {
            field.setAccessible(true);
            return field.get(target);
            }
        catch (Exception e)
            {
            throw new PropertyResolutionError(String.format("caught an exception while resolving property %s in target (%s): %s", name, target.getClass().getSimpleName(), target.toString()), e);
            }
        }
    }


