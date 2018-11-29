package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class GlobPatternTests
    {
    @Test
    void isPattern()
        {
        Assertions.assertFalse(new GlobPattern("abc123").hasWildcard());
        Assertions.assertTrue(new GlobPattern("abc?123").hasWildcard());
        Assertions.assertTrue(new GlobPattern("abc*123").hasWildcard());
        Assertions.assertTrue(new GlobPattern("abc[1-3]").hasWildcard());
        }

    @Test
    void starMatch()
        {
        GlobPattern pattern = new GlobPattern("a*a");
        Assertions.assertTrue(pattern.matches("aba"));
        Assertions.assertTrue(pattern.matches("abbba"));
        Assertions.assertTrue(pattern.matches("aa"));
        Assertions.assertFalse(pattern.matches("abc"));
        }

    @Test
    void questionMatch()
        {
        GlobPattern pattern = new GlobPattern("a?a");
        Assertions.assertTrue(pattern.matches("aba"));
        Assertions.assertFalse(pattern.matches("abbba"));
        Assertions.assertFalse(pattern.matches("aa"));
        }

    @Test
    void numericRangeMatch()
        {
        GlobPattern pattern = new GlobPattern("a[0-9]z");
        Assertions.assertTrue(pattern.matches("a2z"));
        Assertions.assertFalse(pattern.matches("a23z"));
        Assertions.assertFalse(pattern.matches("az"));
        }

    @Test
    void alphaRangeMatch()
        {
        GlobPattern pattern = new GlobPattern("1[a-z]9");
        Assertions.assertTrue(pattern.matches("1a9"));
        Assertions.assertTrue(pattern.matches("1q9"));
        Assertions.assertTrue(pattern.matches("1z9"));
        Assertions.assertFalse(pattern.matches("1aa9"));
        Assertions.assertFalse(pattern.matches("19"));
        }

    @Test
    void alphanumericRangeMatch()
        {
        GlobPattern pattern = new GlobPattern("$[a-zA-Z0-9]$");
        Assertions.assertTrue(pattern.matches("$a$"));
        Assertions.assertTrue(pattern.matches("$Z$"));
        Assertions.assertTrue(pattern.matches("$0$"));
        Assertions.assertFalse(pattern.matches("$aZ$"));
        Assertions.assertFalse(pattern.matches("$a0$"));
        Assertions.assertFalse(pattern.matches("$$"));
        }
    }


