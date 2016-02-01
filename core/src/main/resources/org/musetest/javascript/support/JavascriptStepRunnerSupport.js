/**
 * author: Christopher L Merrill (see LICENSE.txt for license details)
 */

STEP_RESOURCE = 'jsStep';   // declares the MuseResourceType for a Javascript step.

var StepExecutionStatus = Java.type("org.musetest.core.step.StepExecutionStatus");
var BasicStepExecutionResult = Java.type("org.musetest.core.step.BasicStepExecutionResult");
var StepExecutionContext = Java.type("org.musetest.core.context.StepExecutionContext");
var MessageEvent = Java.type("org.musetest.core.events.MessageEvent");

var RESULT_COMPLETE = new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
var RESULT_FAILURE = new BasicStepExecutionResult(StepExecutionStatus.FAILURE);
var RESULT_INCOMPLETE = new BasicStepExecutionResult(StepExecutionStatus.INCOMPLETE);

function executeStepWrapper(context, params)
    {
    var js_context = {};
    js_context.getLocalVariable = function(name)
        {
        return context.getLocalVariable(name);
        };
    js_context.getLocalVariable = function(name)
        {
        return context.getLocalVariable(name);
        };

    executeStep(js_context);
    }

function successResult(message)
    {
    return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, message);
    }

function failureResult(message)
    {
    return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, message);
    }

function logMessage(context, message)
    {
    context.getTestExecutionContext().raiseEvent(new MessageEvent(message));
    }