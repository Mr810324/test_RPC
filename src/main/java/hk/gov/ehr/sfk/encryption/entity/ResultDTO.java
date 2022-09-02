package hk.gov.ehr.sfk.encryption.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName resultDTO
 * @Description TODO
 * @date 20:11
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO {
    private int statusCd;
    private String statusMsg;
    private String[] resultStrAry;
    public ResultDTO(int statusCd,String statusMsg){
        this.statusCd = statusCd;
        this.statusMsg = statusMsg;
        this.resultStrAry = new String[]{};
    }
    public ResultDTO(String[] resultStrAry){
        this.statusCd = 1000;
        this.statusMsg = "success";
        this.resultStrAry = resultStrAry;
    }

}
