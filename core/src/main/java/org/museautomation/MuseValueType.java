package org.museautomation;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseValueType
    {
    String getId();
    String getName();
    boolean isInstance(Object obj);
    }