var MuseResourceType = STEP_RESOURCE;   // required for a Javascript step.

function executeStep(context, params)
    {
    logMessage(context, "test message");
    return successResult();
    }
