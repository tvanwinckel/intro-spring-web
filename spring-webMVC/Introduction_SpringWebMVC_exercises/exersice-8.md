# Exercise 8: Controller Advices

Given the CurrencyController from previous exercises, we'd like to move the exception handling into a specific class (ControllerAdvice) to bundle specific exception handling instead of spreading acros multiple controllers.

## Extra

Create a new controller or add a new endpoint to a different existing controller that throws an exception (can be anything for example IllegalArgumentException). Add the exception handling to the advice you just made.

See if you can configure the advice to:

* Handle all exceptions thrown in all controllers.
* Only handle exception thrown in specific controllers.
