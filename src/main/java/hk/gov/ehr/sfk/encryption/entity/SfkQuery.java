package hk.gov.ehr.sfk.encryption.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.*;
import com.google.gson.stream.MalformedJsonException;
import hk.gov.ehr.sfk.encryption.config.SfkConfig;
import hk.gov.ehr.sfk.encryption.exception.ApiException;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

import static com.google.gson.JsonParser.parseString;

/**
 * @author yunzh
 * @ClassName SfkQuery
 * @Description TODO
 * @date 19:55
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties(value = {})
public class SfkQuery {
    @NotEmpty(message = "srcStrAry can not be null or empty.")
    private String[] srcStrAry;
    @NotBlank(message = "consumerServCd can not be null or blank.")
    private String consumerServCd;
    @NotNull(message = "consumerMajVer can not be null")
    @Range(min = 0, max = 999, message = "The value of consumerMajVer should in [0,999]")
    private Integer consumerMajVer;
    @NotNull(message = "consumerMajVer can not be null")
    @Range(min = 0, max = 999, message = "The value of consumerMinVer should in [0,999]")
    private Integer consumerMinVer;
    @NotBlank(message = "passAlongInfo can not be null or blank")
    private String passAlongInfo;


    public void checkPassAlongInfo() {
        String[] KeysArray = {"CORRELATION_ID", "TXN_CD", "TXN_ID"};
        try {
            JsonObject object = parseString(this.passAlongInfo).getAsJsonObject();
            for (int i = 0; i < KeysArray.length; i++) {
                if (!object.has(KeysArray[i])) {
                    System.out.println("Json String not include : " + KeysArray[i]);
                    throw new ApiException(ErrorResultEnum.PARAMETER_ERROR, "passAlongInfo miss key of " + KeysArray[i]);
                }
            }
        } catch (JsonSyntaxException | IllegalStateException e) {
            throw new ApiException(ErrorResultEnum.PARAMETER_ERROR, "passAlongInfo is not a JSON String.",e);
        }

    }

    public void checkSrcStrArySize(int maxArraySize) {
        if (srcStrAry.length > maxArraySize){
            throw new ApiException(ErrorResultEnum.PARAMETER_ERROR, "The size of input array exceeds limitation");
        }
    }

}
/*
 * srcStrAry,consumerServCd,consumerMajVer,consumerMinVer,passAlongInfo ,????????? null ??? ??????
 * @NotNull ????????? key ??? value ??????????????? value ??? null/""???
 * srcStrAry. @NotEmpty???????????????@NotNull??????????????????null???[](Json????????????{}?)??? ???""???????????????????????? size>0,???????????????????????????
 * consumerServCd,@NotBlank(?????????String????????????null???????????????????????????????????????)
 * consumerMajVer,consumerMinVer ?????????????????????@NotNull(?????????null????????????"")???[0,999].
 * passAlongInfo ,JSON String ,?????? key {"CORRELATION_ID","TXN_CD","TXN_ID"}
 * */