package org.musetest.core;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public enum MuseEventType
    {
    StartTest,
    EndTest,
    TestExecutionError,
    ScriptError,
    StartStep,
    EndStep,
    ConditionEvaluated,
    VerifyFailed,
    ValueResolved,
    SetVariable,
    Pause,
    Resume,
    DynamicStepLoad,
    Message,
    Interrupted
    }
