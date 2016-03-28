# Muse Change Log

## 0.2

- add: ElementEnabledValueSource and elementEnabled() expression
- add: NotValueSource and not() expression
- add: ExecuteJavascript step (Run script in browser)
- add: DateFormatValueSource
- fix: ProjectResourceValueSource referencing a non-existent step fails with IllegalArgumentException (instead of the correct type)
- fix: empty compound step fails (but should simply no-op)
- upgrade to Selenium 2.53.0

## 0.1

- initial release