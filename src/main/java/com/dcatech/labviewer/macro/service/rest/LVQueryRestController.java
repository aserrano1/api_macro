package com.dcatech.labviewer.macro.service.rest;

//import com.dcatech.labviewer.macro.configuration.RestApiController;
//import com.dcatech.labviewer.macro.controller.HomeController;
import com.dcatech.labviewer.macro.controller.HomeController;
import com.dcatech.labviewer.macro.model.*;
import com.dcatech.labviewer.macro.repository.LVQueryFieldsRepository;
import com.dcatech.labviewer.macro.repository.LVQueryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LVQueryRestController {

    private final Logger logger = LoggerFactory.getLogger(LVQueryRestController.class);

    @Autowired
    LVQueryRepository lvQueryRepository;

    @Autowired
    LVQueryFieldsRepository lvQueryFieldsRepository;

    @PostMapping(value = {"/query/findAll"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataSourceResult> findAll(@RequestBody ObjectNode json) {

        DataSourceRequest req = FilterGridUtil.filter(json.get("filter"));
        req.setSkip(json.get("skip").asInt());
        req.setTake(json.get("take").asInt());

        List<Object> listaResultsQueries = new ArrayList<>();
        List<Object[]> lvSQueries = lvQueryRepository.searchingAllQueries(req.getSkip(), req.getTake(), req.getFilter());

        BigDecimal total = BigDecimal.ZERO;

        for (Object[] query : lvSQueries) {
            ObjectNode jsonSampleResult = JsonNodeFactory.instance.objectNode();
            total = (BigDecimal) query[0];
            LVQuery q = (LVQuery) query[1];
            jsonSampleResult.put("queryid", q.getQueryid());
            jsonSampleResult.put("querylabel", q.getQuerylabel());
            jsonSampleResult.put("querydesc", q.getQuerydesc());
            jsonSampleResult.put("section", q.getSection());
            listaResultsQueries.add(jsonSampleResult);

        }

        DataSourceResult dataSR = new DataSourceResult();
        dataSR.setData(listaResultsQueries);
        dataSR.setTotal(total.longValue());
        return new ResponseEntity<>(dataSR, HttpStatus.OK);

    }


    //public Map parametrosWithoutBrackets = null;
    //public Map parametrosWithTitle = null;
    private String selectclause;
    private String fromclause;
    private String whereclause;
    private LVQuery lvquery = null;

    private String GetSecurityQuery(LVQuery query, String userID) {//Obtener Filtro De Seguridad de Labvantage
        String AddedSql = "";
        String Alias = GetFirstAlias(query.getFromclause());
        String EmbedSecurity;
        String SecurityType;
        String SdcId;
        EmbedSecurity = query.getEmbedSecurityFlag();
        SecurityType = query.getAccesscontrolledflag();
        SdcId = query.getBasedonid();

        if (EmbedSecurity != null && EmbedSecurity.equalsIgnoreCase("Y")) {//Si Seguridad esta Activada en el Query
            if (SecurityType.equalsIgnoreCase("D") || SecurityType.equalsIgnoreCase("B") || SecurityType.equalsIgnoreCase("D")) {
                AddedSql = "";
                List<Object> listaSecurityDepartamental = lvQueryRepository.FindSecurityDepartamental(SdcId, userID);
                StringBuilder Str = new StringBuilder();
                Str.append("'" + listaSecurityDepartamental.get(0) + "'");
                for (int i = 1; i < listaSecurityDepartamental.size(); i++) {
                    Str.append(",");
                    Str.append("'" + listaSecurityDepartamental.get(i) + "'");
                }
                AddedSql = "(nvl(" + Alias + ".Securitydepartment" + ",'(null)')in (" + Str.toString() + ",'(null)')" +
                        " and nvl(" + Alias + ".Securityuser" + ",'(null)') in ('" + userID + "','(null)') ) and ";
            } else {

                if (SecurityType.equalsIgnoreCase("S")) {
                    List<Object> listaSecuritySetUsuario = lvQueryRepository.FindSecurityBySecuritySet(SdcId, userID);

                    StringBuilder Str = new StringBuilder();
                    Str.append("'" + listaSecuritySetUsuario.get(0) + "'");
                    for (int i = 1; i < listaSecuritySetUsuario.size(); i++) {
                        Str.append(",");
                        Str.append("'" + listaSecuritySetUsuario.get(i) + "'");
                    }

                    AddedSql = "nvl(" + Alias + ".securityset" + ",'(null)') in (" + Str.toString() + ",'(null)') and ";

                }
            }

        }


        return AddedSql;

    }

    private String GetFirstAlias(String sql) {
        //Oberner el alias  de la tabla si tiene uno
        Pattern p = Pattern.compile("((?i)[^\\s]+(\\s+)?(?=,|$|join)){1}");
        Matcher m = p.matcher(sql);
        m.find();
        return m.group(0);

    }

    public String createQuery(LVQuery lvquery, JsonNode argumentsJson, boolean includeUser) {
        List<Argument> argsIntoJson = getArgsIntoJson(argumentsJson, includeUser);
        selectclause = replaceArgumentsInClause(lvquery.getSelectshowclause(), argsIntoJson);//"s_sample.s_sampleid, s_sample.samplestatus, s_sample.createby, s_sample.productid";
        fromclause = lvquery.getFromclause();//"s_sample";
        fromclause = replaceArgumentsInClause(fromclause, argsIntoJson);
        whereclause = replaceArgumentsInClause(lvquery.getWhereclause(), argsIntoJson);
        String SecFlag = lvquery.getEmbedSecurityFlag();
        if (SecFlag != null && SecFlag.equalsIgnoreCase("Y")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName(); //get logged in username
            whereclause = GetSecurityQuery(lvquery, name) + whereclause;
        }
        return "select count(*) over(), tbl.* from  ( select " + selectclause + " from " + fromclause + " where " + whereclause + ") tbl where 1=1";
    }

    @PostMapping(value = {"/executeQuery"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataSourceResult> executeQuery(@RequestBody ObjectNode json,
                                                         HttpServletRequest request) throws Exception {
        logger.info("//************ START EXECUTING QUERY ************//");
        String query = "";
        String queryForCount = "";
        String queryId = json.get("queryId").asText();

        DataSourceRequest req = FilterGridUtil.filter(json.get("filter"));
        if (json.has("skip")) {
            req.setSkip(json.get("skip").asInt());
        } else {
            req.setSkip(0);
        }
        if (json.has("take")) {
            req.setTake(json.get("take").asInt());
        } else {
            req.setTake(100);
        }


        if (!queryId.equals(this.lvquery.getQueryid())) {
            lvquery = lvQueryRepository.findByQueryId(queryId);
        }
        logger.info("lvquery = " + this.lvquery);
        JsonNode argumentsJson = json.get("argumentsJson");
        logger.info("queryId = " + queryId);
        logger.info("argumentsJson = " + argumentsJson);

        if (lvquery.getQueryconftype() == null) {
            query = createQuery(this.lvquery, argumentsJson, true);
        } else if (lvquery.getQueryconftype().equals("procedure")) {
            query = "";
        }
        //query = lvquery.getQueryconftype().equals("procedure") ? "" : createQuery(this.lvquery, argumentsJson);
        logger.info("query = " + query);

        List<Object[]> lvResultsQuery = null;
        List<LVQueryFields> listColumns = new ArrayList();
        //listColumns.add(0, new LVQueryFields());
        try {
            //lvResultsQuery = lvQueryRepository.executeQueryGrid(query, req.getSkip(), req.getTake(), req.getFilter(), lvQueryFieldsRepository.findColumnsFilterable(queryId));
            if (lvquery.getQueryconftype() == null) {
                listColumns.addAll(lvQueryFieldsRepository.findByQueryId(queryId));
                lvResultsQuery = lvQueryRepository.executeQueryGrid(query, req.getSkip(), req.getTake(), req.getFilter(), lvQueryFieldsRepository.findColumnsFilterable(queryId));
                listColumns.add(0, new LVQueryFields());
            } else if (lvquery.getQueryconftype().equals("procedure")) {
                List<Argument> argumentsList = getArgsIntoJson(argumentsJson, true);
                listColumns.addAll(lvQueryFieldsRepository.findByProcedure(this.lvquery, argumentsList, 0));
                lvResultsQuery = lvQueryRepository.executeQueryGridWithProcedure(this.lvquery, argumentsList, req.getSkip(), req.getTake(), 0);
            }
            //lvResultsQuery = lvquery.getQueryconftype().equals("procedure") ? lvQueryRepository.executeQueryGridWithProcedure(this.lvquery,argumentsJson, req.getSkip(), req.getTake(), req.getFilter()) : lvQueryRepository.executeQueryGrid(query, req.getSkip(), req.getTake(), req.getFilter(), lvQueryFieldsRepository.findColumnsFilterable(queryId));
            //lvResultsQuery = lvQueryRepository.executeQueryGridWithProcedure(query, req.getSkip(), req.getTake(), req.getFilter());
        } catch (Exception e) {

            logger.error(e.getMessage(), e);
            throw new Exception(e.getMessage());

        }

        //List<LVQueryFields> listColumns = lvQueryFieldsRepository.findByQueryId(queryId);
        //List<LVQueryFields> listColumns = lvQueryFieldsRepository.findByProcedure(queryId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        List<Object> listResults = iterateLvResultsQuery(lvResultsQuery, listColumns, dateFormat);

        DataSourceResult dataSR = new DataSourceResult();
        dataSR.setData(listResults);
        //queryForCount = "select count(total) as total from(select count(*) as total " + " from " + fromclause + " where " + whereclause + ") group by 1";
        logger.info("queryForCount = " + queryForCount);
        //long total = lvQueryRepository.executeQueryCount(queryForCount);
        //long total = listResults.get(0).
        //dataSR.setTotal(total);
        dataSR.setTotal(totalCount);
        List arrayList = new ArrayList();
        arrayList.add(lvquery);
        dataSR.setColumns(listColumns);

        logger.info("//************ END EXECUTING QUERY ************//");
        return new ResponseEntity<>(dataSR, HttpStatus.OK);

    }

    @PostMapping(value = {"/executeChartQuery"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataSourceResult> executeChartQuery(@RequestBody ObjectNode json,
                                                              HttpServletRequest request) throws Exception {
        String query = "";
        String queryId = json.get("queryId").asText();
        LVQuery lvquery = lvQueryRepository.findByQueryId(queryId);

//        String selectclausetoReport = lvquery.getSelectclause();
        JsonNode argumentsJson = json.get("argumentsJson");

        query = createQuery(lvquery, argumentsJson, true);

        List<Object[]> lvResultsQuery = null;
        try {
            lvResultsQuery = lvQueryRepository.executeQuery(query);
        } catch (Exception e) {
            logger.error("Error ejecutando consulta de gráficos: ", e);
            throw new Exception(e.getMessage());
        }

        List<LVQueryFields> listColumns = new ArrayList();
        listColumns.add(0, new LVQueryFields());
        listColumns.addAll(lvQueryFieldsRepository.findByQueryId(queryId));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        List<Object> listResults = iterateLvResultsQuery(lvResultsQuery, listColumns, dateFormat);

        DataSourceResult dataSR = new DataSourceResult();
        dataSR.setData(listResults);
        dataSR.setColumns(listColumns);
        return new ResponseEntity<>(dataSR, HttpStatus.OK);

    }

    public long getTotalCount() {
        return totalCount;
    }

    //    private List<Object> listResults;
    private long totalCount = 0;

    public List<Object> iterateLvResultsQuery(List<Object[]> lvResultsQuery, List<LVQueryFields> listColumns, SimpleDateFormat dateFormat) {
        List<Object> listResults = new ArrayList<>();
        totalCount = 0;
        for (Object[] result : lvResultsQuery) {
            BigDecimal bd = (BigDecimal) result[0];
            totalCount = bd.longValue();
            ObjectNode jsonSampleResult = JsonNodeFactory.instance.objectNode();

            for (int i = 1; i <= listColumns.size() - 1; i++) {
                if (result[i] instanceof String) {
                    jsonSampleResult.put(listColumns.get(i).getFieldalias(), (String) result[i]);
                } else if (result[i] instanceof Timestamp) {
                    Timestamp ts = (Timestamp) result[i];
                    Date date = new Date();
                    date.setTime(ts.getTime());
                    String dateSet = dateFormat.format(date);
                    jsonSampleResult.put(listColumns.get(i).getFieldalias(), dateSet);
                } else {
                    if (result[i] instanceof BigDecimal) {
                        jsonSampleResult.put(listColumns.get(i).getFieldalias(), (BigDecimal) result[i]);
                    } else {
                        if (result[i] != null) {
                            logger.info("Tipo de Dato = \"" + result[i].getClass() + "\"  No Implementado");
                        }
                    }
                }
            }
            listResults.add(jsonSampleResult);
        }
        return listResults;
    }

    public String createQuerySentenceForReport(LVQuery byQueryId, List<Argument> argumentsJson) {
        String selectclause = byQueryId.getSelectclause();
        return getQuery(byQueryId, argumentsJson, selectclause);
    }

    public String createQuerySentenceForPdfReport(LVQuery byQueryId, List<Argument> argumentsJson) {
        String selectclause = byQueryId.getSelectShowPdfClause();
        return getQuery(byQueryId, argumentsJson, selectclause);
    }

    public String createQuerySentenceForExcelReport(LVQuery byQueryId, List<Argument> argumentsJson) {
        String selectclause = byQueryId.getSelectShowExcelClause();
        return getQuery(byQueryId, argumentsJson, selectclause);
    }

    public String getQuery(LVQuery byQueryId, List<Argument> argsIntoJson, String selectclause) {
//        List<Argument> argsIntoJson = getArgsIntoJson(argumentsJson);
        String fromclause = replaceArgumentsInClause(byQueryId.getFromclause(), argsIntoJson);
        String whereclause = replaceArgumentsInClause(byQueryId.getWhereclause(), argsIntoJson);
        selectclause = replaceArgumentsInClause(selectclause, argsIntoJson);
        return "SELECT " + selectclause + " FROM " + fromclause + " WHERE " + whereclause;
    }

    public static String replaceArgumentsInClause(String whereclause, List<Argument> argumentsJson) {
        for (Argument arg : argumentsJson) {
            whereclause = whereclause.replace(arg.getName(), arg.getValue());
        }
        return whereclause;
    }

    public List<Argument> getArgsIntoJson(JsonNode argumentsJson, boolean includeUser) {
        List<Argument> argsinto = new ArrayList<>();
        ArrayNode arrNode;
        if (argumentsJson != null && argumentsJson.has("arguments") &&
                argumentsJson.get("arguments") != null) {
            if (argumentsJson.get("arguments").isArray()) {
                //parametrosWithoutBrackets = new HashMap();
                //parametrosWithTitle = new HashMap();
                arrNode = (ArrayNode) argumentsJson.get("arguments");
                Iterator<JsonNode> node = arrNode.elements();
                Argument argument;
                while (node.hasNext()) {
                    argument = new Argument();
                    JsonNode element = node.next();
                    if (!element.has("isFromRow")) {
                        String arginto = element.get("arginto").asText();
                        String value = "";
                        String text = "";
                        String title = "";
                        if (element.has("title")) {
                            argument.setTitle(element.get("title").asText());
                        }
                        if (element.get("value").isArray()) {
                            ArrayNode arrNodeMultipeParameters = (ArrayNode) element.get("value");
                            Iterator<JsonNode> nodeMultiParamters = arrNodeMultipeParameters.elements();
                            Boolean firsTime = true;
                            while (nodeMultiParamters.hasNext()) {
                                JsonNode param = nodeMultiParamters.next();
                                if (firsTime) {
                                    value = "\'" + param.asText() + "\'";
                                    firsTime = !firsTime;
                                } else
                                    value += ", \'" + param.asText() + "\'";
                            }
                        } else {
                            value = element.get("value").asText();
                        }

                        if (!element.has("text")) {
                            argument.setText("");
                        } else {
                            if (element.get("text").isArray()) {
                                ArrayNode arrNodeMultipeParameters = (ArrayNode) element.get("text");
                                Iterator<JsonNode> nodeMultiParamters = arrNodeMultipeParameters.elements();
                                Boolean firsTime = true;
                                while (nodeMultiParamters.hasNext()) {
                                    JsonNode param = nodeMultiParamters.next();
                                    if (firsTime) {
                                        text = "\'" + param.asText() + "\'";
                                        firsTime = !firsTime;
                                    } else
                                        text += ", \'" + param.asText() + "\'";
                                }
                            } else {
                                text = element.get("text").asText();
                            }
                        }
                        argument.setName(arginto);
                        argument.setValue(value);
                        argument.setText(text);
                        argsinto.add(argument);
                    }


                    //parametrosWithoutBrackets.put(arginto, value);
//                    if (title.length() > 0) {
//                          parametrosWithTitle.put(title, value);
//                    }
                }

            }
        }
        if (includeUser) {
            Argument argument = new Argument();
            argument.setTitle("Usuario");
            argument.setName("[LOGON_NAME]");
            argument.setValue(HomeController.getUsuario().getLogonName());
            argument.setText(HomeController.getUsuario().getLogonName());

            argsinto.add(argument);

            argument = new Argument();
            argument.setTitle("Usuario");
            argument.setName("[SYSUSERID]");
            argument.setValue(HomeController.getUsuario().getSysUserId());
            argument.setText(HomeController.getUsuario().getSysUserId());

            argsinto.add(argument);
        }

        return argsinto;
    }


    @PostMapping(value = {"/getColumns"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataSourceResult> getColumns(@RequestBody ObjectNode json, HttpServletRequest request) throws Exception {

        String queryId = json.get("queryId").asText();
        this.lvquery = lvQueryRepository.findByQueryId(queryId);
        JsonNode argumentsJson = json.get("argumentsJson");
        //request.getSession().setAttribute("columns",listColumns);
        //List<LVQueryFields> listColumns = null;//= lvQueryFieldsRepository.findByQueryId(queryId);
        List<LVQueryFields> listColumns = new ArrayList();
        if (lvquery.getQueryconftype() == null) {
            listColumns = lvQueryFieldsRepository.findByQueryId(queryId);
        } else if (lvquery.getQueryconftype().equals("procedure")) {
            List<Argument> argumentsList = getArgsIntoJson(argumentsJson, true);
            listColumns = lvQueryFieldsRepository.findByProcedure(this.lvquery, argumentsList, 0);
            listColumns.remove(0);
        }
        //listColumns = lvquery.getQueryconftype().equals("procedure") ? lvQueryFieldsRepository.findByProcedure(this.lvquery,argumentsJson) : lvQueryFieldsRepository.findByQueryId(queryId);


        //List<LVQueryFields> listColumns = lvQueryFieldsRepository.findByProcedure(queryId);
        request.getSession().setAttribute("columns", listColumns);

        //LVQuery lvquery = lvQueryRepository.findByQueryId(queryId);
        DataSourceResult dataSR = new DataSourceResult();

        dataSR.setColumns(listColumns);
        dataSR.setQuerydata(lvquery);
        return new ResponseEntity<>(dataSR, HttpStatus.OK);

    }

}
