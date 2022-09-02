package hk.gov.ehr.sfk.encryption.dao;

import hk.gov.ehr.sfk.encryption.entity.SesRtConfigs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SesRtConfigaRepository
 * @Description TODO
 * @date 21:26
 * @Version 1.0
 */
public interface SesRtConfigsRepository extends JpaRepository<SesRtConfigs, String> {
    @Query(value = "Select * from VW_SES_RT_CONFIGS ", nativeQuery = true)
    List<SesRtConfigs> listSesRtConfigs();
}
