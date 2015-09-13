package org.musetest.core.resource.origin;

import org.musetest.core.resource.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StringResourceOrigin implements ResourceOrigin
    {
    @Override
    public String getDescription()
        {
        return "String";
        }

    @Override
    public String suggestId()
        {
        return Integer.toString(hashCode());
        }
    }


