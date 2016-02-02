var MuseResourceType = STEP_RESOURCE;   // required for a Javascript step.

function executeStep()
    {
    var var_in = getLocalVariable('var_in');

    if (var_in !== 'input')
        return failureResult("var_in check failed. var_in=" + var_in);

    setLocalVariable('var_out', 'output');

    return successResult("input checks out. Setting var_out=output");
    }
