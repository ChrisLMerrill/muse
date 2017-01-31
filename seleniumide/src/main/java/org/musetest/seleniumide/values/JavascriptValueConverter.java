package org.musetest.seleniumide.values;

import org.musetest.javascript.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // invoked via reflection
public class JavascriptValueConverter extends PrefixedBracketedValueConverter
    {
    @Override
    protected String getValueSourceTypeId()
        {
        return EvaluateJavascriptValueSource.TYPE_ID;
        }

    @Override
    protected String getPrefix()
        {
        return "javascript";
        }
    }


