package hk.gov.ehr.sfk.encryption.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName SesRtConfigs
 * @Description TODO
 * @date 21:26
 * @Version 1.0
 */
@Data
@Entity
@Table(name="VW_SES_RT_CONFIGS")
public class SesRtConfigs {

    @Id
    @Column(name="ALGORITHM")
    private String algorithm;
    @Column(name="MODE")
    private String mode;
    @Column(name="PADDING")
    private String padding;
    @Column(name="ENCRYPT_STRING")
    private String encryptString;
    @Column(name="SCRAMBLE_STRING")
    private String scrambleString;
    @Column(name="ACCESS_ENCRYPT_STRING")
    private String accessEncryptString;
    @Column(name="IV")
    private String iv;
}