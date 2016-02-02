/**
 * author: Christopher L Merrill (see LICENSE.txt for license details)
 */

STEP_RESOURCE = 'jsStep';   // declares the MuseResourceType for a Javascript step.

var StepExecutionStatus = Java.type("org.musetest.core.step.StepExecutionStatus");
var BasicStepExecutionResult = Java.type("org.musetest.core.step.BasicStepExecutionResult");
var StepExecutionContext = Java.type("org.musetest.core.context.StepExecutionContext");
var MessageEvent = Java.type("org.musetest.core.events.MessageEvent");
var System = Java.type('java.lang.System');

function successResult(message)
    {
    return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, message);
    }

function failureResult(message)
    {
    return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, message);
    }

function getLocalVariable(name)
    {
    var value = __context.getLocalVariable(name);
    logMessage("script: getting " + name + "=" + value + " (" + typeof value + ")");
    return value;
    }

function setLocalVariable(name, value)
    {
    logMessage("script: setting " + name + "=" + value + " (" + typeof value + ")");
    __context.setLocalVariable(name, value);
    }

function logMessage(message)
    {
    System.out.println(message);
    __context.getTestExecutionContext().raiseEvent(new MessageEvent(message));
    }