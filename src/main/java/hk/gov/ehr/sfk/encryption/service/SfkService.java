package hk.gov.ehr.sfk.encryption.service;

import hk.gov.ehr.sfk.encryption.entity.ResultDTO;
import org.apache.commons.codec.DecoderException;

public interface SfkService {
    ResultDTO encrypt(char[][] srcChaAry) throws Exception;
    ResultDTO decrypt(char[][] srcChaAry) throws Exception;
    ResultDTO scramble(char[][] srcChaAry) throws Exception;
    ResultDTO unscramble(char[][] srcChaAry) throws Exception;
    String testDecrypt(String s1) throws Exception;
/*    resultDTO encryptedToScrambled(String[] srcStrAry);
    resultDTO scrambledToEncrypted(String[] srcStrAry);*/
    }
