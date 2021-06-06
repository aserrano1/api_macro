package com.dcatech.labviewer.macro.repository;

import com.dcatech.labviewer.macro.model.LVUser;
import com.dcatech.labviewer.macro.repository.impl.LVUserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LVUserRepository extends JpaRepository<LVUser, String>, LVUserRepositoryCustom {

}
