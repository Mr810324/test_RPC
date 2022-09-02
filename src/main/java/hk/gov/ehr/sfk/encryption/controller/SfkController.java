package hk.gov.ehr.sfk.encryption.controller;


import com.google.gson.GsonBuilder;
import hk.gov.ehr.sfk.encryption.config.SfkConfig;
import hk.gov.ehr.sfk.encryption.entity.ErrorResultEnum;
import hk.gov.ehr.sfk.encryption.entity.SfkQuery;
import hk.gov.ehr.sfk.encryption.entity.ResultDTO;
import hk.gov.ehr.sfk.encryption.exception.ApiException;
import hk.gov.ehr.sfk.encryption.service.SfkService;
import hk.gov.ehr.sfk.encryption.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;


@RestController
@RequestMapping(value = "/protected/cses/sfk", produces = MediaType.APPLICATION_JSON_VALUE)
public class SfkController {

    @Autowired
    SfkService sfkService;
    @Autowired
    SystemProperties sysPro;
    @Autowired
    SfkConfig sfkConfig;

    @PostMapping("/testDecryptError")
    public void testDecryptError(@RequestBody String string1) throws Exception {
        sfkService.testDecrypt(string1);
    }
    @PostMapping("/testParam")
    public void testParam(){

    }

    @PostMapping("/testBinder1")
    public String getSfkQuery1(@Valid @RequestBody SfkQuery sfkQuery) throws ParseException, UnsupportedEncodingException, MethodArgumentNotValidException {
        System.out.println(new GsonBuilder().serializeNulls().disableHtmlEscaping().create().toJson(sfkQuery));
        sfkQuery.checkPassAlongInfo();
        System.out.println(sfkQuery.getConsumerMajVer());
        return "success1";
    }

    @PostMapping("/encryption/getEhrNoEncryption")
    public ResultDTO getEhrNoEncryption(@RequestBody @Valid SfkQuery sfkQuery) throws Exception {
        System.out.println("Now is run getEhrNoEncryption");
        char[][] srcChaAry = sysPro.convertStringArrayToCharArray(sfkQuery.getSrcStrAry());
        ResultDTO result = sfkService.encrypt(srcChaAry);
        return result;

    }

    @PostMapping("/encryption/getEhrNoDecryption")
    public ResultDTO getEhrNoDecryption(@RequestBody @Valid SfkQuery sfkQuery) throws Exception {
        System.out.println("Now is run getEhrNoDecryption");
        sfkQuery.checkSrcStrArySize(sfkConfig.getMaxArraySize());
        char[][] srcChaAry = sysPro.convertStringArrayToCharArray(sfkQuery.getSrcStrAry());
        try {
            ResultDTO result = sfkService.decrypt(srcChaAry);
            return result;
        } catch (IllegalArgumentException | IllegalBlockSizeException | BadPaddingException e) {
            throw new ApiException(ErrorResultEnum.INTERNAL_SERVER_ERROR, "getEhrNoDecryption failed, " + e.getClass() + ":" + e.getMessage(),e);
        }
    }
    @PostMapping("/scramble/getEhrNoScramble")
    public ResultDTO getEhrNoScramble(@RequestBody @Valid SfkQuery sfkQuery) throws Exception {
        System.out.println("Now is run getEhrNoScramble");
        sfkQuery.checkSrcStrArySize(sfkConfig.getMaxArraySize());
        char[][] srcChaAry = sysPro.convertStringArrayToCharArray(sfkQuery.getSrcStrAry());
        ResultDTO result = sfkService.scramble(srcChaAry);
        return result;
    }

    @PostMapping("/scramble/getEhrNoUnscramble")
    public ResultDTO getEhrNoUnscramble(@RequestBody @Valid SfkQuery sfkQuery) throws Exception {
        System.out.println("Now is run getEhrNoUnscramble");
        sfkQuery.checkSrcStrArySize(sfkConfig.getMaxArraySize());
        char[][] srcChaAry = sysPro.convertStringArrayToCharArray(sfkQuery.getSrcStrAry());
        try {
            ResultDTO result = sfkService.unscramble(srcChaAry);
            return result;
        } catch (IllegalArgumentException | IllegalBlockSizeException | BadPaddingException e) {
            throw new ApiException(ErrorResultEnum.PARAMETER_ERROR, "unscramble failed " + e.getClass() + ":" + e.getMessage(),e);
        }
    }



}
