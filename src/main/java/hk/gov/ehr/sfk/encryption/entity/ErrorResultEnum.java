package hk.gov.ehr.sfk.encryption.entity;
/**
 * @author yunzh
 * @Usage: ErrorMessage
 */
public enum ErrorResultEnum {
    //user input is error
    PARAMETER_ERROR(400,4000),
    //user input exceeds maximum number
    PARAMETER_OUT_BOUNDARY(400,4100),
    //decryption/unscramble error
    INTERNAL_SERVER_ERROR(500,5000),
    //database access error
    DATABASE_ACCESS_ERROR (500,5200),
    //get data from database error
    DATABASE_GET_DATA_ERROR(500,5201),
    UNEXPECTED_ERROR(500,9999);

    public int getHttpStatus() {
        return httpStatus;
    }

    public int getStatusCode() {
        return statusCode;
    }
    private final int httpStatus;
    private final int statusCode;

    ErrorResultEnum(int httpStatus,int statusCode){
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }

}
