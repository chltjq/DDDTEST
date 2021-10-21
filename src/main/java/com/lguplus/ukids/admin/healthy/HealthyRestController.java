package com.lguplus.ukids.admin.healthy;

import static org.springframework.http.HttpStatus.OK;

import com.lguplus.ukids.admin.exception.BusinessException;
import com.lguplus.ukids.admin.exception.SystemException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class HealthyRestController {
    @Operation(summary = "health check", description = "health check")
    @GetMapping(path = "/healthy", produces = "application/json")
    public ResponseEntity<String> retrieveHealthy() throws Exception {
        try {
            return new ResponseEntity<>("{\"status\": \"UP\"}", OK);
        } catch (RuntimeException exception) {
            throw new BusinessException(exception.getMessage(), HttpStatus.CONFLICT.toString());
        } catch (Exception exception) {
            throw new SystemException(exception.getMessage());
        }
    }
}
