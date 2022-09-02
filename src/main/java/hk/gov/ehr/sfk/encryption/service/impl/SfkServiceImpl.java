package hk.gov.ehr.sfk.encryption.service.impl;


import hk.gov.ehr.sfk.encryption.config.SfkConfig;
import hk.gov.ehr.sfk.encryption.dao.SesRtConfigsRepository;
import hk.gov.ehr.sfk.encryption.entity.ErrorResultEnum;
import hk.gov.ehr.sfk.encryption.entity.SesRtConfigs;
import hk.gov.ehr.sfk.encryption.entity.ResultDTO;
import hk.gov.ehr.sfk.encryption.exception.ApiException;
import hk.gov.ehr.sfk.encryption.service.SfkService;
import hk.gov.ehr.sfk.encryption.util.SfkEncrypt;
import hk.gov.ehr.sfk.encryption.util.SystemProperties;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Collections;
import java.util.List;
import java.util.Base64;


@Service
public class SfkServiceImpl implements SfkService {
    @Autowired
    SesRtConfigsRepository srcRep;
    @Autowired
    SfkEncrypt sfkEncrypt;
    @Autowired
    SystemProperties sysPro;
    @Autowired
    SfkConfig sfkConfig;

    @Override
    public String testDecrypt(String string1) throws Exception {
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        if (CollectionUtils.isEmpty(listSesRtConfigs)) {
            throw new ApiException(ErrorResultEnum.DATABASE_GET_DATA_ERROR, "Get Data from DB failed,listSesRtConfigs is empty.");
        }
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
        byte[] cipherBytes = string1.getBytes(StandardCharsets.UTF_8);
        sfkEncrypt.testDecrypt(sesRtConfigs.getAlgorithm(), sesRtConfigs.getMode(), sesRtConfigs.getPadding(),
                    iv, sfkConfig.getEncryptString(), cipherBytes);
        System.out.println("After IllegalBlockSizeException in service.");
        return "success";
    }

    @Override
    public ResultDTO encrypt(char[][] srcChaAry) throws Exception {
        String[] resultStrAry = new String[srcChaAry.length];
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        if (CollectionUtils.isEmpty(listSesRtConfigs)) {
            throw new ApiException(ErrorResultEnum.DATABASE_GET_DATA_ERROR, "Get Data from DB failed,listSesRtConfigs is empty.");
        }
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
        for (int i = 0; i < srcChaAry.length; i++) {
            if (sfkEncrypt.preprocessForEncrypt(i, sfkConfig.getMaxPlainTextLength(), sfkConfig.getMaxArraySize(), srcChaAry[i], resultStrAry)) {
                continue;
            }
            byte[] plainBytes = sysPro.convertCharArrayToByteArray(srcChaAry[i]);
            byte[] cipherBytes = sfkEncrypt.encrypt(sesRtConfigs.getAlgorithm(), sesRtConfigs.getMode(), sesRtConfigs.getPadding(),
                    iv, sesRtConfigs.getEncryptString(), plainBytes);
            resultStrAry[i] = new String(cipherBytes, sysPro.getCharSetName());
        }

        return new ResultDTO(1000,"success",resultStrAry);
    }

    @Override
    public ResultDTO decrypt(char[][] srcChaAry) throws Exception {
        String[] resultStrAry = new String[srcChaAry.length];
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        if (CollectionUtils.isEmpty(listSesRtConfigs)) {
            throw new ApiException(ErrorResultEnum.DATABASE_GET_DATA_ERROR, "Get Data from DB failed,listSesRtConfigs is empty.");
        }
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
        for (int i = 0; i < srcChaAry.length; i++) {
            if (sfkEncrypt.preprocessForEncrypt(i, sfkConfig.getMaxCipherTextLength(), sfkConfig.getMaxArraySize(), srcChaAry[i], resultStrAry)) {
                continue;
            }
            byte[] cipherBytes = sysPro.convertCharArrayToByteArray(srcChaAry[i]);
            byte[] plainBytes = sfkEncrypt.decrypt(sesRtConfigs.getAlgorithm(), sesRtConfigs.getMode(), sesRtConfigs.getPadding(),
                    iv, sesRtConfigs.getEncryptString(), cipherBytes);
            resultStrAry[i] = new String(plainBytes, sysPro.getCharSetName());
        }
        return new ResultDTO(1000,"success",resultStrAry);
    }


    @Override
    public ResultDTO scramble(char[][] srcChaAry) throws Exception {
        String[] resultStrAry = new String[srcChaAry.length];
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        if (CollectionUtils.isEmpty(listSesRtConfigs)) {
            throw new ApiException(ErrorResultEnum.DATABASE_GET_DATA_ERROR, "Get Data from DB failed,listSesRtConfigs is empty.");
        }
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        String logicStr = "12,8#6,5";
        byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
        for (int i = 0; i < srcChaAry.length; i++) {
            if (sfkEncrypt.preprocessForScramble(i, sfkConfig.getMaxPlainTextLength(), srcChaAry[i], resultStrAry)) {
                continue;
            }
            byte[] plainBytes = sysPro.convertCharArrayToByteArray(srcChaAry[i]);
            byte[] swappedBytes = sfkEncrypt.swapBytes(plainBytes, logicStr);
            byte[] scrambledBytes = sfkEncrypt.encrypt(sesRtConfigs.getAlgorithm(), sesRtConfigs.getMode(), sesRtConfigs.getPadding(),
                    iv, sesRtConfigs.getScrambleString(), swappedBytes);
            resultStrAry[i] = new String(scrambledBytes, sysPro.getCharSetName());
        }
        return new ResultDTO(1000,"success",resultStrAry);
    }

    @Override
    public ResultDTO unscramble(char[][] srcChaAry) throws Exception {
        String[] resultStrAry = new String[srcChaAry.length];
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        if (CollectionUtils.isEmpty(listSesRtConfigs)) {
            throw new ApiException(ErrorResultEnum.DATABASE_GET_DATA_ERROR, "Get Data from DB failed,listSesRtConfigs is empty.");
        }
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        String logicStr = "12,8#6,5";
        byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
        for (int i = 0; i < srcChaAry.length; i++) {
            if(sfkEncrypt.preprocessForScramble(i,sfkConfig.getMaxCipherTextLength(),srcChaAry[i],resultStrAry)){
                continue;
            }
            byte[] scrambledBytes = sysPro.convertCharArrayToByteArray(srcChaAry[i]);
            byte[] swappedBytes = sfkEncrypt.decrypt(sesRtConfigs.getAlgorithm(), sesRtConfigs.getMode(),
                    sesRtConfigs.getPadding(), iv, sesRtConfigs.getScrambleString(), scrambledBytes);
            byte[] plainBytes = sfkEncrypt.reverseSwapByte(swappedBytes,logicStr);
            resultStrAry[i] = new String(plainBytes,sysPro.getCharSetName());
        }

        return new ResultDTO(1000,"success",resultStrAry);
    }

/*
    @Override
    public resultDTO scramble(String[] srcStrAry) {
        String[] resultStrAry = new String[srcStrAry.length];
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        String algorithm = sesRtConfigs.getAlgorithm();
        String mode = sesRtConfigs.getMode();
        String padding = sesRtConfigs.getPadding();
        String logicStr = "12,8#6,5";
        try {
            byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
            for (int i = 0; i < srcStrAry.length; i++) {
                String swappedStr = sfkEncrypt.swapString(srcStrAry[i], logicStr);
                byte[] swappedByte = swappedStr.getBytes(sysPro.getCharSetName());
                byte[] scrambledByte = sfkEncrypt.scrambleEncrypt(algorithm, mode, padding, iv, swappedByte);
                resultStrAry[i] = new String(scrambledByte, sysPro.getCharSetName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysPro.getResultDTO(resultStrAry);
    }

    @Override
    public resultDTO unscramble(String[] srcStrAry) {
        String[] resultStrAry = new String[srcStrAry.length];
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        String algorithm = sesRtConfigs.getAlgorithm();
        String mode = sesRtConfigs.getMode();
        String padding = sesRtConfigs.getPadding();
        String logicStr = "12,8#6,5";

        try {
            byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
            for (int i = 0; i < srcStrAry.length; i++) {
                byte[] scrambledByte = srcStrAry[i].getBytes(sysPro.getCharSetName());
                byte[] swappedByte = sfkEncrypt.scrambleDecrypt(algorithm, mode, padding, iv, scrambledByte);
                String plainStr = sfkEncrypt.reverseSwapString(new String(swappedByte, sysPro.getCharSetName()), logicStr);
                resultStrAry[i] = plainStr;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysPro.getResultDTO(resultStrAry);
    }

    @Override
    public resultDTO encryptedToScrambled(String[] srcStrAry) {
        String[] resultStrAry = new String[srcStrAry.length];
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        String algorithm = sesRtConfigs.getAlgorithm();
        String mode = sesRtConfigs.getMode();
        String padding = sesRtConfigs.getPadding();

        try {
            byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
            for (int i = 0; i < srcStrAry.length; i++) {
                byte[] plainBytes = srcStrAry[i].getBytes(sysPro.getCharSetName());
                byte[] cipherBytes = sfkEncrypt.scrambleEncrypt(algorithm, mode, padding, iv, plainBytes);
                resultStrAry[i] = new String(cipherBytes, sysPro.getCharSetName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysPro.getResultDTO(resultStrAry);
    }

    @Override
    public resultDTO scrambledToEncrypted(String[] srcStrAry) {
        String[] resultStrAry = new String[srcStrAry.length];
        List<SesRtConfigs> listSesRtConfigs = srcRep.listSesRtConfigs();
        SesRtConfigs sesRtConfigs = listSesRtConfigs.get(0);
        String algorithm = sesRtConfigs.getAlgorithm();
        String mode = sesRtConfigs.getMode();
        String padding = sesRtConfigs.getPadding();

        try {
            byte[] iv = Hex.decodeHex(sesRtConfigs.getIv().toCharArray());
            for (int i = 0; i < srcStrAry.length; i++) {
                byte[] cipherBytes = srcStrAry[i].getBytes(sysPro.getCharSetName());
                byte[] plainBytes = sfkEncrypt.scrambleDecrypt(algorithm, mode, padding, iv, cipherBytes);
                resultStrAry[i] = new String(plainBytes, sysPro.getCharSetName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sysPro.getResultDTO(resultStrAry);
    }
*/

}
