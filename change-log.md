# Muse Change Log

## 0.9
- update dependencies to latest versions, including Selenium 3.0.1.
- add: IdListTestSuite supports ids of test suites
- add: support for PhantomJS driver
- add: step SetBrowserSize
- add: ElementHasClassCondition and expression support for elementHasClass(<element>, "class")
- add: TestConfiguration gets a name() property.
- add: TestResult gets name and configuration properties
- add: ParameterListTestSuite adds parameters to name of test configuration when it generates them
- add: TestSuiteRunners add the TestConfiguration to the TestResult.
- add: JUnitReportRenderer uses the TestConfiguration name as the test name
- add: JUnitReportRenderer uses the TestSuite id as the class name
- fix: OpenBrowser step should not propogate an exception during test resource cleanup if WebDriver.quit() throws an exception.
- fix: BaseMuseTest always returns a result, even when a serious exception is thrown.
- fix: encode jUnit report XML in UTF-8
- fix: HTML-escape the test failure message in jUnit report
- fix: run VariableListContextInitializers in the order they appear in the config

## 0.8
- add: RandomNumberValueSource and random(min,max) expression support.
- fix: Implement createResource() for Macro
- fix: Ensure .json and .csv files are closed after reading by the responsible resource factories.
- add: Verify step adds (optional) parameter to terminate test on failure
- add: Support verifyElementPresent and assertElementPresent commands in SeleniumIDE test import
- add: Support verifyText and assertText commands in SeleniumIDE test import
- add: Support glob: exact: and regexp: matchers in verifyText and assertText steps imported from SeleniumIDE
- add: globMatch and regexMatch value source and corresponding string expressions
- add: 'clear' parameter to the SendKeys command - an optional parameter which, if true, causes the field to be cleared before keystrokes are sent.
- enhance: The SeleniumIDE import now accepts the TYPE command as well as SENDKEYS. Both convert to the SendKeys Muse step, but converting the TYPE command, the 'clear' parameter will be set to true.
- add: low-level support for colored step icons (and implement Red for unknown step icon).
- add: support for ValueConverters in SeleniumIDE test import
- add: support for variable references in SeleniumIDE test import. E.g. ${value1}
- add: support for store command in SeleniumIDE test import
- add: EvaluateJavascript value source
- add: support for javascript{...} parameters in SeleniumIDE test import

## 0.7
- add: Change listeners on WebDriverProviderList
- add: Name property on WebDriverProvider
- add: Support resource sub-types (e.g. different types of MuseTestSuite)
- add: DataTable resource type and CSV implementation
- add: optional parameters for the wait step (control wait duration and fail behavior)
- add: Wait for N seconds step
- add: import pause step from SeleniumIDE
- fix: Stopping the InteractiveTestRunner now interrupts the thread (thus, stopping immediately)

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