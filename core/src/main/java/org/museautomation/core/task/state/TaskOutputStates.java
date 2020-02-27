package org.museautomation.core.task.state;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskOutputStates
    {
    public TaskOutputStates()
        {
        }

    public TaskOutputStates(String... types)
        {
        Collections.addAll(_type_list, types);
        }

    public List<String> getTypeList()
        {
        return _type_list;
        }

    public void setTypeList(List<String> list)
        {
        _type_list = list;
        }

    private List<String> _type_list = new ArrayList<>();
    }