package org.museautomation.core.task.state;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskInputStates
    {
    public TaskInputStates()
        {
        }

    public TaskInputStates(String... ids)
        {
        Collections.addAll(_id_list, ids);
        }

    public List<String> getIdList()
        {
        return _id_list;
        }

    public void setIdList(List<String> list)
        {
        _id_list = list;
        }

    private List<String> _id_list = new ArrayList<>();
    }