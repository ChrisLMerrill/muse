package org.museautomation.core.util;

/**
 * Extend to provide a better toString() implementation for classes that don't have one.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class Stringifier
    {
    /**
     * Implement this method to return an object that implements toString() as needed.
     * @return null if this Stringifier cannot supply a relevant toString() implementation.
     */
    public abstract Object create(Object target);
    }

