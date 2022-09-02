
package hk.gov.ehr.sfk.encryption.exception;


import hk.gov.ehr.sfk.encryption.entity.ErrorResultEnum;
import hk.gov.ehr.sfk.encryption.entity.ResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;


/**
 * @author yunzh
 */
@ControllerAdvice
@Order(100)
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HandlerMethod handlerMethod) {

        BindingResult result = e.getBindingResult();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StringBuilder rs = new StringBuilder();
        if (result.hasErrors()) {
            for (ObjectError p : result.getAllErrors()) {
                FieldError fieldError = (FieldError) p;
                rs.append("bad request," + fieldError.getDefaultMessage());
            }
        }
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setStatusCd(4000);
        resultDTO.setStatusMsg(rs.toString());
        return new ResponseEntity<>(resultDTO, status);

    }

    @ExceptionHandler(ApiException.class)
    @ResponseBody
    public ResponseEntity apiExceptionHandler(ApiException e, HandlerMethod handlerMethod) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResultDTO resultDTO = new ResultDTO(e.getStatusCode(), e.getErrorMessage());
        return new ResponseEntity<>(resultDTO, status);

    }

    @ExceptionHandler(CannotGetJdbcConnectionException.class)
    @ResponseBody
    public ResponseEntity cannotGetJdbcConnectionExceptionHandler(CannotGetJdbcConnectionException e, HandlerMethod handlerMethod) {
        HttpStatus status = HttpStatus.resolve(ErrorResultEnum.DATABASE_ACCESS_ERROR.getHttpStatus());
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setStatusCd(ErrorResultEnum.DATABASE_ACCESS_ERROR.getStatusCode());
        resultDTO.setStatusMsg(e.getMessage());
        return new ResponseEntity<>(resultDTO, status);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity exceptionHandler(Exception e, HandlerMethod handlerMethod) {
        HttpStatus status = HttpStatus.resolve(ErrorResultEnum.UNEXPECTED_ERROR.getHttpStatus());
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setStatusCd(ErrorResultEnum.UNEXPECTED_ERROR.getStatusCode());
        resultDTO.setStatusMsg(" Unexpected exception is caught." + e.getClass() + ":" + e.getMessage());
        return new ResponseEntity<>(resultDTO, status);
    }

}

