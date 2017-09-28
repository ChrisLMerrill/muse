package org.musetest.core.context.initializers;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VariableListContextInitializerConfiguration
    {
    @SuppressWarnings("WeakerAccess") // public API
    public ValueSourceConfiguration getListId()
        {
        return _list_id;
        }

    public void setListId(ValueSourceConfiguration list_id)
        {
        _list_id = list_id;
        }

    @SuppressWarnings("WeakerAccess") // public API
    public ValueSourceConfiguration getIncludeCondition()
        {
        return _include_condition;
        }

    public void setIncludeCondition(ValueSourceConfiguration include_condition)
        {
        _include_condition = include_condition;
        }

    private ValueSourceConfiguration _list_id;
    private ValueSourceConfiguration _include_condition;
    }


