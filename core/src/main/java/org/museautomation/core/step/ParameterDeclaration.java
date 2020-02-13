package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

/**
 * Describes a parameter recieved by another entity.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("parameter")
public class ParameterDeclaration
    {
    public String _name;
    public boolean _required;
    public ValueSourceConfiguration _default_value;
    public String _description;
    }


