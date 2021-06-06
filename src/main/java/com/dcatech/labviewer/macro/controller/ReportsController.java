package com.dcatech.labviewer.macro.controller;

import com.dcatech.labviewer.macro.model.*;
import com.dcatech.labviewer.macro.repository.*;
//import com.dcatech.labviewer.macro.model.service.rest.LVQueryRestController;
import com.dcatech.labviewer.macro.service.rest.LVQueryRestController;
import com.dcatech.labviewer.macro.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class ReportsController {
    @Autowired
    USaveReportParamRepository uSaveReportParamRepository;
    @Autowired
    TranslateUtil translate;

    @Autowired
    LVQueryRestController lvQueryRestController;

    @Autowired
    LVAttachmentRepository lvAttachmentRepository;

    @Autowired
    LVQueryRepository lVQueryRepository;

    @Autowired
    CreateReportHelper reportHelper;

    @Autowired
    LVQueryRepository lvQueryRepository;

    @Autowired
    LVQueryFieldsRepository lvQueryFieldsRepository;


    @Autowired
    CreateReportWithPOIExcel createReportWithPOIExcel;

    @Autowired
    CreateReportWithPOIExcelForRepCombinado createReportWithPOIExcelForRepCombinado;

    @Autowired
    CreateReportForMacroSubmission createReportForMacroSubmission;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private DataSourceResult encabezado;


    private HttpHeaders headers;

    private String reportTemplate;
    private Map reportParameters;
    private JasperReport jasperReport;
    private byte[] byteArrayOutputStream;
    private String labvantage_home = System.getenv().get("LABVANTAGE_HOME").replaceAll("\"", "/");
    private String reportLocation = labvantage_home + "applications/labvantage/reports/labviewer/";//C:\App\labvantagehome\applications\labvantage\reports\OOB
    private JsonNode arguments = null;
    private LVQuery byQueryId = null;
    private Map argumentsWithValue;
    private Map argumentsForDynamicReportWithValue;
    private String argumentWithJasper = "";

    @Transactional(readOnly = true)
    @RequestMapping(value = "/reporte/dynamicreport/{queryId}/{format}/{level}", method = RequestMethod.GET,
            params = {"reportName"})
    @Produces({"application/vnd.ms-excel", "application/pdf"})
    public ResponseEntity<byte[]> createAndExportDynamicReport(@PathVariable("format") String format,
                                                               @RequestParam("reportName") String reportName,
                                                               @PathVariable("queryId") String queryId,
                                                               @PathVariable("level") int level,
                                                               HttpServletRequest request) throws Exception {
        logger.info("//************ START CREATING DYNAMIC REPORT ************//");
        logger.info("format = " + format);
        logger.info("reportName = " + reportName);
        logger.info("queryId = " + queryId);

        String nameReport = reportName;

        List<ObjectNode> json = (List<ObjectNode>) request.getSession().getAttribute("listOfarguments");
        logger.info("getLastQueries = " + json);
        argumentsWithValue = new HashMap();
        argumentsForDynamicReportWithValue = new TreeMap();
        List<Argument> arguments = new ArrayList();

        arguments.addAll(extractArgumentsFromSession(queryId, true, json.get(level).get("arguments"), arguments.size()));


        Map p = loadDynamicReportParametersCopy(request, format, queryId, level);
        DataSourceResult encabezado = null;
        if (json.get(level).has("encabezado") && json.get(level).get("encabezado").size() > 0) {
            ObjectMapper mapper = new ObjectMapper();
            encabezado = mapper.convertValue(json.get(level).get("encabezado"), DataSourceResult.class);
        }


        try {
            BufferedImage imagenLogo = getReportLogoAndTitle("ImagenLogo");
            p.put("IMAGEN", imagenLogo);
        } catch (IOException e) {
            logger.error("REPORTES: Error cargando parámetros para el reporte dinámico", e);
        }
        List listOfResults;
        List<LVQueryFields> lvQueryFieldsList;

        if (byQueryId.getQueryconftype() != null) {
            if (byQueryId.getQueryconftype().equalsIgnoreCase("special")) {

                if (queryId.equals("RPTE_SUBMISSION_HIST_JAS")) {
                    byte[] in = this.createReportForMacroSubmission.createReport(byQueryId, arguments, format);

                    String dateNow = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    String random = String.valueOf(Math.round(Math.random() * 100000));
                    String attachment = String.format("attachment; filename=%s_%s_%s.xls", reportName, dateNow, random);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Disposition", attachment);

                    return ResponseEntity
                            .ok()
                            .headers(headers)
                            .body(in);

                } else {
                    byte[] in = this.createReportWithPOIExcel.createReport(byQueryId, arguments, 0, 0, format, nameReport);

                    String dateNow = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                    String random = String.valueOf(Math.round(Math.random() * 100000));
                    String attachment = String.format("attachment; filename=%s_%s_%s.xlsx", reportName, dateNow, random);
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Disposition", attachment);

                    return ResponseEntity
                            .ok()
                            .headers(headers)
                            .body(in);
                }
            }
        }


        if (byQueryId != null && byQueryId.getQueryconftype() != null && byQueryId.getQueryconftype().equals("procedure")) {
            int procedureformat = mapformatToInt(format);
            listOfResults = lvQueryRepository.executeQueryGridWithProcedure(byQueryId, arguments, 0, 0, procedureformat);
            logger.info("listOfResults= " + listOfResults.size());
            lvQueryFieldsList = lvQueryFieldsRepository.findByProcedure(byQueryId, arguments, procedureformat);
            lvQueryFieldsList.remove(0);

        } else {

            String queryForReport = createQueryForReport(format, arguments);

            listOfResults = lVQueryRepository.executeQuery(queryForReport);
            logger.info("listOfResults= " + listOfResults.size());
            lvQueryFieldsList = lvQueryFieldsRepository.findByQueryIdtodynamicReport(queryId, format);
            logger.info("lvQueryFieldsList size= " + lvQueryFieldsList.size());
        }
        JRDataSource reportDataSource = null;
        try {
            reportDataSource = this.loadReportData(format, listOfResults, lvQueryFieldsList);
        } catch (Exception e) {
            logger.error("REPORTES: Error cargando datos del reporte dinámico", e);
        }
        boolean useFullPageWidth = format.equals("pdf");
        logger.info("useFullPageWidth= " + useFullPageWidth);

        String errors = "";

        try {
            reportTemplate = labvantage_home + "applications/labvantage/reports/labviewer/templates/template.jrxml";
            logger.info("reportTemplate = " + reportTemplate);


            jasperReport = reportHelper.createDynamicReportWithSpecifiedFormat(lvQueryFieldsList,
                    p, listOfResults, reportTemplate, useFullPageWidth, format, encabezado, nameReport);

            logger.info("jasperReport= " + jasperReport.toString(), queryId);
        } catch (JRException e) {
            logger.error("REPORTES: Error creando el reporte dinámico", e);
        } catch (IOException e) {
            logger.error("REPORTES: Error creando el reporte dinámico", e);
        }
        try {
            byteArrayOutputStream = reportHelper.exportDynamicReportTo(format, reportDataSource,
                    jasperReport, p);
        } catch (Exception e) {
            logger.error("REPORTES: Error exportando el reporte dinámico", e);
        }
        ResponseEntity<byte[]> response = null;
        try {
            response = createResponseCopy(format, nameReport);
        } catch (UnsupportedEncodingException e) {
            logger.error("REPORTES: Error creando respuesta para el reporte dinámico", e);
        }
        logger.info("//************ END CREATING DYNAMIC REPORT ************//");
        return response;

    }

//-----------------------------------------------------------------------------------
    @Transactional(readOnly = true)
    @RequestMapping(value = "/reporte/staticreport/{queryId}/{format}", method = RequestMethod.GET)
    @Produces({"application/vnd.ms-excel", "application/pdf"})
    public ResponseEntity<byte[]> createAndExportStaticReport(HttpServletRequest request,
                                                              @PathVariable("format") String format,
                                                              @PathVariable("queryId") String queryId,
                                                              @QueryParam("level") int level,
                                                              HttpServletResponse httpResponse) throws JRException {
        logger.info("//************Creando Reporte Estatico ************//");

        StringBuilder report = null;
        StringBuilder reportName = null;
        String NameExportReport = "";

        try {
            List<ObjectNode> json = (List<ObjectNode>) request.getSession().getAttribute("listOfarguments");
            JsonNode arguments = json.get(level).get("arguments");
            loadReportParameters(request, format, queryId, arguments);
            reportParameters.put("REPORTEXECID", saveReportarguments(arguments.toString(), queryId, request.getSession().getId()));
            reportParameters.put("EXPORT_URL", "/reporte/savedstaticreport/" + queryId + "/" + reportParameters.get("REPORTEXECID") + "/" + byQueryId.getRownumid());
            reportParameters.put("EXPORT_XLS_URL", "/reporte/staticreport/xls/" + byQueryId.getQueryid() + "?level=" + level);
            reportParameters.put("EXPORT_PDF_URL", "/reporte/staticreport/pdf/" + byQueryId.getQueryid() + "?level=" + level);
            reportParameters.put("EXPORT_DOCX_URL", "/reporte/staticreport/docx/" + byQueryId.getQueryid() + "?level=" + level);
            String reportNm = argumentWithJasper != "" ? argumentWithJasper : byQueryId.getReportlocation();
            List<Argument> arguments1 = new ArrayList();
            arguments1.addAll(extractArgumentsFromSession(queryId, false, json.get(level).get("arguments"), arguments.size()));
            List<Map> arguments2 = new ArrayList<>();
            arguments2.addAll(reportParameters.values());


            NameExportReport = byQueryId.getReportlocation();
            if (NameExportReport == null || NameExportReport.equals("")) {
                NameExportReport = byQueryId.getQueryid();
            }

            if (queryId.equals("RepBiotecnologiaCombinado")) {
                byte[] in = this.createReportWithPOIExcelForRepCombinado.createReport(arguments1, 0, 0, format);
                String dateNow = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                String random = String.valueOf(Math.round(Math.random() * 100000));
                String attachment = String.format("attachment; filename=%s_%s_%s.xlsm", NameExportReport, dateNow, random);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", attachment);

                return ResponseEntity
                        .ok()
                        .headers(headers)
                        .body(in);
            } else {


                //reportName = new StringBuilder(byQueryId.getReportlocation()).append(".jasper");
                //reportName = new StringBuilder(byQueryId.getReportlocation()).append(".jasper");
                reportName = new StringBuilder(reportNm).append(".jasper");
                logger.info("reportName= " + reportName);
                report = new StringBuilder(reportLocation + reportName);
                logger.info("report= " + report);
            }
        } catch (IOException e) {
            logger.error("REPORTE: Excepción cargando parametros del query " + queryId, e);
        }
        argumentWithJasper = "";

        jasperReport = reportHelper.buildExistingReport(report.toString());


        try {
            byteArrayOutputStream = reportHelper.exportReportTo(format,
                    jasperReport, reportParameters);
        } catch (Exception e) {
            logger.error("REPORTE: Excepción exportando reporte estático " + report.toString(), e);
            byteArrayOutputStream = null;
        }
        ResponseEntity<byte[]> response = null;
        try {
            response = createResponseCopy(format, NameExportReport);
            byteArrayOutputStream = null;
        } catch (UnsupportedEncodingException e) {
            logger.error("REPORTE: Excepción creando respuesta para el reporte " + report.toString(), e);
        }
        logger.info("//************END Creando Reporte Estatico ************//");


        return response;
    }


    private ResponseEntity<byte[]> createResponseCopy(String format, String reportName) throws UnsupportedEncodingException {
        logger.info("//************Creando ResponseEntity ************//");
        logger.info("byteArrayOutputStream= " + byteArrayOutputStream);
        if (byteArrayOutputStream != null) {

            logger.info("byteArrayOutputStream!=null");
            format = (format.equalsIgnoreCase("xls")) ? "xlsx" : format;
            String dateNow = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String random = String.valueOf(Math.round(Math.random() * 100000));
            String attachment = String.format("attachment; filename=%s_%s_%s.%s", reportName, dateNow, random, format);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", attachment);

            logger.info("headers= " + headers);
            logger.info("//************END Creando ResponseEntity ************//");
            return new ResponseEntity<byte[]>(byteArrayOutputStream, headers, HttpStatus.OK);
        } else {
            logger.info("byteArrayOutputStream=null");
            headers = new HttpHeaders();
            logger.info("//************END Creando ResponseEntity ************//");

            return new ResponseEntity<byte[]>(null, headers, HttpStatus.OK);
        }
    }
    private BufferedImage getReportLogoAndTitle(String keyId1) throws IOException {
        logger.info("//************Obteniendo logo del reporte: ************//");
        logger.info("keyId1= " + keyId1);
        LVAttachment imagenLogo = lvAttachmentRepository.findLastAttachmentByKeyId1(keyId1);
        logger.info("Nombre del archivo " + imagenLogo.getFileName());
        logger.info("Convirtiendo a ByteArray");
        InputStream in = new ByteArrayInputStream(imagenLogo.getAttachment());
        logger.info("Creando BufferedImage");
        BufferedImage bImageFromConvert = ImageIO.read(in);
        logger.info("//************END Obteniendo logo del reporte: ************//");
        return bImageFromConvert;
    }
    private List<Argument> extractArgumentsFromSession(String queryId, boolean isDynamicReport, JsonNode jsonNode, int indicador) {

        String val;
        String tex;

        if (jsonNode != null) {
            List<Argument> argumentsJson = lvQueryRestController.getArgsIntoJson(jsonNode, true);
            String nameWithOutBrackets;
            if (argumentsJson != null) {

                for (Argument arg : argumentsJson) {
                    indicador++;
                    if (arg.getValue().contains(".jasper")) {
                        argumentWithJasper = arg.getValue().replace(".jasper", "");
                    }

                    nameWithOutBrackets = arg.getName().replace("[", "");
                    nameWithOutBrackets = nameWithOutBrackets.replace("]", "");

                    if (arg.getValue() == "") {
                        argumentsWithValue.put(nameWithOutBrackets, null);
                    } else {
                        argumentsWithValue.put(nameWithOutBrackets, (arg.getValue()));
                    }
                    //Validacion de Parametros del reporte
                    val = arg.getValue();
                    tex = arg.getText();

                    if (!((val.equals("") || val == null) || val.equals("null"))) {
                        val = tex;
                    }

                    if ((tex.equals("") || tex == null) || tex.equals("Seleccione")) {
                        val = arg.getValue();
                    }

                    if (val.equals("null") || val.equals("") || val == null) {
                        val = "";
                    }

                    if ((val.equals("null") || val.equals("") || val == null) && (tex.equals("") || tex == null) || tex.equals("Seleccione")) {
                        val = "";
                    }
                    //Fin Validacion de Parametros del reporte
                    if (isDynamicReport && !arg.getName().equals("[SYSUSERID]")) {
                        argumentsForDynamicReportWithValue.put((indicador < 10 ? "0" + indicador : indicador) + ". " + arg.getTitle(),
                                val);
                    }

                }

            }
            return argumentsJson;
        }
        return null;
    }
    private Map loadDynamicReportParametersCopy(HttpServletRequest request, String format, String queryId, int level) throws IOException {

        byQueryId = lvQueryRepository.findByQueryId(queryId);
        Map reportParam = new HashMap();
        logger.info("//************Cargando Parametros del reporte ************//");
        reportParam.put("REPORTS_DIR", reportLocation.toString());
        reportParam.put("host", request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath());
        reportParam.put("jasperprint", format);
        if (format.equalsIgnoreCase("pdf") || format.equalsIgnoreCase("docx")) {
            reportParam.put("mode", "pdfbytes");
        } else {
            reportParam.put("mode", "xls");
        }
        String reportsTitle = lVQueryRepository.findReportsTitle();

        reportParam.put("SUBREPORT_DIR", "");
        reportParam.put("TITULO", reportsTitle);
        reportParam.put("SUBTITULO", byQueryId.getReportSubtitle());
        reportParam.put("REPORTNAME", reportsTitle);
        reportParam.put("QUERYID", byQueryId.getQueryid());
        reportParam.put("NODATAFOUND", translate.localizeMessage("nodata"));
        reportsTitle = null;
        logger.info("reportParameters= " + reportParam.toString());
        logger.info("Cantidad reportParameters= " + reportParam.size());
        logger.info("//************END Cargando Parametros del reporte ************//");

        return buildDynamicReportMessageCopy(reportParam);

    }

   private void loadReportParameters(HttpServletRequest request, String format, String queryId, JsonNode json) throws IOException {
    byQueryId = lvQueryRepository.findByQueryId(queryId);
    reportParameters = new HashMap();
    logger.info("//************Cargando Parametros del reporte ************//");
    reportParameters.put("REPORTS_DIR", reportLocation.toString());
    reportParameters.put("host", request.getScheme() + "://" + request.getServerName() + ":" + request.getLocalPort() + request.getContextPath());
    reportParameters.put("jasperprint", format);
    if (format.equalsIgnoreCase("pdf") || format.equalsIgnoreCase("docx")) {
        reportParameters.put("mode", "pdfbytes");
    } else {
        reportParameters.put("mode", "xls");
    }
    reportParameters.put("SUBREPORT_DIR", "");
    String reportsTitle = lVQueryRepository.findReportsTitle();
    reportParameters.put("TITULO", reportsTitle);
    reportParameters.put("SUBTITULO", byQueryId.getReportSubtitle());
    reportParameters.put("REPORTNAME", reportsTitle);
    reportParameters.put("QUERYID", byQueryId.getQueryid());
    reportParameters.put("NODATAFOUND", translate.localizeMessage("nodata"));
    argumentsWithValue = new HashMap();
    extractArgumentsFromSession(queryId, false, json, 0);
    Map<String, JsonNode> argumentos = (Map<String, JsonNode>) request.getSession().getAttribute("argumentsForReportGraph");
    if (argumentos != null) {
        extractArgumentsFromSession(queryId, false, argumentos.get(queryId), 0);
    }
    reportParameters.putAll(argumentsWithValue);
    logger.info("reportParameters= " + reportParameters.toString());
    logger.info("Cantidad reportParameters= " + reportParameters.size());
    logger.info("//************END Cargando Parametros del reporte ************//");

}
    private String saveReportarguments(String arguments, String queryid, String sessionId) {
        String insertClause = "insert into u_SAVEREPORTPARAM(ARGUMENTS, FECHA_REGISTRO, REPORTUSEREXEC, REPORTQUERYID)" +
                " values (:arguments,sysdate,:userid,:reportqueryid)";
        USaveReportParam uSaveReportParam = new USaveReportParam();
        uSaveReportParam.setArguments(arguments);
        uSaveReportParam.setArgumentsid(new Date().getTime());
        uSaveReportParam.setFechaRegistro(new Date());
        uSaveReportParam.setReportqueryid(queryid);
        uSaveReportParam.setReportuserexec(HomeController.getUsuario().getSysUserId());
        uSaveReportParam.setuSavereportparamid(sessionId);
        return uSaveReportParamRepository.saveAndFlush(uSaveReportParam).getuSavereportparamid();

    }
    private int mapformatToInt(String format) {

        if (format.equalsIgnoreCase("pdf")) return 1;
        if (format.equalsIgnoreCase("xls")) return 2;
        return 1;
    }
    public String createQueryForReport(@PathVariable("format") String format, List<Argument> parametersMap) {
        String queryForReport = null;
        if (format != null) {
            if (format.equalsIgnoreCase("pdf") || format.equalsIgnoreCase("docx")) {
                queryForReport = lvQueryRestController.createQuerySentenceForPdfReport(byQueryId, parametersMap);
            } else {
                queryForReport = lvQueryRestController.createQuerySentenceForExcelReport(byQueryId, parametersMap);
            }
        } else {
            queryForReport = lvQueryRestController.createQuerySentenceForReport(byQueryId, parametersMap);
        }
        return queryForReport;
    }
    private JRDataSource loadReportData(String format, List listOfResults, List<LVQueryFields> lvQueryFieldsList) throws Exception {
        List<String> columnNames = reportHelper.returnColumnNames(lvQueryFieldsList);
        logger.info("//************Cargando datos del reporte ************//");
        JRDataSource reportDataSource = null;
        if (byQueryId != null && byQueryId.getQueryconftype() != null && byQueryId.getQueryconftype().equals("procedure")) {
            reportDataSource = new HibernateQueryResultDataSource(listOfResults, columnNames);
            logger.info("reportDataSource= " + reportDataSource.toString());
        } else {
            reportDataSource = new HibernateQueryResultDataSource(listOfResults, columnNames);
            logger.info("reportDataSource= " + reportDataSource.toString());
        }
        logger.info("//************END Cargando datos del reporte ************//");
        return reportDataSource;
    }

    private Map buildDynamicReportMessageCopy(Map param) {
        StringBuilder dynamicParametersMessage = null;
        if (argumentsForDynamicReportWithValue != null && argumentsForDynamicReportWithValue.size() > 0) {
            dynamicParametersMessage = new StringBuilder();
            dynamicParametersMessage.append("Parámetros Seleccionados: \n");
            for (Object key : argumentsForDynamicReportWithValue.keySet()) {
                if (key != null && key.toString().trim().length() > 0) {
                    dynamicParametersMessage.append(key.toString());
                    dynamicParametersMessage.append(": ");
                    String valor = argumentsForDynamicReportWithValue.get(key.toString()).toString();
                    if (valor != null) {
                        dynamicParametersMessage.append((valor.equals("AAAAAAA") ? "Todos" : valor));
                    } else {
                        dynamicParametersMessage.append(" ");
                    }
                    dynamicParametersMessage.append("      ");
                }
            }
        }
        param.put("PARAMETROS_DINAMICOS", dynamicParametersMessage);
        return param;
    }
}