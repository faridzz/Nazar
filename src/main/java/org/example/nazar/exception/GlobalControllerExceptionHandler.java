package org.example.nazar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(DuplicateProductFoundException.class)
    public ResponseEntity<String> handleDuplicateProductFoundException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("محصول تکراری است");
    }

    @ExceptionHandler(ProductTypeNotFoundException.class)
    public ResponseEntity<String> handleProductTypeNotFoundException() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("این تایپ وجود ندارد");
    }

    @ExceptionHandler(DuplicateTypeException.class)
    public ResponseEntity<String> handleDuplicateTypeException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("تایپ ورودی تکراری است ");
    }

    @ExceptionHandler(DuplicateSiteException.class)
    public ResponseEntity<String> handleDuplicateSiteException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("سایت ورودی تکراری است ");
    }

    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<String> handleDuplicateUserException() {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("یوزر نیم ورودی تکراری است ");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // مدیریت ارورهای عمومی کنترلر
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleOtherException(Exception e, WebRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage() + req);
    }
}
