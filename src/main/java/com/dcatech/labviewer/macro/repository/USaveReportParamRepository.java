package com.dcatech.labviewer.macro.repository;

import com.dcatech.labviewer.macro.model.USaveReportParam;
import com.dcatech.labviewer.macro.repository.impl.USaveReportParamRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface USaveReportParamRepository extends JpaRepository<USaveReportParam, String>, USaveReportParamRepositoryCustom {

}
