package com.heartsync.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("error", "Something went wrong");
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex, Model model) {
        model.addAttribute("status", 400);
        model.addAttribute("error", "Bad Request");
        model.addAttribute("message", "Missing required field: " + ex.getParameterName());
        return "error";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNotFound(NoResourceFoundException ex, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("error", "Page Not Found");
        model.addAttribute("message", "The page you're looking for doesn't exist.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("status", 500);
        model.addAttribute("error", "Unexpected Error");
        model.addAttribute("message", "Something went wrong. Please try again.");
        return "error";
    }
}