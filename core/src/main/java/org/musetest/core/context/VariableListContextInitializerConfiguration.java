package org.musetest.core.context;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableListContextInitializerConfiguration
    {
    public String getVariableListId()
        {
        return _varlist_id;
        }

    public void setVariableListId(String varlist_id)
        {
        _varlist_id = varlist_id;
        }

    public ValueSourceConfiguration getIncludeCondition()
        {
        return _include_condition;
        }

    public void setIncludeCondition(ValueSourceConfiguration include_condition)
        {
        _include_condition = include_condition;
        }

    private String _varlist_id;
    private ValueSourceConfiguration _include_condition;
    }


