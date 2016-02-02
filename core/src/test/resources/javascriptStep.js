var MuseResourceType = 'jsStep';   // required for a Javascript step.

function executeStep(context)
    {
    return successResult();
    }

function getStepDescriptor()
    {
    var descriptor = {};
    descriptor.name = 'JS Example';
    descriptor.group = 'javascript';
    descriptor.icon = 'glyph:FontAwesome:PAW';
    descriptor.shortDescription = 'A Javascript step';
    descriptor.longDescription = 'The long description of the javascript step';
    return descriptor;
    }