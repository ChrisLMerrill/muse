package org.museautomation.core.steptask;

import org.museautomation.core.context.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepExecutionContextStack
    {
    public StepExecutionContext peek()
        {
        return _stack.peek();
        }

    public StepExecutionContext pop()
        {
        return _stack.pop();
        }

    public void push(StepExecutionContext context)
        {
        _stack.push(context);
        }

    public Iterator<StepExecutionContext> iterator()
        {
        return _stack.iterator();
        }

    public boolean hasMoreSteps()
        {
        Iterator<StepExecutionContext> iterator = iterator();
        while (iterator.hasNext())
            if (iterator.next().hasStepToExecute())
                return true;
        return false;
        }

    private LinkedList<StepExecutionContext> _stack = new LinkedList<>();
    }


