package com.wanderrful.baewoo.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * When we throw an exception, Spring will use this class to translate that
 *  exception into something it can return to the user.
 */
@ControllerAdvice
class ExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(NotImplementedError::class)
    fun notImplemented() {
        // Intentionally left empty
    }

}