package hk.gov.ehr.sfk.encryption.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class SfkConfig {
    public static final String CLIENT_NAME = "sfk";
    @Value("${sfk.encryptString}")
    private String encryptString;
    @Value("${sfk.accessEncryptString}")
    private String accessEncryptString;
    @Value("${sfk.scrambleEncryptString}")
    private String scrambleEncryptString;
    @Value("${sfk.random}")
    private String random;
    @Value("${sfk.maxArraySize}")
    private int maxArraySize;
    @Value("${sfk.maxPlainTextLength}")
    private int maxPlainTextLength;
    @Value("${sfk.maxCipherTextLength}")
    private int maxCipherTextLength;
}
