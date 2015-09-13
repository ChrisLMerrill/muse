package org.musetest.builtins.condition;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // used via reflection
public abstract class BinaryConditionQuickEditSupport implements ValueSourceQuickEditSupport
    {
    @Override
    public ValueSourceConfiguration parse(String string, MuseProject project)
        {
        if (string.contains(getSeparator()))
            {
            StringTokenizer tokenizer = new StringTokenizer(string, getSeparator());
            if (!tokenizer.hasMoreTokens())
                return null;

            // parse the left side
            ValueSourceConfiguration left = null;
            List<ValueSourceConfiguration> configurations = ValueSourceQuickEditSupporters.parseWithAll(tokenizer.nextToken().trim(), project);
            if (configurations.size() > 0)
                left = configurations.get(0);

            ValueSourceConfiguration right = null;
            if (!tokenizer.hasMoreTokens())
                return null;
            configurations = ValueSourceQuickEditSupporters.parseWithAll(tokenizer.nextToken().trim(), project);
            if (configurations.size() > 0)
                right = configurations.get(0);

            if (left != null && right != null)
                {
                ValueSourceConfiguration config = new ValueSourceConfiguration();
                config.setType(getSourceType());
                config.addSource("left", left);
                config.addSource("right", right);
                return config;
                }
            }
        return null;
        }

    @Override
    public int getPriority()
        {
        return 1;
        }

    protected abstract String getSeparator();
    protected abstract String getSourceType();

    @Override
    public String asString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(getSourceType()))
            {
            try
                {
                String left = ValueSourceQuickEditSupporters.asStringFromAll(config.getSourceMap().get("left"), project).get(0);
                String right = ValueSourceQuickEditSupporters.asStringFromAll(config.getSourceMap().get("right"), project).get(0);
                return left + " " + getSeparator() + " " + right;
                }
            catch (Exception e)
                {
                // none
                }
            }
        return null;
        }
    }


