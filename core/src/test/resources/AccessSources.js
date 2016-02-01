var MuseResourceType = STEP_RESOURCE;   // required for a Javascript step.

function executeStep(context, params)
    {
    var param = params.get("named_source")
        ;
    if (param !== 'named_value')
        return failureResult("named_source check failed. named_value=" + param);

    return successResult("success!");
    }
