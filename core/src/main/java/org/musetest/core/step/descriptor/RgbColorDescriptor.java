package org.musetest.core.step.descriptor;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class RgbColorDescriptor extends ColorDescriptor
    {
    public RgbColorDescriptor(float red, float green, float blue)
        {
        _red = red;
        _green = green;
        _blue = blue;
        }

    public float _red;
    public float _green;
    public float _blue;

    public final static ColorDescriptor RED = new RgbColorDescriptor(1.0f, 0.0f, 0.0f);
    public final static ColorDescriptor GREEN = new RgbColorDescriptor(0.0f, 1.0f, 0.0f);
    public final static ColorDescriptor BLACK = new RgbColorDescriptor(0.0f, 0.0f, 0.0f);
    }