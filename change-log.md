# Muse Change Log

## 0.6
- Minor changes in support of the first IDE release
- switch to LogBack (from slf4j-simple) for logging diagnostic. Log to a file instead of console ($HOME/.muse/muse.log) 
- enhance: Support relative and absolute paths for local driver providers
- enhance: Support OS-specific configurations for local driver providers

## 0.5
- refactor: resource lookup from project return ResourceTokens, instead of the resources. The tokens may then be used to fetch the resource.
- add: ElementTextValueSource and elementText() expression
- refactor: replace ResourceMetadata with ResourceInfo implemented by resources

## 0.4
- add: 'Map' subsource type (and configure for CallFunction parameters)
- add: ElementSelectedValueSource and elementSelected() expression
- enhance: remove "amount" parameter from inline quick editor on IncrementVariable step
- enhance: browser providers that require path to executable will report missing parameter and missing file path
- refactor: MuseTestResult adds .isPassed() and .getFailureDescription(), looses .getTestResultStatus(). MuseTestResultStatus replaced with MuseTestFailureDescription.
- refactor: implement result evaluation logic as an event listener (pull out of executor)
- refactor: implement stop-test-on-error logic as an event listener (pull out of executor logic)
- refactor: pull step execution logic into StepExecutor, separate from SteppedTestExecutor
- implement EventMatcher
- refactor: MuseTestResult
- refactor: re-work how the test context is initialized. Separate into individual components (ContextInitializers)
- add: ContextInitializerConfigurations for controlling which VariableLists are applied 
- add: ListContainsSource
- add: PropertySource and PropertyResolver support classes, and support for object.property string expressions
- add: SystemVariableSource and $$varname expression, SystemVariableProvider and dynamic implementation lookup
- add: environment and test system variables
- add: varlistid system variable for context initializers
- add: commandlineoption (clo) system variable

## 0.3

- add: Import Back and Refresh steps from SeleniumIDE
- add: Import Select by name and Select by label steps from SeleniumIDE
- add: ValueSourceChangeListener interface and ValueSourceChangeObserver convenience class
- add: @MuseSubsourceDescriptor annotation (and SubsourceDescriptor class) for describing the expected parameters of a Value Source or Step
- add: add annotations with source descriptions for the builtin steps (for both UI and command-line help) 
- refactor: MuseValueSource.resolveValue() throws ValueSourceResolutionError instead of StepConfigurationError
- upgrade to Gradle 2.13
- upgrade to Selenium 2.53.1

## 0.2

- add: ElementEnabledValueSource and elementEnabled() expression
- add: NotValueSource and not() expression
- add: ExecuteJavascript step (Run script in browser)
- add: DateFormatValueSource
- add: NavigateForward, NavigateBack and RefreshPage steps
- fix: ProjectResourceValueSource referencing a non-existent step fails with IllegalArgumentException (instead of the correct type)
- fix: empty compound step fails (but should simply no-op)
- upgrade to Selenium 2.53.0

## 0.1

- initial release