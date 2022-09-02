package hk.gov.ehr.sfk.encryption.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author yunzh
 */
@Getter
@AllArgsConstructor
public class ScrambleLogic implements Serializable {
    private static final long serialVersionID = 1797553552409875769L;
    private int[] srcChar;
    private int[] desChar;

}
