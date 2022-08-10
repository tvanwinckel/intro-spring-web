Exercise 7:

We are given a controller which is making use of an exception service. Due to the nature of this service a lot
of exceptions are thrown. We would like for all these exceptions to be handled in the 'background' causing users not to
be confronted with our 'internal' exceptions.

    - All types of exceptions should be handled by a general exception handler.
    - If an exception is handled, inform the user something went wrong. You can use the 'basicView' we have been using
      for previous exercises or try to create your own.
    - IllegalArgumentExceptions should be handled by a specific exception handler.


Extra:
    - When you return a view to the user, send along a http 500 code instead of the default 200.
    (Might require you to you a ResponseEntity)

```java
@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(final Exception e) {
        // When using a ResponseEntity it is not possible to return a Model, ModelMap or ModelAndView.
        return new ResponseEntity<>("Oops! Something went wrong, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleSpecific(final Exception e) {
        final ModelAndView view = new ModelAndView("basicView");
        view.addObject("message", "Oops! Something went wrong, an IllegalArgument was given.");
        return view;
    }

```