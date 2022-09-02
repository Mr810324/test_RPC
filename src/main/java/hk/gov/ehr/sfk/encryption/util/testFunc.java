package hk.gov.ehr.sfk.encryption.util;


import hk.gov.ehr.sfk.encryption.entity.ScrambleLogic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class testFunc {
    public static void main(String[] args) throws Exception {
        int i1 = Integer.parseInt("a");
//        String[] logicAry = "12,a#b,c".split("#");
//        String[] srcChar = logicAry[0].split(",");
//        String[] desChar = logicAry[1].split(",");
//        int[] src = new int[srcChar.length];
//        int[] des = new int[desChar.length];
//        for (int i = 0; i < srcChar.length; i++) {
//            src[i] = Integer.parseInt(srcChar[i].trim());
//            des[i] = Integer.parseInt(desChar[i].trim());
//        }
    }

    @Test
    public void test1() throws UnsupportedEncodingException {
        //现在Base64编码   import java.util.Base64;
        String s = "zhangjilin";
        String encodeToString = Base64.getEncoder().encodeToString(s.getBytes());
        System.out.println("s.getBytes() is: " + s.getBytes());
        System.out.println(new String(s.getBytes()));
        byte[] b1 = Base64.getEncoder().encode(s.getBytes());
        System.out.println(encodeToString);
        System.out.println(Base64.getEncoder().encodeToString(b1));
        //解码
        byte[] decode = Base64.getDecoder().decode(encodeToString);
        System.out.println("decode is: " + decode);
        System.out.println(new String(decode));
        ByteArrayInputStream is1;
        is1 = new ByteArrayInputStream(("slot:" + 1).getBytes());
    }

    @Test
    public void test02() {
        String[] srcStrAry = {};
        System.out.println(srcStrAry.length);
        char[][] srcChaAry = new char[srcStrAry.length][];
        for (int i = 0; i < srcStrAry.length; i++) {
            if (srcStrAry[i] == null) {
                srcChaAry[i] = null;
                continue;
            }
            srcChaAry[i] = srcStrAry[i].toCharArray();
        }
        System.out.println(srcChaAry.length);
    }

}
