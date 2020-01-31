package org.musetest.selenium.values;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

import java.util.*;
import java.util.regex.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("keystrokes")
@MuseValueSourceName("Keystrokes")
@MuseValueSourceTypeGroup("Selenium")
@MuseValueSourceShortDescription("a string of characters containing keystroke symbols")
@MuseValueSourceLongDescription("A value source that returns a string of characters with keystrokes translated (e.g. {ESC}, {TAB}, {CTRL-A}, etc.")
@MuseStringExpressionSupportImplementation(KeystrokesStringSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Text", description = "text of the string, surrounded by quotes", type = SubsourceDescriptor.Type.Single)
public class KeystrokesStringSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public KeystrokesStringSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _source = config.getSource().createSource(project);
        if (_source == null)
            throw new MuseInstantiationException("The single-subsource configuration is required. Found null.");
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String value = getValue(_source, context, false, String.class);

        value = replaceRegularKeys(value);
        value = replaceChords(value);

        context.raiseEvent(ValueSourceResolvedEventType.create(value, value));

        return value;
        }

    private String replaceRegularKeys(String format_spec) throws ValueSourceResolutionError
        {
        StringBuilder formatter = new StringBuilder(format_spec);
        List<Object> values = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{(\\w+)}").matcher(format_spec);

        while (matcher.find())
            {
            String key = matcher.group(1);
            String format_key = String.format("{%s}", key);

            try
                {
                String keys = Keys.valueOf(key).toString();
                int index = formatter.indexOf(format_key);
                if (index != -1)
                    formatter.replace(index, index+format_key.length(), "%s");
                values.add(keys);
                }
            catch (IllegalArgumentException e)
                {
                throw new ValueSourceResolutionError(String.format("No key value found for '%s'", format_key));
                }
            }
        return String.format(formatter.toString(), values.toArray());
        }

    private String replaceChords(String format_spec) throws ValueSourceResolutionError
        {
        StringBuilder formatter = new StringBuilder(format_spec);
        List<Object> values = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\{(\\w+)-([A-Za-z0-9])}").matcher(format_spec);    // TODO this should allow any key and more than 2 at a time (i.e. CONTROL-ALT-P)

        while (matcher.find())
            {
            String match_modifer = matcher.group(1);
            String match_key = matcher.group(2);
            String format_key = String.format("{%s-%s}", match_modifer, match_key);

            try
                {
                String modifier = Keys.valueOf(match_modifer).toString();
                int index = formatter.indexOf(format_key);
                if (index != -1)
                    formatter.replace(index, index+format_key.length(), "%s");
                values.add(Keys.chord(modifier, match_key));
                }
            catch (IllegalArgumentException e)
                {
                throw new ValueSourceResolutionError(String.format("No key value found for '%s'", format_key));
                }
            }
        return String.format(formatter.toString(), values.toArray());
        }

    private MuseValueSource _source;

    public final static String TYPE_ID = KeystrokesStringSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "keystrokes";
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected String getTypeId()
            {
            return KeystrokesStringSource.TYPE_ID;
            }
        }
    }