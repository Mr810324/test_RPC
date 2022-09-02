package hk.gov.ehr.sfk.encryption.util;

import hk.gov.ehr.sfk.encryption.entity.ScrambleLogic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SystemPropertiesTest {

    @InjectMocks
    SystemProperties sysPro;
    @Test
    void getLogic() throws Exception {
        int[] src = {12,8};
        int[] des = {6,5};
        ScrambleLogic testLogic = new ScrambleLogic(src,des);
        SystemProperties sysPro = new SystemProperties();
        String logicStr = "12,8#6,5";
        Assertions.assertArrayEquals(testLogic.getSrcChar(),(sysPro.getLogic(logicStr).getSrcChar()));
        Assertions.assertArrayEquals(testLogic.getDesChar(),(sysPro.getLogic(logicStr).getDesChar()));
    }

    public static void main(String[] args) {
        String[] s1 = {"str1","str2",null};
        for (int i = 0;i< s1.length;i++ ) {
            System.out.println(s1[i]);
        }
    }

    @Test
    void convertStringArrayToCharArray() {
        String[] srcStrAry = {"str","","123"};
        char[][] srcChaAry = {{'s','t','r'},{},{'1','2','3'}};
        SystemProperties sysPro = new SystemProperties();
        Assertions.assertArrayEquals(srcChaAry, sysPro.convertStringArrayToCharArray(srcStrAry));
    }

    @Test
    void convertCharArrayToByteArray() throws Exception {
        char[] chars = {'s','t','r'};
        byte[] bytes = "str".getBytes("UTF-8");
        SystemProperties sysPro = new SystemProperties();
        Assertions.assertArrayEquals(bytes,sysPro.convertCharArrayToByteArray(chars));
    }
}