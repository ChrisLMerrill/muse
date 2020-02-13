package org.museautomation.core.step.descriptor;

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

    @Override
    public boolean equals(Object obj)
        {
        if (obj instanceof RgbColorDescriptor)
            {
            RgbColorDescriptor other = (RgbColorDescriptor) obj;
            return other._red == _red && other._blue == _blue && other._green == _green;
            }
        return false;
        }

    @Override
    public int hashCode()
        {
        return ((int)(1000*_red)) * ((int)(1000*_blue)) * ((int)(1000*_green));
        }

    public float _red;
    public float _green;
    public float _blue;

    public final static ColorDescriptor RED = new RgbColorDescriptor(1.0f, 0.0f, 0.0f);
    public final static ColorDescriptor GREEN = new RgbColorDescriptor(0.0f, 1.0f, 0.0f);
    public final static ColorDescriptor BLACK = new RgbColorDescriptor(0.0f, 0.0f, 0.0f);
    }