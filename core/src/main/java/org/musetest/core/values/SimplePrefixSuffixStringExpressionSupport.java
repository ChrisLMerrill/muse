package org.musetest.core.values;

import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class SimplePrefixSuffixStringExpressionSupport extends BaseValueSourceStringExpressionSupport
    {
    public SimplePrefixSuffixStringExpressionSupport(String type, String prefix, String suffix)
        {
        _type = type;
        _prefix = prefix;
        _suffix = suffix;
        }

    @Override
    public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
        {
        if (string.startsWith(_prefix) && string.endsWith(_suffix))
            {
            List<ValueSourceConfiguration> configs = ValueSourceQuickEditSupporters.parseWithAll(string.substring(_prefix.length(), string.length() - _suffix.length()), project);
            if (configs.size() > 0)
                return ValueSourceConfiguration.forSource(_type, configs.get(0));
            }
        return null;
        }

    @Override
    public String toString(ValueSourceConfiguration config, MuseProject project)
        {
        if (_type.equals(config.getType()) && config.getSource() != null)
            {
            List<String> strings = ValueSourceQuickEditSupporters.asStringFromAll(config.getSource(), project);
            if (strings.size() > 0)
                return _prefix + strings.get(0) + _suffix;
            }
        return null;
        }

    private String _type;
    private String _prefix;
    private String _suffix;
    }


