package com.dcatech.labviewer.macro.repository;

import com.dcatech.labviewer.macro.model.ULabviewerconnectEntity;
import com.dcatech.labviewer.macro.repository.impl.LabviewerConnectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabviewerConnectRepository extends JpaRepository<ULabviewerconnectEntity, String>, LabviewerConnectRepositoryCustom {
}
