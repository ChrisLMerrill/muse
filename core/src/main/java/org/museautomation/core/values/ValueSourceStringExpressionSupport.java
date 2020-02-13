package org.museautomation.core.values;

import org.museautomation.core.*;

import java.util.*;

/**
 * Interface for classes that support quick-editing of a value source. Quick-editing allows
 * the user to enter a text string that is parsed to create the value source.
 * ValueSource implementations are not required to implement this.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ValueSourceStringExpressionSupport
    {
    ValueSourceConfiguration fromLiteral(String string, MuseProject project);
    ValueSourceConfiguration fromPrefixedExpression(String prefix, ValueSourceConfiguration expression, MuseProject project);
    ValueSourceConfiguration fromElementExpression(String type, List<ValueSourceConfiguration> arguments, MuseProject project);
    ValueSourceConfiguration fromElementLookupExpression(List<ValueSourceConfiguration> arguments, MuseProject project);
    ValueSourceConfiguration fromArgumentedExpression(String name, List<ValueSourceConfiguration> arguments, MuseProject project);
    ValueSourceConfiguration fromArrayExpression(List<ValueSourceConfiguration> elements, MuseProject project);
    ValueSourceConfiguration fromArrayItemExpression(ValueSourceConfiguration collection, ValueSourceConfiguration selector, MuseProject project);
    ValueSourceConfiguration fromBinaryExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project);
    ValueSourceConfiguration fromBooleanExpression(ValueSourceConfiguration left, String operator, ValueSourceConfiguration right, MuseProject project);
    ValueSourceConfiguration fromDotExpression(ValueSourceConfiguration left, ValueSourceConfiguration right, MuseProject project);
    String toString(ValueSourceConfiguration config, StringExpressionContext context);
    String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth);
    }
