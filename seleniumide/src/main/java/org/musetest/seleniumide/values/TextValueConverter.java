package org.musetest.seleniumide.values;

import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.javascript.*;
import org.musetest.selenium.steps.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TextValueConverter implements ValueConverter
    {
    public TextValueConverter()
        {
        _types.add(new KeystrokeTokenType());
        _types.add(new VariableTokenType());
        _types.add(new ScriptTokenType());
        }

    @Override
    public ValueSourceConfiguration convert(String parameter)
        {
        List<TextValueToken> tokens = new ArrayList<>();
        int start = 0;
        while (start < parameter.length())
            {
            TextValueToken token = findNextToken(parameter, start);
            tokens.add(token);
            start += token.length();
            }

        if (tokens.size() == 0)
            return null;
        else if (tokens.size() == 1)
            return createSource(tokens.get(0), parameter);
        else
            {
            ValueSourceConfiguration concat = ValueSourceConfiguration.forType(AdditionSource.TYPE_ID);
            for (TextValueToken token : tokens)
                concat.addSource(createSource(token, parameter));
            compactSources(concat);
            if (concat.getSourceList().size() == 1)
                return concat.getSourceList().get(0);
            return concat;
            }
        }

    private void compactSources(ValueSourceConfiguration concat)
        {
        List<ValueSourceConfiguration> compacted = new ArrayList<>();
        for (ValueSourceConfiguration source : concat.getSourceList())
            {
            if (compacted.size() == 0)
                compacted.add(source);
            else
                {
                ValueSourceConfiguration last = compacted.get(compacted.size() - 1);
                // if both are String or Keystrokes, then combine them
                if ((last.getType().equals(StringValueSource.TYPE_ID) || last.getType().equals(KeystrokesStringSource.TYPE_ID))
                    && (source.getType().equals(StringValueSource.TYPE_ID) || source.getType().equals(KeystrokesStringSource.TYPE_ID)))
                    {
                    // get the string for each
                    String first;
                    if (last.getType().equals(KeystrokesStringSource.TYPE_ID))
                        first = last.getSource().getValue().toString();
                    else
                        first = last.getValue().toString();
                    String second;
                    if (source.getType().equals(KeystrokesStringSource.TYPE_ID))
                        second = source.getSource().getValue().toString();
                    else
                        second = source.getValue().toString();

                    // if either is a keystroke...the compacted will be keystroek
                    if (last.getType().equals(KeystrokesStringSource.TYPE_ID) || source.getType().equals(KeystrokesStringSource.TYPE_ID))
                        last.setType(KeystrokesStringSource.TYPE_ID);

                    if (last.getType().equals(KeystrokesStringSource.TYPE_ID))
                        {
                        last.setSource(ValueSourceConfiguration.forValue(first + second));
                        last.setValue(null);
                        }
                    else
                        {
                        last.setSource(null);
                        last.setValue(first + second);
                        }
                    }
                else
                    compacted.add(source);
                }
            }
        concat.setSourceList(compacted);
        }

    private ValueSourceConfiguration createSource(TextValueToken token, String target)
        {
        return token.type().createSource(target, token);
        }

    private TextValueToken findNextToken(String target, int start)
        {
        TextValueToken next = null;
        for (TokenType type : _types)
            {
            TextValueToken candidate = type.find(target, start);
            if ((candidate != null) && (next == null || candidate.startIndex() < next.startIndex()))
                next = candidate;
            }

        if (next != null)
            {
            if (next.startIndex() > start)
                return new TextValueToken(start, next.startIndex() - start, _default_type);
            return next;
            }
        return _default_type.find(target, start);
        }

    private List<TokenType> _types = new ArrayList<>();
    private TokenType _default_type = new LiteralTokenType();

    static class TextValueToken
        {
        public TextValueToken(int start, int length, TokenType type)
            {
            _start = start;
            _length = length;
            _type = type;
            }

        int length()
            {
            return _length;
            }
        TokenType type()
            {
            return _type;
            }
        int startIndex()
            {
            return _start;
            }
        private int _start;
        private int _length;
        private TokenType _type;
        }

    static abstract class TokenType
        {
        abstract TextValueToken find(String target, int start);
        abstract ValueSourceConfiguration createSource(String target, TextValueToken token);
        }

    static class LiteralTokenType extends TokenType
        {
        @Override
        TextValueToken find(String target, int start)
            {
            return new TextValueToken(start, target.length() - start, this);
            }

        @Override
        ValueSourceConfiguration createSource(String target, TextValueToken token)
            {
            String value = target.substring(token._start, token._start + token._length);
            return ValueSourceConfiguration.forValue(value);
            }
        }

    static class KeystrokeTokenType extends TokenType
        {
        @Override
        TextValueToken find(String target, int start)
            {
            int index = target.indexOf("${KEY_", start);
            if (index < 0)
                return null;

            int end_index = target.indexOf("}", start + 5);
            return new TextValueToken(index, end_index - index + 1, this);
            }

        @Override
        ValueSourceConfiguration createSource(String target, TextValueToken token)
            {
            int start = token._start + 6;
            String keycode = "{" + target.substring(start, start + token._length - 7) + "}";
            return ValueSourceConfiguration.forTypeWithSource(KeystrokesStringSource.TYPE_ID, keycode);
            }
        }

    static class VariableTokenType extends TokenType
        {
        @Override
        TextValueToken find(String target, int start)
            {
            int index = target.indexOf("${", start);
            if (index < 0)
                return null;

            int end_index = target.indexOf("}", start + 1);
            return new TextValueToken(index, end_index - index + 1, this);
            }

        @Override
        ValueSourceConfiguration createSource(String target, TextValueToken token)
            {
            int start = token._start + 2;
            String varname = target.substring(start, start + token._length - 3);
            return ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(varname));
            }
        }

    static class ScriptTokenType extends TokenType
        {
        @Override
        TextValueToken find(String target, int start)
            {
            int index = target.indexOf("javascript{", start);
            if (index < 0)
                return null;

            int end_index = target.indexOf("}", start + 11);
            return new TextValueToken(index, end_index - index + 1, this);
            }

        @Override
        ValueSourceConfiguration createSource(String target, TextValueToken token)
            {
            int start = token._start + 11;
            String script = target.substring(start, start + token._length - 12);
            ValueSourceConfiguration source = ValueSourceConfiguration.forType(EvaluateJavascriptValueSource.TYPE_ID);
            source.setSource(ValueSourceConfiguration.forValue(script));
            return source;
            }
        }
    }