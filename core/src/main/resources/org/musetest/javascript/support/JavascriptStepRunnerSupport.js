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

function successResult(message)
    {
    return new BasicStepExecutionResult(RESULT_COMPLETE, message);
    }

function failureResult(message)
    {
    return new BasicStepExecutionResult(RESULT_FAILURE, message);
    }

function logMessage(context, message)
    {
    context.getTestExecutionContext().raiseEvent(new MessageEvent(message));
    }