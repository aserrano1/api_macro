package com.dcatech.labviewer.macro.service.rest;


import com.dcatech.labviewer.macro.controller.ReportsController;
import com.dcatech.labviewer.macro.model.LVQuery;
import com.dcatech.labviewer.macro.model.LVQueryFields;
import com.dcatech.labviewer.macro.repository.LVQueryFieldsRepository;
import com.dcatech.labviewer.macro.repository.LVQueryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LVReportRestController {

    private final Logger logger = LoggerFactory.getLogger(ReportsController.class);

    @Autowired
    private LVQueryRepository lVQueryRepository;

    @Autowired
    private LVQueryFieldsRepository lvQueryFieldsRepository;

    private LVQuery byQueryId = null;

    @Value("${ecp.jasperserver.credentials.host}")
    private String jservercrendentials;

    @PostMapping(value = {"/saveQueryArgumentsForReport"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveQueryArguments(@RequestBody ObjectNode json,
                                   HttpServletRequest request) throws Exception {

        List<ObjectNode> listOfArguments = null;

        Object listOfQueriesAsObject = request.getSession().getAttribute("listOfarguments");
        int level = 0;
        ObjectNode arguments = JsonNodeFactory.instance.objectNode();
        if (listOfQueriesAsObject != null) {
            listOfArguments = (List<ObjectNode>) listOfQueriesAsObject;
        }

        if (listOfArguments == null) {
            listOfArguments = new ArrayList();
        }

        if (json.has("arguments")) {
            arguments.set("arguments", json.get("arguments"));
        }
        if (json.has("level")) {
            level = json.get("level").asInt();
        }
        if (json.has("encabezado")) {
            arguments.set("encabezado", json.get("encabezado"));
        }
        if (listOfArguments.size() > 0 && listOfArguments.size() > level) {
            listOfArguments.set(level, arguments);
        } else {
            listOfArguments.add(level, arguments);
        }
        request.getSession().setAttribute("listOfarguments", listOfArguments);
    }

    @PostMapping(value = {"/saveArgumentsWithQuery"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveArgumentsWithQuery(@RequestBody ObjectNode json,
                                       HttpServletRequest request) throws Exception {

        saveArguments(json, request, "argumentsJson");
//        Map<String, JsonNode> listOfArguments = null;
//
//        Object listOfQueriesAsObject = request.getSession().getAttribute("argumentsJson");
//
//        JsonNode arguments = null;
//        if (listOfQueriesAsObject != null) {
//            listOfArguments = (Map<String, JsonNode>) listOfQueriesAsObject;
//        }
//
//        if (listOfArguments == null) {
//            listOfArguments = new HashMap();
//        }
//
//        if (json.has("arguments")) {
//            arguments = json.get("arguments");
//        }
//
//        listOfArguments.put(json.get("queryId").asText(), arguments);
//        request.getSession().setAttribute("argumentsJson", listOfArguments);
    }

    public void saveArgumentsForReportGraph(@RequestBody ObjectNode json,
                                            HttpServletRequest request) throws Exception {
        saveArguments(json, request, "argumentsForReportGraph");
    }

    private void saveArguments(ObjectNode json, HttpServletRequest request, String arguemntsName) {
        Map<String, JsonNode> listOfArguments = null;

        Object listOfQueriesAsObject = request.getSession().getAttribute(arguemntsName);

        ObjectNode arguments = JsonNodeFactory.instance.objectNode();
        if (listOfQueriesAsObject != null) {
            listOfArguments = (Map<String, JsonNode>) listOfQueriesAsObject;
        }

        if (listOfArguments == null) {
            listOfArguments = new HashMap();
        }

        if (json.has("arguments")) {
            arguments.set("arguments", json.get("arguments"));
        }

        listOfArguments.put(json.get("queryId").asText(), arguments);
        request.getSession().setAttribute(arguemntsName, listOfArguments);
    }

    @GetMapping(value = {"/query/getSavedArguments"}, params = "level", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ObjectNode> getSavedArguments(@QueryParam("level") int level, HttpServletRequest request) throws IOException {
        List<ObjectNode> json = (List<ObjectNode>) request.getSession().getAttribute("listOfarguments");
        logger.info("getSavedArguments = " + json);
        ObjectNode savedarguments = null;
        if (level != -1 && level < json.size()) {
            savedarguments = json.get(level);
        }
//        Object dataToDynamicReport = request.getSession().getAttribute("dataToDynamicReport");
//        List<ObjectNode> newArguments = null;
//        if (dataToDynamicReport != null) {
//            newArguments = (List<ObjectNode>) dataToDynamicReport;
//            ArrayNode rowNodes = (ArrayNode)newArguments.get(level).get("arguments");
//            for (JsonNode columnNode : rowNodes) {
//                ObjectNode node = (ObjectNode) columnNode;
//                ArrayNode arguments = (ArrayNode)savedarguments.get("arguments").get("arguments");
//                arguments.add(node);
//            }
//        }
        return new ResponseEntity<ObjectNode>(savedarguments, HttpStatus.OK);
    }

//    @PostMapping(value = {"/savesubquerydatatodynamicreport"}, produces = MediaType.APPLICATION_JSON_VALUE)
//    public void saveSubQueryDataToDynamicReport(@RequestBody ObjectNode json,
//                                                HttpServletRequest request) throws Exception {
//        ObjectNode query = null;
//        List<ObjectNode> dataToDynamicReport = null;
//
//        Object listOfQueriesAsObject = request.getSession().getAttribute("dataToDynamicReport");
//
//        if (listOfQueriesAsObject != null) {
//            dataToDynamicReport = (List<ObjectNode>) listOfQueriesAsObject;
//        }
//
//        if (dataToDynamicReport == null) {
//            dataToDynamicReport = new ArrayList();
//        }
//        if (json.has("dataToReport")) {
//            query = (ObjectNode) json.get("dataToReport");
//        }
//
//        dataToDynamicReport.add(query);
//        request.getSession().setAttribute("dataToDynamicReport", dataToDynamicReport);
//    }


//    public void removeSubQueryDataToDynamicReport(int level, HttpServletRequest request) {
//
//
//        List<ArrayNode> dataToDynamicReport = null;
//
//        Object listOfQueriesAsObject = request.getSession().getAttribute("dataToDynamicReport");
//        if (level == 0) {
//            request.getSession().setAttribute("dataToDynamicReport", new ArrayList<>());
//        } else {
//            if (listOfQueriesAsObject != null) {
//                dataToDynamicReport = (List<ArrayNode>) listOfQueriesAsObject;
//                if (dataToDynamicReport != null && level > 0) {
//                    for (int i = level - 1; dataToDynamicReport.size() >= level; i--) {
//                        dataToDynamicReport.remove(i);
//                    }
//                    request.getSession().setAttribute("dataToDynamicReport", dataToDynamicReport);
//                }
//            }
//        }
//    }

    @PostMapping(value = {"/saveLastQuery"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveLastQueries(@RequestBody ObjectNode json,
                                HttpServletRequest request) throws Exception {
        int level = 0;
        JsonNode query = null;
        Map<Integer, JsonNode> listOfQueries = null;

        Object listOfQueriesAsObject = request.getSession().getAttribute("listOfQueries");

        if (listOfQueriesAsObject != null) {
            listOfQueries = (Map<Integer, JsonNode>) listOfQueriesAsObject;
        }

        if (listOfQueries == null) {
            listOfQueries = new HashMap<>();
        }
        if (json.has("query")) {
            query = json.get("query");
        }
        if (query != null && query.has("level")) {
            level = query.get("level").asInt();
        }

        listOfQueries.put(level, query);
        request.getSession().setAttribute("listOfQueries", listOfQueries);
    }

    @GetMapping(value = {"/getLastQuery"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> getLastQuery(@QueryParam("level") Integer level,
                                                 HttpServletRequest request) {
//        removeSubQueryDataToDynamicReport(level, request);
        Map<Integer, JsonNode> listOfQueries = (Map<Integer, JsonNode>) request.getSession().getAttribute("listOfQueries");
        JsonNode lastQuery = null;
        if (listOfQueries != null) {
            lastQuery = listOfQueries.get(level);
        }
        logger.info("getLastQuery = " + lastQuery);

        return new ResponseEntity<>(lastQuery, HttpStatus.OK);
    }

    @PostMapping(value = {"/saverowfieldsforreport"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public void saveRowFieldsForReport(@RequestBody ObjectNode json,
                                       HttpServletRequest request) throws Exception {

        int level = 0;
        ObjectNode arguments = JsonNodeFactory.instance.objectNode();

        if (json.has("field")) {
            arguments.setAll(json);
        }

        request.getSession().setAttribute("fieldsValueForReport", arguments);
    }

    /* New Service generated*/

    @GetMapping("/retriveoptionjserver")
    public ResponseEntity<JsonNode> retriveOptionJServer(@QueryParam("option") String option,
                                                         @QueryParam("query") String query,
                                                         @QueryParam("rownumid") Long rownumid) {
        JsonNode response = null;
        if (option.compareTo("onlyjasper") == 0) {
            LVQuery lvQuery = lVQueryRepository.findByQueryId(query);
            if (lvQuery != null) {
                response = new ObjectMapper().createObjectNode();
                ((ObjectNode) response).put("value", lvQuery.getJserver());
            }
        } else {
            LVQueryFields lvQueryFields = lvQueryFieldsRepository.findByRowNumId(rownumid, query);
            if (lvQueryFields != null) {
                response = new ObjectMapper().createObjectNode();
                ((ObjectNode) response).put("value", lvQueryFields.getjServer());
            }
        }
        return new ResponseEntity<JsonNode>(response, HttpStatus.OK);
    }


    @GetMapping("/retrievecredentialsjserver")
    public ResponseEntity<JsonNode> retrievecredentialsjserver() {
        JsonNode credential = new ObjectMapper().createObjectNode();
        // String[] joption = System.getenv().get("JASPERSERVER_OPTION").split(";");
        String[] joption = this.jservercrendentials.split(";");
        ((ObjectNode) credential).put("c", String.format("%s;%s", joption[1], joption[2]));
        ((ObjectNode) credential).put("r", joption[0]);
        return new ResponseEntity<JsonNode>(credential, HttpStatus.OK);
    }

    @GetMapping("/retrieveparamsandvaluesbyreport/{queryid}")
    public ResponseEntity<JsonNode> retrieveParameterAndValuesOfReports(@PathVariable("queryid") String queryid, HttpServletRequest request) {
        JsonNode reponse = null;
        LVQuery lvQuery = lVQueryRepository.findByQueryId(queryid);
        if (lvQuery.getQueryconftype() == null && lvQuery.getReportlocation() == null) {
            List<JsonNode> arguments = new ArrayList<>();
            JsonNode fieldsValueForReport = (JsonNode) request
                    .getSession()
                    .getAttribute("fieldsValueForReport");
            String reportName = this.retrieveNameReport(
                    fieldsValueForReport.get("rownumid").asLong(),
                    queryid
            );

            JsonNode objectCreated = new ObjectMapper().createObjectNode();
            ((ObjectNode) objectCreated).put("key", fieldsValueForReport.get("subqueryarg").textValue());
            ((ObjectNode) objectCreated).put("value", fieldsValueForReport.get("field").textValue());

            arguments.add(objectCreated);

            reponse = new ObjectMapper().createObjectNode();
            ((ObjectNode) reponse).put("report", reportName);
            String reportsTitle = lVQueryRepository.findReportsTitle();
            ((ObjectNode) reponse).put("title", reportsTitle);
            ((ObjectNode) reponse).put("subtitle", lvQuery.getReportSubtitle());
            ((ObjectNode) reponse).putArray("arguments").addAll(arguments);

        } else if (lvQuery.getReportlocation() != null) {

            List<JsonNode> json = (List<JsonNode>) request.getSession().getAttribute("listOfarguments");
            JsonNode jsonParamForReport = null;
            //JsonNode jsonArguments = null;
            List<JsonNode> listJsonArguments = new ArrayList<>();
            for (JsonNode o : json) {
                if (o.path("arguments") != null) {
                    listJsonArguments.add(o.get("arguments"));
                }
            }
            if (!listJsonArguments.isEmpty()) {
                jsonParamForReport = new ObjectMapper().createObjectNode();
                jsonParamForReport = this.generateJsonParamForReport((listJsonArguments));
                ((ObjectNode) jsonParamForReport).put("report", lvQuery.getReportlocation());
                ((ObjectNode) jsonParamForReport).put("title", lvQuery.getReporttitle());
                ((ObjectNode) jsonParamForReport).put("subtitle", lvQuery.getReportSubtitle());
            }
            reponse = jsonParamForReport;
        }
        return new ResponseEntity<JsonNode>(reponse, HttpStatus.OK);
    }

    private JsonNode generateJsonParamForReport(List<JsonNode> listJsonArguments) {
        List<JsonNode> arguments = new ArrayList<>();
        JsonNode jsonFormatted = new ObjectMapper().createObjectNode();
        for (JsonNode json : listJsonArguments) {
            json.get("arguments").forEach(record -> {
                JsonNode objectCreated = new ObjectMapper().createObjectNode();
                ((ObjectNode) objectCreated).put("key", record.get("arginto"));
                ((ObjectNode) objectCreated).put("value", record.get("value"));
                arguments.add(objectCreated);
            });
        }

        ((ObjectNode) jsonFormatted).putArray("arguments").addAll(arguments);
        return jsonFormatted;
    }

    private String retrieveNameReport(Long rownumid, String queryId) {
        LVQueryFields lvQueryFields = lvQueryFieldsRepository.findByRowNumId(rownumid, queryId);
        return (lvQueryFields.getFieldrptlocation() == null) ? "" : lvQueryFields.getFieldrptlocation();
    }
    /*End New Service generated*/

}
