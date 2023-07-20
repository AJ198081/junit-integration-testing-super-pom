package dev.aj.app.exception_handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleMethodArgumentTypeMismatchException(HttpServletRequest request, Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }

}
