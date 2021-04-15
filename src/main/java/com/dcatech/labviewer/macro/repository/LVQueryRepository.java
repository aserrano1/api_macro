package com.dcatech.labviewer.macro.repository;

import com.dcatech.labviewer.macro.model.LVQuery;
import com.dcatech.labviewer.macro.repository.impl.LVQueryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LVQueryRepository extends JpaRepository<LVQuery, String>, LVQueryRepositoryCustom {
}
