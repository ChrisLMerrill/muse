package org.musetest.core.variables;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public enum VariableScope
    {
    Local,      // local to the current execution unit. This is somewhat subjective depending on the execution implementation. For a SteppedTest, it is clearly defined by the StepExecutionStack.
    Execution   // this scope applies to the logical execution unit (e.g. a test).
    //Initial     // this scope is for initializers that run at the beginning of an execution.
    }
