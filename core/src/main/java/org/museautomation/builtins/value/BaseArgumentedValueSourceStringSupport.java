package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseArgumentedValueSourceStringSupport extends BaseValueSourceStringExpressionSupport
    {
    @Override
    public ValueSourceConfiguration fromArgumentedExpression(String name, List<ValueSourceConfiguration> arguments, MuseProject project)
        {
        if (getName().equals(name) && arguments.size() == getNumberArguments())
            {
            ValueSourceConfiguration config = ValueSourceConfiguration.forType(getTypeId());

            if (arguments.size() == 1 && storeSingleArgumentAsSingleSubsource())
                config.setSource(arguments.get(0));
            else if (storeArgumentsNamed())
                {
                String[] names = getArgumentNamesChecked();
                for (int i = 0; i < arguments.size(); i++)
                    config.addSource(names[i], arguments.get(i));
                }
            else
                {
                for (int i = 0; i < arguments.size(); i++)
                    config.addSource(i, arguments.get(i));
                }

            return config;
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
        {
        if (getTypeId().equals(config.getType()))
            {
            StringBuilder builder = new StringBuilder();
            builder.append(getName());
            builder.append('(');

            if (getNumberArguments() == 1 && storeSingleArgumentAsSingleSubsource())
                builder.append(context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(), context));
            else if (storeArgumentsNamed())
                {
                String[] names = getArgumentNamesChecked();
                for (int i = 0; i < getNumberArguments(); i++)
                    {
                    if (i > 0)
                        builder.append(',');
                    builder.append(context.getProject().getValueSourceStringExpressionSupporters().toString(config.getSource(names[i]), context));
                    }
                }
            else
                {
                int arguments = 0;
                List<ValueSourceConfiguration> list = config.getSourceList();
                if (list != null)
                    for (ValueSourceConfiguration argument : list)
                        {
                        if (arguments > 0)
                            builder.append(',');
                        builder.append(context.getProject().getValueSourceStringExpressionSupporters().toString(argument, context));
                        arguments++;
                        }
                }

            builder.append(')');
            return builder.toString();
            }
        else
            return null;
        }

    public abstract String getName();
    protected abstract int getNumberArguments();
    protected abstract String getTypeId();
    /**
     * Override and return true to store a single argument as the singular subsource (accessed via getSource()) instead of
     * a list of one (accessed via getSourceList()).
     */
    protected boolean storeSingleArgumentAsSingleSubsource()
        {
        return false;
        }

    /**
     * Override and return true to store arguments as named subsources (accessed via getSource(name)) instead of a list.
     * Must also override #getArgumentNames().
     */
    protected boolean storeArgumentsNamed()
        {
        return false;
        }

    /**
     * Override to provide names for the arguments. Must also override #storeArgumentsNamed().
     */
    protected String[] getArgumentNames()
        {
        return null;
        }

    private String[] getArgumentNamesChecked()
        {
        String[] names = getArgumentNames();
        if (names == null)
            throw new UnsupportedOperationException("class " + getClass().getSimpleName() + " implemented storeArgumentsNamed() but returned null from getArgmentNames(). This is not allowed.");
        return names;
        }
    }


