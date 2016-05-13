# Muse Change Log

## 0.3

- add: Import Back and Refresh steps from SeleniumIDE
- add: Import Select by name and Select by label steps from SeleniumIDE
- add: ValueSourceChangeListener interface and ValueSourceChangeObserver convenience class
- refactor: MuseValueSource.resolveValue() throws ValueSourceResolutionError instead of StepConfigurationError

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