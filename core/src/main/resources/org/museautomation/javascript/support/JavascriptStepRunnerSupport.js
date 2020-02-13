/**
 * author: Christopher L Merrill (see LICENSE.txt for license details)
 */

STEP_RESOURCE = 'jsStep';   // declares the MuseResourceType for a Javascript step.

var StepExecutionStatus = Java.type("org.museautomation.core.step.StepExecutionStatus");
var BasicStepExecutionResult = Java.type("org.museautomation.core.step.BasicStepExecutionResult");
var StepExecutionContext = Java.type("org.museautomation.core.context.StepExecutionContext");
var MessageEventType = Java.type("org.museautomation.core.events.MessageEventType");
var System = Java.type('java.lang.System');

function successResult(message)
    {
    return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, message);
    }

function failureResult(message)
    {
    return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, message);
    }

function getVariable(name)
    {
    var value = __context.getVariable(name);
    logMessage("script: getting " + name + "=" + value + " (" + typeof value + ")");
    return value;
    }

function setVariable(name, value)
    {
    logMessage("script: setting " + name + "=" + value + " (" + typeof value + ")");
    __context.setVariable(name, value);
    }

function logMessage(message)
    {
    __context.raiseEvent(MessageEventType.create(message));
    }