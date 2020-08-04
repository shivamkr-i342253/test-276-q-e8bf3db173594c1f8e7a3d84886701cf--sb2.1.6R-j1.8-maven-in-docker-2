package org.codejudge.sb.controller;

import org.codejudge.sb.model.BadRequestResponse;
import org.codejudge.sb.service.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BadRequestControllerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestResponse handleBadRequestException(BadRequestException badRequestException) {
        return new BadRequestResponse(badRequestException.getStatusMsg(), badRequestException.getResponseMsg());
    }
}
