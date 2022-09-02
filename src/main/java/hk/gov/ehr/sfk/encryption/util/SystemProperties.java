package hk.gov.ehr.sfk.encryption.util;


import hk.gov.ehr.sfk.encryption.entity.ErrorResultEnum;
import hk.gov.ehr.sfk.encryption.entity.ScrambleLogic;
import hk.gov.ehr.sfk.encryption.entity.ResultDTO;
import hk.gov.ehr.sfk.encryption.exception.ApiException;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * @author yunzh
 */
@Component
public class SystemProperties {

    private ScrambleLogic logic;
    private final String CHARSET_NAME = "UTF8";
    private final String LOGIC_SEPARATOR = "#";
    private final String LOGIC_SUB_SEPARATOR = ",";

    private void setLogic(String logicStr)  {
        if(logicStr == null){
            throw new ApiException(ErrorResultEnum.DATABASE_GET_DATA_ERROR,"get data from db error,logicStr is null");
        }
        if (!(logicStr.contains(LOGIC_SEPARATOR) && (logicStr.contains(LOGIC_SUB_SEPARATOR)))) {
            throw new ApiException(ErrorResultEnum.DATABASE_GET_DATA_ERROR,"get data from db error, logicStr is invalid");
        }
        String[] logicAry = logicStr.split(LOGIC_SEPARATOR);
        String[] srcChar = logicAry[0].split(LOGIC_SUB_SEPARATOR);
        String[] desChar = logicAry[1].split(LOGIC_SUB_SEPARATOR);
        if (srcChar.length != desChar.length) {
            throw new ApiException(ErrorResultEnum.DATABASE_GET_DATA_ERROR,"get data from db error, logicStr is invalid");
        }

        int[] src = new int[srcChar.length];
        int[] des = new int[desChar.length];
        for (int i = 0; i < srcChar.length; i++) {
            src[i] = Integer.parseInt(srcChar[i].trim());
            des[i] = Integer.parseInt(desChar[i].trim());
        }
        logic = new ScrambleLogic(src, des);
    }

    public ScrambleLogic getLogic(String logicStr) throws Exception {
        if (logic == null) {
            setLogic(logicStr);
        }
        return logic;
    }

    public String getCharSetName() {
        return CHARSET_NAME;
    }


    public char[][] convertStringArrayToCharArray(String[] srcStrAry) {
        char[][] srcChaAry = new char[srcStrAry.length][];
        for (int i = 0; i < srcStrAry.length; i++) {
            if (srcStrAry[i] == null) {
                srcChaAry[i] = null;
                continue;
            }
            srcChaAry[i] = srcStrAry[i].toCharArray();
        }
        return srcChaAry;
    }

    public byte[] convertCharArrayToByteArray(char[] chars) {
        ByteBuffer buf = Charset.forName(getCharSetName()).encode(CharBuffer.wrap(chars));
        byte[] bytes = new byte[buf.limit()];
        buf.get(bytes);
        return bytes;
    }
}