package com.dcatech.labviewer.macro.repository.impl;

import com.dcatech.labviewer.macro.model.Argument;
import com.dcatech.labviewer.macro.model.LVQuery;
import com.dcatech.labviewer.macro.model.LVQueryFields;

import java.util.List;

/**
 * Created by LuiFer on 30/06/2017.
 */
public interface LVQueryFieldsRepositoryCustom {

    List<LVQueryFields> findByQueryId(String queryid);

    List<LVQueryFields> findColumnsFilterable(String queryid);

    List<LVQueryFields> findQueryFieldsByQueryIdforReport(String queryid);

    List<LVQueryFields> findByQueryIdtodynamicReport(String queryid, String format);

    LVQueryFields findByRowNumId(Long rownumid, String queryid);

    List<LVQueryFields> findByProcedure(LVQuery queryid, List<Argument> argumentsJson, int tipo);
}
