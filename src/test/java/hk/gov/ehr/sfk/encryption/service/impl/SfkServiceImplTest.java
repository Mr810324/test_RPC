package hk.gov.ehr.sfk.encryption.service.impl;

import hk.gov.ehr.sfk.encryption.config.SfkConfig;
import hk.gov.ehr.sfk.encryption.dao.SesRtConfigsRepository;
import hk.gov.ehr.sfk.encryption.entity.SesRtConfigs;
import hk.gov.ehr.sfk.encryption.entity.ResultDTO;
import hk.gov.ehr.sfk.encryption.exception.ApiException;
import hk.gov.ehr.sfk.encryption.util.SfkEncrypt;
import hk.gov.ehr.sfk.encryption.util.SystemProperties;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
//@SpringBootTest
public class SfkServiceImplTest {

    @Mock
    SesRtConfigsRepository srcRep;
    @Mock
    SfkConfig sfkConfig;
    @Mock
    SfkEncrypt sfkEncrypt;
    @Mock
    SystemProperties sysPro;
    @InjectMocks
    SfkServiceImpl sfkService;

    private final String IV_STR = "EB408552C6564A2CA44469769B2E634B";
    private final String[] PLAIN_TEXT_ARRAY = {"012345678912"};
    private final char[][] PLAIN_CHAR_ARRAY = {"012345678912".toCharArray()};
    private final String[] CIPHER_TEXT_ARRAY = {"Skl5n+ptpBmSLfh3RGd6CQ=="};
    private final char[][] CIPHER_CHAR_ARRAY = {"Skl5n+ptpBmSLfh3RGd6CQ==".toCharArray()};
    private final String LOGIC_STR = "12,8#6,5";

    @Test
    private void initGetSesRtConfig(){
        SesRtConfigs sesRtConfigs = new SesRtConfigs();
        sesRtConfigs.setAlgorithm("AES");
        sesRtConfigs.setMode("CBC");
        sesRtConfigs.setPadding("PKCS5Padding");
        sesRtConfigs.setEncryptString("KeyForEncryption");
        sesRtConfigs.setIv(IV_STR);
        List<SesRtConfigs> listSesRtConfigs = new ArrayList<>();
        listSesRtConfigs.add(sesRtConfigs);
        when(srcRep.listSesRtConfigs()).thenReturn(listSesRtConfigs);
    }
    @Test
    private void initGetSesRtConfigException(){
        when(srcRep.listSesRtConfigs()).thenReturn(new ArrayList<SesRtConfigs>());
    }
    @Test
    public void initEncryptSfkConfig(){
        when(sfkConfig.getMaxPlainTextLength()).thenReturn(1000);
    }
    @Test
    private void initConvertArrayForEncrypt(){
        when(sysPro.convertCharArrayToByteArray(PLAIN_CHAR_ARRAY[0])).thenReturn(PLAIN_TEXT_ARRAY[0].getBytes(StandardCharsets.UTF_8));
    }
    @Test
    private void initCharsetName(){
        when(sysPro.getCharSetName()).thenReturn("UTF-8");
    }
    @Test
    private void initEncrypt() throws Exception{
        byte[] iv = Hex.decodeHex(IV_STR.toCharArray());
        when(sfkEncrypt.encrypt("AES","CBC","PKCS5Padding",iv,"KeyForEncryption",
                PLAIN_TEXT_ARRAY[0].getBytes(StandardCharsets.UTF_8))).thenReturn(CIPHER_TEXT_ARRAY[0].getBytes(StandardCharsets.UTF_8));
    }
    @Test
    private void initPreprocessForEncrypt(){
        when(sfkEncrypt.preprocessForEncrypt(0,1000,0,PLAIN_CHAR_ARRAY[0], new String[1])).
                thenReturn(false);
    }

    @Test
    public void testEncrypt() throws Exception {
        initGetSesRtConfigException();
        Assertions.assertThrows(ApiException.class,()->sfkService.encrypt(PLAIN_CHAR_ARRAY));
        System.out.println("成功抛出 ApiException");
        initGetSesRtConfig();
        initEncryptSfkConfig();
        initPreprocessForEncrypt();
        initEncrypt();
        initCharsetName();
        initConvertArrayForEncrypt();
        ResultDTO resultDTO = sfkService.encrypt(PLAIN_CHAR_ARRAY);
        Assertions.assertEquals(CIPHER_TEXT_ARRAY[0],resultDTO.getResultStrAry()[0]);
        System.out.println("抛出 ApiException 后继续成功加密");
    }


    @Test
    public void decrypt() {
    }

    @Test
    public void scramble() {
    }

    @Test
    public void unscramble() {
    }

    @Test
    public void encryptedToScrambled() {
    }

    @Test
    public void scrambledToEncrypted() {
    }
}
