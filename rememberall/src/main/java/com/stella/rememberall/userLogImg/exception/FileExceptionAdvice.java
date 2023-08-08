package com.stella.rememberall.userLogImg.exception;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.stella.rememberall.common.response.ErrorEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class FileExceptionAdvice {
    @ExceptionHandler(EmptyFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity emptyFileException(EmptyFileException e) {
        log.error("Empty File Exception({}) - {}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorEntity emptyFileException(MultipartException e) {
        String errorName = e.getClass().getSimpleName();
        String errorMsg = e.getMessage();
        log.error("MultiFile Exception({}) - {}", errorName, errorMsg);
        return new ErrorEntity(errorName, errorMsg);
    }

    @ExceptionHandler({S3FileException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorEntity s3FileException(S3FileException e) {
        log.error("S3 File Exception({}) - {}", e.getErrorCode(), e.getErrorMessage());
        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage());
    }

//    @ExceptionHandler({AmazonS3Exception.class})
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorEntity amazoneS3Exception(AmazonS3Exception e) {
//        log.error("Amazon S3 Exception({}) - {}", e.getErrorCode(), e.getErrorMessage());
//        return new ErrorEntity(e.getErrorCode().toString(), e.getErrorMessage());
//    }
}
