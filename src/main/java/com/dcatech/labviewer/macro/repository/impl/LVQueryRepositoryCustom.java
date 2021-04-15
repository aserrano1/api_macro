package com.dcatech.labviewer.macro.repository.impl;

import com.dcatech.labviewer.macro.model.*;

import java.util.List;

public interface LVQueryRepositoryCustom {

    List<Object[]> searchingAllQueries(int skip, int take, DataSourceRequest.FilterDescriptor filterDescriptor);

    LVQuery findByQueryId(String queryid);

    List<Object> FindSecurityBySecuritySet(String SdcId, String userID);

    List<Object> FindSecurityDepartamental(String SdcId, String userID);

    List executeQuery(String query) throws Exception;

    List<String[]> executeJDBCQuery(String query) throws Exception;

    List executeQueryGrid(String query, int skip, int take, DataSourceRequest.FilterDescriptor filterDescriptor, List<LVQueryFields> filterColumns) throws Exception;

    Long executeQueryCount(String query);

    List<String> findQueryLinks(String queryId);

    List executeQueryGridWithProcedure(LVQuery query, List<Argument> argumentsJson, int skip, int take, int tipo) throws Exception;

    List executeJDBCQueryGridWithProcedure(LVQuery query, List<Argument> argumentsJson, int skip, int take) throws Exception;

    String findReportsTitle();

}
