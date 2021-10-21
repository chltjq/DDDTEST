package com.lguplus.ukids.admin.exception.advice;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lguplus.ukids.admin.constants.ResponseEntityConstants;
import com.lguplus.ukids.admin.constants.StatusCodeConstants;
import com.lguplus.ukids.admin.constants.ValidatorCodeConstants;
import com.lguplus.ukids.admin.exception.BusinessException;
import com.lguplus.ukids.admin.exception.RestApiException;
import com.lguplus.ukids.admin.exception.SystemException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class DefaultExceptionAdvice {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<Object> handleException(final BusinessException businessException) {
        Map<String, Object> result = new HashMap<>();
        result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
        result.put(ResponseEntityConstants.STATUS_CODE, businessException.getStatusCode());
        return new ResponseEntity<>(result, OK);
    }

    @ExceptionHandler(SystemException.class)
    protected ResponseEntity<Object> handleException(final SystemException systemException) {
        Map<String, Object> result = new HashMap<>();
        result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
        result.put(ResponseEntityConstants.STATUS_CODE, systemException.getStatusCode());

        LOGGER.error(systemException.getMessage());
        return new ResponseEntity<>(result, OK);
    }

    @ExceptionHandler(RestApiException.class)
    protected ResponseEntity<Object> handleException(final RestApiException restApiException) {
        Map<String, Object> result = new HashMap<>();
        result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
        result.put(ResponseEntityConstants.STATUS_CODE, restApiException.getHttpStatus());
        result.put(ResponseEntityConstants.ERROR_MESSAGE, restApiException.getMessage());
        return new ResponseEntity<>(result, restApiException.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(final Exception exception) {
        Map<String, Object> result = new HashMap<>();
        ResponseEntity<Object> ret = null;

        if (exception instanceof BusinessException) {
            BusinessException businessException = (BusinessException) exception;
            result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
            result.put(ResponseEntityConstants.STATUS_CODE, businessException.getStatusCode());
            result.put(ResponseEntityConstants.ERROR_MESSAGE, businessException.getMessage());

            ret = new ResponseEntity<>(result, EXPECTATION_FAILED);
        } else if (exception instanceof SystemException) {
            SystemException systemException = (SystemException) exception;
            result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
            result.put(ResponseEntityConstants.STATUS_CODE, systemException.getStatusCode());
            ret = new ResponseEntity<>(result, EXPECTATION_FAILED);

            LOGGER.error(systemException.getMessage());
        } else if (exception instanceof MissingServletRequestParameterException) {
            result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
            result.put(ResponseEntityConstants.STATUS_CODE, StatusCodeConstants.MANDATORY_PARAM_ERR);
            result.put(ResponseEntityConstants.MANDATORY_PARAM_ERR_KEY,
                    ((MissingServletRequestParameterException) exception).getParameterName());
            ret = new ResponseEntity<>(result, EXPECTATION_FAILED);
        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;

            result = handleMethodArgumentNotValidException(methodArgumentNotValidException);

            result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
            ret = new ResponseEntity<>(result, EXPECTATION_FAILED);
        } else {
            result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
            result.put(ResponseEntityConstants.STATUS_CODE, StatusCodeConstants.INTERNAL_SERVER_ERROR);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "*");
            headers.add("Access-Control-Allow-Credentials", "true");
            headers.add("Access-Control-Expose-Headers", "*");
            ret = new ResponseEntity<>(result, headers, OK);

            LOGGER.error(exception.getMessage());
        }

        return ret;
    }

    @ResponseStatus(code = NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<Object> handleException(NoHandlerFoundException notFoundException) {
        Map<String, Object> result = new HashMap<>();
        result.put(ResponseEntityConstants.SUCCESS_OR_NOT, ResponseEntityConstants.SUCCESS_NO_FLAG);
        return new ResponseEntity<>(result, OK);
    }

    private Map<String, Object> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        Map<String, Object> result = new HashMap<>();

        List<FieldError> fieldErrorList = exception.getBindingResult().getFieldErrors();

        List<FieldError> mandatoryFieldErrors = new ArrayList<>();
        List<FieldError> lengthFieldErrors = new ArrayList<>();
        List<FieldError> patternFieldErrors = new ArrayList<>();
        for (final FieldError element : fieldErrorList) {
            if (element.getCode().equals(ValidatorCodeConstants.NOT_BLANK)
                    || element.getCode().equals(ValidatorCodeConstants.NOT_EMPTY)
                    || element.getCode().equals(ValidatorCodeConstants.NOT_NULL)) {
                mandatoryFieldErrors.add(element);

            } else if (element.getCode().equals(ValidatorCodeConstants.SIZE)) {
                lengthFieldErrors.add(element);

            } else if (element.getCode().equals(ValidatorCodeConstants.PATTERN)) {
                patternFieldErrors.add(element);
            }
        }

        if (!mandatoryFieldErrors.isEmpty()) {
            result.put(ResponseEntityConstants.STATUS_CODE, StatusCodeConstants.MANDATORY_PARAM_ERR);
            result.put(ResponseEntityConstants.MANDATORY_PARAM_ERR_KEY, mandatoryFieldErrors.get(0).getField());
        } else if (!lengthFieldErrors.isEmpty()) {
            result.put(ResponseEntityConstants.STATUS_CODE, StatusCodeConstants.PARAM_LENGTH_ERR);
            result.put(ResponseEntityConstants.PARAM_LENGTH_ERR_KEY, lengthFieldErrors.get(0).getField());
        } else if (!patternFieldErrors.isEmpty()) {
            result.put(ResponseEntityConstants.STATUS_CODE, StatusCodeConstants.PARAM_PATTERN_ERR);
            result.put(ResponseEntityConstants.PARAM_LENGTH_ERR_KEY, patternFieldErrors.get(0).getField());
        }

        return result;
    }
}
