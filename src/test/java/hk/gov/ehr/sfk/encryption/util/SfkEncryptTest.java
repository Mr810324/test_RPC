package hk.gov.ehr.sfk.encryption.util;


import hk.gov.ehr.sfk.encryption.config.SfkConfig;
import hk.gov.ehr.sfk.encryption.entity.ScrambleLogic;
import hk.gov.ehr.sfk.encryption.exception.ApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
@ExtendWith(MockitoExtension.class)
public class SfkEncryptTest {

    @Mock
    SystemProperties sysPro;
    @Mock
    SfkConfig sfkConfig;

    @InjectMocks
    SfkEncrypt sfkEncrypt;

    @Test
    private void initGetLogic() throws Exception {
        int[] src = {12,8};
        int[] des = {6,5};
        ScrambleLogic logic = new ScrambleLogic(src,des);
        String logicStr = "12,8#6,5";
        when(sysPro.getLogic(logicStr)).thenReturn(logic);
        when(sysPro.getCharSetName()).thenReturn("UTF-8");

    }

    @Test
    public void swapBytes() throws Exception {
        initGetLogic();
        byte[] plainBytes1 = "1234567890abc".getBytes("UTF-8");
        byte[] plainBytes2 = "123".getBytes("UTF-8");
        byte[] swappedBytes1 = "12348b7590a6c".getBytes("UTF-8");
        byte[] swappedBytes2 = "123".getBytes("UTF-8");
        String logicStr = "12,8#6,5";
        Assertions.assertArrayEquals(swappedBytes1,sfkEncrypt.swapBytes(plainBytes1,logicStr));
        Assertions.assertArrayEquals(swappedBytes2,sfkEncrypt.swapBytes(plainBytes2,logicStr));
    }

    @Test
    public void reverseSwapSwapBytes() throws Exception {
        initGetLogic();
        byte[] plainBytes1 = "1234567890abc".getBytes("UTF-8");
        byte[] plainBytes2 = "123".getBytes("UTF-8");
        byte[] swappedBytes1 = "12348b7590a6c".getBytes("UTF-8");
        byte[] swappedBytes2 = "123".getBytes("UTF-8");
        String logicStr = "12,8#6,5";
        when(sysPro.getCharSetName()).thenReturn("UTF-8");
        Assertions.assertArrayEquals(plainBytes1,sfkEncrypt.swapBytes(swappedBytes1,logicStr));
        Assertions.assertArrayEquals(plainBytes2,sfkEncrypt.swapBytes(swappedBytes2,logicStr));
    }




    @Test
    void preprocessForEncrypt() {
        char[] chars1 = {'1','2','3','4'};
        char[] chars2 = {};
        char[] chars3 = {' ',' '};
        String[] resultStrAry = new String[10];
        Assertions.assertTrue(sfkEncrypt.preprocessForEncrypt(0,3,4,null,resultStrAry));
        Assertions.assertTrue(sfkEncrypt.preprocessForEncrypt(0,3,4,chars1,resultStrAry));
        Assertions.assertTrue(sfkEncrypt.preprocessForEncrypt(5,3,4,chars1,resultStrAry));
        Assertions.assertTrue(sfkEncrypt.preprocessForEncrypt(0,3,4,chars2,resultStrAry));
        Assertions.assertTrue(sfkEncrypt.preprocessForEncrypt(0,3,4,chars3,resultStrAry));
        Assertions.assertFalse(sfkEncrypt.preprocessForEncrypt(0,5,4,chars1,resultStrAry));
    }
    @Test
    void preprocessForScramble() {
        char[] chars1 = {'1','2','3','4'};
        char[] chars2 = {};
        char[] chars3 = {' ',' '};
        String[] resultStrAry = new String[10];
        Assertions.assertTrue(sfkEncrypt.preprocessForScramble(0,3,null,resultStrAry));
        Assertions.assertThrows(ApiException.class,()->sfkEncrypt.preprocessForScramble(0,3,chars1,resultStrAry));
        Assertions.assertTrue(sfkEncrypt.preprocessForScramble(0,3,chars2,resultStrAry));
        Assertions.assertTrue(sfkEncrypt.preprocessForScramble(0,3,chars3,resultStrAry));
        Assertions.assertFalse(sfkEncrypt.preprocessForScramble(0,5,chars1,resultStrAry));
    }
    /*Mock: get common Config Data
    * */
}
