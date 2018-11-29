package org.musetest.tests.utils;

import org.junit.jupiter.api.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class AssertRange
	{
	public static void assertNearly(Long expected, Long actual, Long acceptable_variance)
		{
		if (expected < actual - acceptable_variance || actual + acceptable_variance < expected)
			Assertions.fail(format(String.format("Value was not +/- %d", acceptable_variance), expected, actual));
		}


	// copied from Assert because it is package-protected :(
	private static String format(String message, Object expected, Object actual)
		{
		String formatted = "";
		if (message != null && !message.equals(""))
			{
			formatted = message + " ";
			}

		String expectedString = String.valueOf(expected);
		String actualString = String.valueOf(actual);
		return expectedString.equals(actualString) ? formatted + "expected: " + formatClassAndValue(expected, expectedString) + " but was: " + formatClassAndValue(actual, actualString) : formatted + "expected:<" + expectedString + "> but was:<" + actualString + ">";
		}

	// copied from Assert because it is package-protected :(
	private static String formatClassAndValue(Object value, String valueString)
		{
		String className = value == null ? "null" : value.getClass().getName();
		return className + "<" + valueString + ">";
		}
	}


