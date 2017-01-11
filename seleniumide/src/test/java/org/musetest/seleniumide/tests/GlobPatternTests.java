package org.musetest.seleniumide.tests;

import org.junit.*;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class GlobPatternTests
    {
    @Test
    public void isPattern()
        {
        Assert.assertFalse(new GlobPattern("abc123").hasWildcard());
        Assert.assertTrue(new GlobPattern("abc?123").hasWildcard());
        Assert.assertTrue(new GlobPattern("abc*123").hasWildcard());
        Assert.assertTrue(new GlobPattern("abc[1-3]").hasWildcard());
        }

    @Test
    public void starMatch()
        {
        GlobPattern pattern = new GlobPattern("a*a");
        Assert.assertTrue(pattern.matches("aba"));
        Assert.assertTrue(pattern.matches("abbba"));
        Assert.assertTrue(pattern.matches("aa"));
        Assert.assertFalse(pattern.matches("abc"));
        }

    @Test
    public void questionMatch()
        {
        GlobPattern pattern = new GlobPattern("a?a");
        Assert.assertTrue(pattern.matches("aba"));
        Assert.assertFalse(pattern.matches("abbba"));
        Assert.assertFalse(pattern.matches("aa"));
        }

    @Test
    public void numericRangeMatch()
        {
        GlobPattern pattern = new GlobPattern("a[0-9]z");
        Assert.assertTrue(pattern.matches("a2z"));
        Assert.assertFalse(pattern.matches("a23z"));
        Assert.assertFalse(pattern.matches("az"));
        }

    @Test
    public void alphaRangeMatch()
        {
        GlobPattern pattern = new GlobPattern("1[a-z]9");
        Assert.assertTrue(pattern.matches("1a9"));
        Assert.assertTrue(pattern.matches("1q9"));
        Assert.assertTrue(pattern.matches("1z9"));
        Assert.assertFalse(pattern.matches("1aa9"));
        Assert.assertFalse(pattern.matches("19"));
        }

    @Test
    public void alphanumericRangeMatch()
        {
        GlobPattern pattern = new GlobPattern("$[a-zA-Z0-9]$");
        Assert.assertTrue(pattern.matches("$a$"));
        Assert.assertTrue(pattern.matches("$Z$"));
        Assert.assertTrue(pattern.matches("$0$"));
        Assert.assertFalse(pattern.matches("$aZ$"));
        Assert.assertFalse(pattern.matches("$a0$"));
        Assert.assertFalse(pattern.matches("$$"));
        }
    }


