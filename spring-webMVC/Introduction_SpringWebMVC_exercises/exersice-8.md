Exercise 8:
We are again given a controller that is making use of an exception service. This time the controller already exception
handling going on.

Rework the existing controller, allowing it to have external instead internal exception handling using a
ControllerAdvice.

###Extra:

Move the existing controller in a separate package (for example /exercise_8/first-controller).
Create an additional controller who's method(s) return an exception as well, add this controller to its own
package (for example /exercise_8/second-controller)
Play around with the ControllerAdvice to only handle exceptions thrown from:

only the first controller
only the second controller
all controllers

```java
@ControllerAdvice(basePackages = "com.axxes.SpringMvc.exercise_8")
//@ControllerAdvice(basePackages = "com.axxes.SpringMvc.exercise_8.first_controller")
//@ControllerAdvice(basePackages = "com.axxes.SpringMvc.exercise_8.second_controller")
public class ExceptionAdvice {


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleAll(final Exception e) {
        final ModelAndView view = new ModelAndView("basicView");
        view.addObject("message", "Oops! Something went wrong, try again later.");
        return view;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleSpecific(final Exception e) {
        final ModelAndView view = new ModelAndView("basicView");
        view.addObject("message", "Oops! Something went wrong, an IllegalArgument was given.");
        return view;
    }
}

```