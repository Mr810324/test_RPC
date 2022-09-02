package hk.gov.ehr.sfk.encryption.exception;

import hk.gov.ehr.sfk.encryption.entity.ErrorResultEnum;

/**
 * @author yunzh
 */
public class ApiException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private Integer statusCode;
    private Integer httpStatus;
    private String errorMessage;

    public Integer getStatusCode() {
        return statusCode;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ApiException(Integer statusCode, Integer httpStatus, String errorMessage,Throwable e) {
        super(errorMessage,e);
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public ApiException(ErrorResultEnum error, String errorMessage,Throwable e) {
        super(errorMessage,e);
        this.statusCode = error.getStatusCode();
        this.httpStatus = error.getHttpStatus();
        this.errorMessage = errorMessage;
    }
    public ApiException(ErrorResultEnum error, String errorMessage) {
        super(errorMessage);
        this.statusCode = error.getStatusCode();
        this.httpStatus = error.getHttpStatus();
        this.errorMessage = errorMessage;
    }
}