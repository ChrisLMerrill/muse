var MuseResourceType = 'jsStep';   // required for a Javascript step.

var System = Java.type('java.lang.System');

function executeStep(context)
    {
    return STEP_COMPLETE;
    }

function getStepDescriptor()
    {
    var descriptor = {};
    descriptor.name = 'JS Example';
    descriptor.group = 'javascript';
    descriptor.icon = 'glyph:FontAwesome:PAW';
    descriptor.shortDescription = 'A Javascript step';
    return descriptor;
    }