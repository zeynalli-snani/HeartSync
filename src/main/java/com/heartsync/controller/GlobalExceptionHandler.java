package com.heartsync.controller;

import com.heartsync.exception.InvalidInputException;
import com.heartsync.exception.ResourceNotFoundException;
import com.heartsync.exception.UnauthorizedAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("error", "Not Found");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidInputException.class)
    public String handleInvalidInput(InvalidInputException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("error", "Invalid Input");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public String handleUnauthorized(UnauthorizedAccessException ex, Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("error", "Access Denied");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(DateTimeParseException.class)
    public String handleDateTimeParse(DateTimeParseException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("error", "Invalid Time");
        model.addAttribute("message", "Please enter a valid time for the stop.");
        return "error";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("error", "Missing Field");
        model.addAttribute("message", "A required field was missing: " + ex.getParameterName());
        return "error";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("error", "Invalid Value");
        model.addAttribute("message", "Invalid value provided for: " + ex.getName());
        return "error";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResource(NoResourceFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("error", "Page Not Found");
        model.addAttribute("message", "The page you're looking for doesn't exist.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model) {
//        ex.printStackTrace();
        model.addAttribute("status", 500);
        model.addAttribute("error", "Something went wrong");
        model.addAttribute("message", "An unexpected error occurred. Please try again.");
        return "error";
    }
}