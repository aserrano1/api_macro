package com.dcatech.labviewer.macro.repository;

import com.dcatech.labviewer.macro.model.LVQueryFields;
import com.dcatech.labviewer.macro.repository.impl.LVQueryFieldsRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by LuiFer on 30/06/2017.
 */
public interface LVQueryFieldsRepository extends JpaRepository<LVQueryFields, String>, LVQueryFieldsRepositoryCustom {


}
