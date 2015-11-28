package org.musetest.selenium.locators;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ElementByLocatorValueSourceStringExpressionSupport implements ValueSourceStringExpressionSupport
    {
    protected ElementByLocatorValueSourceStringExpressionSupport(String type_id, String text_prefix)
        {
        _type_id = type_id;
        _prefix = "<" + text_prefix + ":";
        }

    @Override
    public ValueSourceConfiguration parse(String string, MuseProject project)
        {
        if (string.startsWith(_prefix) && string.endsWith(">"))
            {
            List<ValueSourceConfiguration> configurations = ValueSourceQuickEditSupporters.parseWithAll(string.substring(_prefix.length(), string.length() - 1), project);
            if (configurations.size() > 0)
                return ValueSourceConfiguration.forSource(_type_id, configurations.get(0));
            }
        return null;
        }

    @Override
    public String asString(ValueSourceConfiguration config, MuseProject project)
        {
        if (config.getType().equals(_type_id))
            {
            List<String> strings = ValueSourceQuickEditSupporters.asStringFromAll(config.getSource(), project);
            if (strings.size() > 0)
                return _prefix + strings.get(0) + ">";
            }
        return null;
        }

    @Override
    public int getPriority()
        {
        return 2;
        }

    private String _prefix;
    private String _type_id;
    }


