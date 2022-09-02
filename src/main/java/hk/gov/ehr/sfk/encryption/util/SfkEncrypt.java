package hk.gov.ehr.sfk.encryption.util;

import hk.gov.ehr.sfk.encryption.config.SfkConfig;
import hk.gov.ehr.sfk.encryption.entity.ErrorResultEnum;
import hk.gov.ehr.sfk.encryption.entity.SesRtConfigs;
import hk.gov.ehr.sfk.encryption.exception.ApiException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;

@Component
public class SfkEncrypt {

    @Autowired
    private SystemProperties sysPro;
    @Autowired
    SfkConfig sfkConfig;

    public void testDecrypt(String algorithm, String mode, String padding, byte[] iv, String keyAlias, byte[] cipherBytes) throws Exception {
        System.out.println("开始执行 SfkEncrypt.testDecrypt(...)");
//        try {
            SecretKey key = getKey(algorithm, keyAlias);
            Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            System.out.println("Cipher init success");
            byte[] tempCipherBytes = Base64.decodeBase64(cipherBytes);
            System.out.println("get tempCipherBytes success");
            cipher.doFinal(tempCipherBytes);
            System.out.println("发生异常之后");
//        } catch (IllegalArgumentException e){
//            throw new IllegalArgumentException("start in SfkEncrypt:"+e.getClass()+": "+e.getMessage());
//        } catch (IllegalBlockSizeException e){
//            throw new IllegalArgumentException("start in SfkEncrypt:"+e.getClass() + ": "+e.getMessage());
//        }catch (BadPaddingException e){
//            throw new BadPaddingException("start in SfkEncrypt:"+e.getClass()+": "+e.getMessage());
//        }
    }

    public byte[] encrypt(String algorithm, String mode, String padding, byte[] iv, String keyAlias, byte[] plainBytes) throws Exception {
        System.out.println("开始执行 SfkEncrypt.encrypt(...)");
        SecretKey key = getKey(algorithm, keyAlias);
        Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        return Base64.encodeBase64(cipher.doFinal(plainBytes));
    }

    public byte[] decrypt(String algorithm, String mode, String padding, byte[] iv, String keyAlias, byte[] cipherBytes) throws Exception {
        System.out.println("开始执行 SfkEncrypt.decrypt(...)");
        try {
            SecretKey key = getKey(algorithm, keyAlias);
            Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/" + padding);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] tempCipherBytes = Base64.decodeBase64(cipherBytes);
            return cipher.doFinal(tempCipherBytes);
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getClass()+": "+e.getMessage());
        } catch (IllegalBlockSizeException e){
            throw new IllegalArgumentException(e.getClass() + ": "+e.getMessage());
        }catch (BadPaddingException e){
            throw new BadPaddingException(e.getClass()+": "+e.getMessage());
        }
    }

    public SecretKey getKey(String algorithm, String keyAlias) throws Exception {
        SecureRandom random = SecureRandom.getInstance(sfkConfig.getRandom());
        random.setSeed(keyAlias.getBytes(sysPro.getCharSetName()));
        KeyGenerator generator = KeyGenerator.getInstance(algorithm);
        generator.init(128, random);
        SecretKey key = generator.generateKey();
        return key;
    }

    public byte[] swapBytes(byte[] inputBytes, String logicStr) throws Exception {
        char[] inputChar = Charset.forName(sysPro.getCharSetName()).decode(ByteBuffer.wrap(inputBytes)).array();
        int[] srcChar = sysPro.getLogic(logicStr).getSrcChar();
        int[] desChar = sysPro.getLogic(logicStr).getDesChar();

        for (int j = 0; j < srcChar.length; j++) {
            if (srcChar[j] > inputChar.length || desChar[j] > inputChar.length) {
                continue;
            }
            char temp = inputChar[srcChar[j] - 1];
            inputChar[srcChar[j] - 1] = inputChar[desChar[j] - 1];
            inputChar[desChar[j] - 1] = temp;
        }
        return new String(inputChar).getBytes(sysPro.getCharSetName());
        //return sysPro.convertCharArrayToByteArray(inputChar);

    }

    public byte[] reverseSwapByte(byte[] inputBytes, String logicStr) throws Exception {
        char[] inputChar = Charset.forName(sysPro.getCharSetName()).decode(ByteBuffer.wrap(inputBytes)).array();
        int[] srcChar = sysPro.getLogic(logicStr).getSrcChar();
        int[] desChar = sysPro.getLogic(logicStr).getDesChar();
        for (int j = srcChar.length - 1; j >= 0; j--) {
            if (srcChar[j] > inputChar.length || desChar[j] > inputChar.length) {
                continue;
            }
            char temp = inputChar[srcChar[j] - 1];
            inputChar[srcChar[j] - 1] = inputChar[desChar[j] - 1];
            inputChar[desChar[j] - 1] = temp;
        }
        return new String(inputChar).getBytes(sysPro.getCharSetName());

        //return sysPro.convertCharArrayToByteArray(inputChar);
    }

    public boolean preprocessForScramble(int i, int maxTextLength, char[] chars, String[] resultStrAry) {
        if (chars == null ) {
            resultStrAry[i] = null;
            return true;
        }
        if (chars.length > maxTextLength) {
            throw new ApiException(ErrorResultEnum.PARAMETER_ERROR, "The input string exceeds maximum allowed length.");
        }

        if (chars.length == 0 || isSpace(chars)) {
            resultStrAry[i] = new String(chars);
            return true;
        }
        return false;
    }

    public boolean preprocessForEncrypt(int i, int maxTextLength,int maxArraySize, char[] chars, String[] resultStrAry) {
        if (chars == null || chars.length > maxTextLength || i>= maxArraySize) {
            resultStrAry[i] = null;
            return true;
        }

        if (chars.length == 0 || isSpace(chars)) {
            resultStrAry[i] = new String(chars);
            return true;
        }
        return false;
    }

    private boolean isSpace(char[] chars) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ') {
                return false;
            }
        }
        return true;
    }
}
