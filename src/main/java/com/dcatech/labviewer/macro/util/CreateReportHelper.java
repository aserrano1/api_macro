package com.dcatech.labviewer.macro.util;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.HorizontalBandAlignment;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.AutoText;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;

import com.dcatech.labviewer.macro.configuration.RoutingDataSourceImpl;
import com.dcatech.labviewer.macro.controller.ReportsController;
import com.dcatech.labviewer.macro.model.DataSourceResult;
import com.dcatech.labviewer.macro.model.LVQueryFields;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CreateReportHelper {

    @Autowired
    TranslateUtil translate;

    @Autowired
    protected RoutingDataSourceImpl localDataSource;

    private final Logger logger = LoggerFactory.getLogger(ReportsController.class);
    private final int anchodeltitulo_en_template = 136;

    private HashMap<String, Integer> arrColumnSpan;


    public byte[] exportReportTo(String format,
                                 JasperReport jr, Map params) throws SQLException {
        JasperPrint jasperPrint = null;

        Connection connection = localDataSource.getConnection();
        try {
            jasperPrint = JasperFillManager.fillReport(jr, params, connection);
        } catch (JRException e) {
            logger.error("Error fillReport: " + e.getMessage(), e);
            e.printStackTrace();
        } finally {
            connection.close();
        }
        try {
            return getBytes(format, jasperPrint);
        } catch (Exception ex) {
            logger.error("Error extrayendo reporte: " + ex.getMessage(), ex);
        }
        return null;
    }

    private static byte[] getBytes(String format, JasperPrint jasperPrint) throws JRException, Exception {
        if (format.equalsIgnoreCase("pdf"))
            return JasperExportManager.exportReportToPdf(jasperPrint);
        else if (format.equalsIgnoreCase("xls")) {
            return exportToXLS(jasperPrint);
        } else if (format.equalsIgnoreCase("docx")) {
            return exportToDocx(jasperPrint);
        }
        return null;
    }

    public static byte[] exportDynamicReportTo(String format, JRDataSource ds,
                                               JasperReport jr, Map params) throws Exception {
        JasperPrint jasperPrint;
        jasperPrint = JasperFillManager.fillReport(jr, params, ds);
        return getBytes(format, jasperPrint);
    }

    public static byte[] exportToXLS(JasperPrint jasperPrint) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        //configuration.setOnePagePerSheet(true);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(false);
        exporter.setConfiguration(configuration);

//        JRXlsExporter exporter = new JRXlsExporter();
//
//        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
//        exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
//        exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//        exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        ByteArrayOutputStream outputFile = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));

        //SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        //configuration.setDetectCellType(true);//Set configuration as you like it!!
        //configuration.setCollapseRowSpcreaan(false);
        //exporter.setConfiguration(configuration);
        exporter.exportReport();

        return outputFile.toByteArray();
    }

    public static byte[] exportToDocx(JasperPrint jasperPrint) throws JRException {
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        ByteArrayOutputStream outputFile = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
        exporter.exportReport();
        return outputFile.toByteArray();
    }

    private JasperReport createJasperReport(DynamicReport dr, LayoutManager layoutManager, Map params) throws JRException {
        return DynamicJasperHelper.generateJasperReport(dr, layoutManager, params);
    }

    public List<String> returnColumnNames(List<LVQueryFields> columnList) {
        logger.info("//************ START returnColumnNames ************//");
        logger.info("columnList size=" + columnList);
        List<String> listOfColumns = new ArrayList<>();
        for (LVQueryFields columnMap : columnList) {
            logger.info("columnMap=" + columnMap);
            listOfColumns.add(columnMap.getFieldalias());
        }
        logger.info("listOfColumns=" + listOfColumns);
        logger.info("//************ END returnColumnNames ************//");
        return listOfColumns;
    }


    public JasperReport
    createDynamicReportWithSpecifiedFormat(List<LVQueryFields> columnList,
                                           Map reportParameters, List listOfResults,
                                           String reportTemplate, boolean useFullPageWidth, String format, DataSourceResult encabezado, String reportname)
            throws JRException, IOException {
        logger.info("//************ START createDynamicReportWithSpecifiedFormat ************//");
        String reportnameLocal = reportname;
        DynamicReport dynamicReport;
        JasperReport jasperReport;
        dynamicReportBuilder = new DynamicReportBuilder();
        JasperDesign jasperDesign = new JasperDesign();

        dynamicReport = createFastDynamicReport(listOfResults, columnList, reportTemplate, useFullPageWidth, format, encabezado, reportnameLocal);

//        JasperDesignDecorator jasperDesignDecorator = new JasperDesignDecorator();
//        jasperDesignDecorator.beforeLayout(jasperDesign, null);
//        dynamicReport.setJasperDesignDecorator(jasperDesignDecorator);

        logger.info("dynamicReport= " + dynamicReport.toString());
//        dynamicReport.set
        jasperReport = createJasperReport(dynamicReport, getLayoutManager(), reportParameters);
//        JRBand title = jasperReport.getTitle();
//
//        JRBand jrband =new JRDesignBand();

        logger.info("jasperReport= " + jasperReport.toString());
        logger.info("//************ END createDynamicReportWithSpecifiedFormat ************//");
        return jasperReport;
    }

    public HttpHeaders createContentTypeHeader(String format, String reportName) throws UnsupportedEncodingException {
        logger.info("//************ START createContentTypeHeader ************//");
        HttpHeaders headers = new HttpHeaders();
        logger.info("format= " + format);
        logger.info("reportName= " + reportName);
        //if (format.equalsIgnoreCase("pdf"))
        //headers.setContentType(MediaType.parseMediaType("application/pdf"));
        //else if (format.equalsIgnoreCase("xlsx"))
        //headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        StringBuilder reportNameBuilder = new StringBuilder();
        reportNameBuilder.append(java.net.URLDecoder.decode(reportName, "UTF-8"));
        headers.setCacheControl("max-age=0");
        headers.setContentType(MediaType.parseMediaType("multipart/form-data"));
        reportNameBuilder.append(".");
        reportNameBuilder.append(format);
        logger.info("reportNameBuilder= " + reportNameBuilder.toString());
        headers.setContentDispositionFormData("filename", reportNameBuilder.toString());
        logger.info("headers= " + headers.toString());
        logger.info("//************ END createContentTypeHeader ************//");
        return headers;
    }

    private Style getColumnStyle(String styleName, Color color, Font font) {
        logger.info("//************ START getColumnStyle ************//");
        if (color != null) {
            logger.info("color= " + color.toString());
        } else {
            logger.info("color= null");
        }
        logger.info("font= " + font.toString());
        logger.info("styleName= " + styleName);
        Style style = new Style();
        style.setBlankWhenNull(true);
        style.setBorder(new Border(0.5f));
        style.setStretchWithOverflow(true);
        style.setName(styleName);
        style.setFont(font);
        if (color != null)
            style.setBackgroundColor(color);
        logger.info("style= " + style);
        logger.info("//************ END getColumnStyle ************//");
        return style;
    }

    private DynamicReportBuilder dynamicReportBuilder;

    private DynamicReport createFastDynamicReport(List<String[]> values, List<LVQueryFields> columns,
                                                  String reportTemplate, boolean useFullPageWidth, String format,
                                                  DataSourceResult encabezado, String reportname) throws IOException {
        logger.info("//************ START createFastDynamicReport ************//");
        logger.info("reportTemplate= " + reportTemplate);
        logger.info("UseFullPageWidth= " + useFullPageWidth);
        logger.info("format= " + format);
        String reportnameLocal = reportname;
        // Generación de
        this.arrColumnSpan = new HashMap<String, Integer>();
        for (LVQueryFields record : columns) {
            String columHeader = (record.getColumnHeader() == null) ? "A" : record.getColumnHeader();
            if (!(this.arrColumnSpan.size() == 0)) {
                Integer value = (this.arrColumnSpan.get(columHeader) == null) ? -1 : this.arrColumnSpan.get(columHeader);

                if (value == -1) {
                    this.arrColumnSpan.put(columHeader, 1);
                } else {
                    Integer i = this.arrColumnSpan.get(columHeader);
                    this.arrColumnSpan.put(columHeader, ++i);
                }
                System.out.println(String.format("%s --> %s", columHeader, this.arrColumnSpan.get(columHeader)));
            } else {
                System.out.println(String.format("%s --> %s", columHeader, 1));
                this.arrColumnSpan.put(columHeader, 1);
            }

        }


        Color color = new Color(150, 150, 150);
        Style columnStyle = getColumnStyle("Columns style", null, Font.ARIAL_SMALL);
        logger.info("columnStyle= " + columnStyle.toString());
        Style headerStyle = getColumnStyle("Header style", color, Font.ARIAL_MEDIUM);
        headerStyle.setTransparent(false);
        logger.info("headerStyle= " + headerStyle.toString());

        ClassPathResource res = new ClassPathResource(reportTemplate);

        if (res != null) {
            logger.info("res= " + res.toString());

            dynamicReportBuilder.setTemplateFile(reportTemplate);


        } else {
            logger.info("res= null");
            logger.info("//************ END createFastDynamicReport ************//");
            throw new java.io.FileNotFoundException(reportTemplate + " not found");
        }
        if (format.equals("xls")) {
            dynamicReportBuilder.setIgnorePagination(true)
                    .setMargins(0, 0, 0, 0);

        }
        dynamicReportBuilder.setReportName(reportnameLocal);
        dynamicReportBuilder.setUseFullPageWidth(true);
        Page page = new Page();
        page.setWidth(totalwidth);
        dynamicReportBuilder.setPageSizeAndOrientation(page);
        dynamicReportBuilder.setProperty("net.sf.jasperreports.export.pdf.size.page.to.content", "true");
        //Page page = Page.Page_A4_Landscape();
        //page.setWidth(2000);
        //dynamicReportBuilder.setPageSizeAndOrientation(page);
        //logger.info("PageSizeAndOrientation= " + page.toString());
        //dynamicReportBuilder.setUseFullPageWidth(useFullPageWidth);

        AbstractColumn reportColumn;
        if (values.isEmpty()) {
            logger.info("Values empty");
            //dynamicReportBuilder.setWhenNoDataBlankPage();
            logger.info("Creating DataBlankPage");
            DynamicReport dynamicReport = dynamicReportBuilder.build();
            logger.info("//************ END createFastDynamicReport ************//");
            return dynamicReport;
        }
        columns.get(0).setWidthcolumn("25");
        //para evitar que se creen columna en blanco se debe setear en ancho de la primera columna a este numero;
        logger.info("Iterando cantidad de columnas...");
        int totalancho = 0;
        int offset = 50;
        boolean columnaAjustada = false;
        for (int columnPosition = 0; columnPosition < columns.size(); columnPosition++) {
            Object[] data = values.get(0);

            Object obj = data[columnPosition];

            int anch_columna = Integer.parseInt(columns.get(columnPosition).getWidthcolumn());
            totalancho += anch_columna;
            if (totalancho > anchodeltitulo_en_template && !columnaAjustada) {
                columnaAjustada = true;
                anch_columna -= totalancho - anchodeltitulo_en_template;
                if (anch_columna <= 0) {
                    anch_columna = 20;
                }

            }
            totalancho = totalancho;
            anch_columna = anch_columna;


            if (obj == null) {
                reportColumn = createAstractColumn(Object.class.getName(),
                        columns.get(columnPosition).getFieldalias(),
                        columns.get(columnPosition).getFieldlabel(),
                        columnStyle,
                        anch_columna);

                logger.info("Agregando HeaderStyle");
                reportColumn.setHeaderStyle(headerStyle);
                logger.info("Agregando Columna");
                dynamicReportBuilder.addColumn(reportColumn);
                logger.info("Agregando HeaderStyle");
            } else {
                logger.info("Tipo de dato de la columna: " + columns.get(columnPosition).getFieldlabel() + "= " + obj.toString());
                if (obj instanceof String) {
                    reportColumn = createAstractColumn(String.class.getName(),
                            columns.get(columnPosition).getFieldalias(),
                            columns.get(columnPosition).getFieldlabel(), columnStyle,
                            anch_columna);
                    logger.info("Agregando HeaderStyle");
                    reportColumn.setHeaderStyle(headerStyle);
                    logger.info("Agregando Columna");
                    reportColumn.setMarkup("html");
                    dynamicReportBuilder
                            .addColumn(reportColumn);

                    logger.info("Agregando HeaderStyle");
                } else {
                    if (obj instanceof Date) {
                        reportColumn = createAstractColumn(Date.class.getName(),
                                columns.get(columnPosition).getFieldalias(),
                                columns.get(columnPosition).getFieldlabel(), columnStyle,
                                anch_columna);
                        reportColumn.setHeaderStyle(headerStyle);
                        reportColumn.setPattern("dd/MM/yyyy HH:mm:ss");
                        reportColumn.setMarkup("html");
                        dynamicReportBuilder.addColumn(reportColumn);
                    } else {
                        if (obj instanceof BigDecimal) {
                            reportColumn = createAstractColumn(BigDecimal.class.getName(),
                                    columns.get(columnPosition).getFieldalias(),
                                    columns.get(columnPosition).getFieldlabel(), columnStyle,
                                    anch_columna);
                            reportColumn.setHeaderStyle(headerStyle);
                            reportColumn.setMarkup("html");
                            dynamicReportBuilder.addColumn(reportColumn);

                        } else {
                            if (obj == null) {
                                reportColumn = createAstractColumn(String.class.getName(),
                                        columns.get(columnPosition).getFieldalias(),
                                        columns.get(columnPosition).getFieldlabel(), columnStyle,
                                        anch_columna);
                                reportColumn.setHeaderStyle(headerStyle);
                                dynamicReportBuilder.addColumn(reportColumn);
                            } else {
                                if (obj instanceof Integer) {
                                    reportColumn = createAstractColumn(Integer.class.getName(),
                                            columns.get(columnPosition).getFieldalias(),
                                            columns.get(columnPosition).getFieldlabel(), columnStyle,
                                            anch_columna);
                                    reportColumn.setHeaderStyle(headerStyle);
                                    reportColumn.setMarkup("html");
                                    dynamicReportBuilder.addColumn(reportColumn);

                                } else {
                                    if (obj instanceof Double) {
                                        reportColumn = createAstractColumn(Double.class.getName(),
                                                columns.get(columnPosition).getFieldalias(),
                                                columns.get(columnPosition).getFieldlabel(), columnStyle,
                                                anch_columna);
                                        reportColumn.setHeaderStyle(headerStyle);
                                        reportColumn.setMarkup("html");
                                        dynamicReportBuilder.addColumn(reportColumn);

                                    } else {
                                        if (obj instanceof Float) {
                                            reportColumn = createAstractColumn(Float.class.getName(),
                                                    columns.get(columnPosition).getFieldalias(),
                                                    columns.get(columnPosition).getFieldlabel(), columnStyle,
                                                    anch_columna);
                                            reportColumn.setHeaderStyle(headerStyle);
                                            reportColumn.setMarkup("html");
                                            dynamicReportBuilder.addColumn(reportColumn);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }

        int initColumnSpan = 0;
        if (!(this.arrColumnSpan.size() == 1)) {
            this.arrColumnSpan = arrColumnSpan.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            for (Map.Entry<String, Integer> entry : this.arrColumnSpan.entrySet()) {
                int a = entry.getValue();
                String b = (entry.getKey() == "A") ? "" : entry.getKey();
                dynamicReportBuilder.setColspan(initColumnSpan, a, b).setDefaultStyles(headerStyle, headerStyle, headerStyle, headerStyle);
                initColumnSpan += a;
            }
        }


        logger.info("Build dynamicReport");

        totalwidth = 0;
        DynamicReport dynamicReport = dynamicReportBuilder.build();

        if (encabezado != null) {
            Style headerTextStyle = getColumnStyle("HeaderText style", null, Font.ARIAL_SMALL);
            headerTextStyle.setStretchWithOverflow(true);
            headerTextStyle.setHorizontalAlign(HorizontalAlign.LEFT);
            headerTextStyle.setVerticalAlign(VerticalAlign.MIDDLE);
            headerTextStyle.setBorder(new Border(0.0f));
            columnStyle.setStretchWithOverflow(true);
            List<AutoText> autoTexts = new ArrayList();
            AutoText label = null;
            String valuesdata = "";
            for (int i = 0; i < encabezado.getData().size(); i++) {
                HashMap datosMap = ((HashMap) encabezado.getData().get(i));
                Collection datosValues = datosMap.values();

                for (int j = 1; j <= datosValues.size(); j += 3) {

                    LinkedHashMap queryField = (LinkedHashMap) encabezado.getColumns().get(j);
                    LinkedHashMap queryField2 = null;
                    LinkedHashMap queryField3 = null;
                    if (encabezado.getColumns().size() > j + 1) {
                        queryField2 = (LinkedHashMap) encabezado.getColumns().get(j + 1);
                        if (encabezado.getColumns().size() > j + 2) {
                            queryField3 = (LinkedHashMap) encabezado.getColumns().get(j + 2);
                        }
                    }


                    valuesdata += queryField.get("fieldlabel") + "  :   " + datosMap.get(queryField.get("fieldalias").toString());
                    if (queryField2 != null) {
                        valuesdata += "    " + queryField2.get("fieldlabel") + "  :   " + datosMap.get(queryField2.get("fieldalias").toString());

                        if (queryField3 != null) {
                            valuesdata += "    " + queryField3.get("fieldlabel") + "  :   " + datosMap.get(queryField3.get("fieldalias").toString());

                        }

                    }

                    valuesdata += "\\n";
                }
            }

            label = new AutoText(valuesdata, AutoText.POSITION_HEADER,
                    HorizontalBandAlignment.LEFT, 681);


            label.setStyle(headerTextStyle);
            label.setFixedWith(false);
            autoTexts.add(label);

            dynamicReport.setAutoTexts(autoTexts);
        }
        dynamicReport.setProperty("net.sf.jasperreports.export.pdf.size.page.to.content", "true");
        logger.info("dynamicReport= " + dynamicReport.toString());
        logger.info("//************ END createFastDynamicReport ************//");
        return dynamicReport;
    }

    int totalwidth = 50;

    private AbstractColumn createAstractColumn(String className, String property, String title, Style columnStyle, int width) {
        logger.info("//************ START createAstractColumn ************//");
        logger.info("className=" + className);
        logger.info("property=" + property);
        logger.info("title=" + title);
        logger.info("columnStyle=" + columnStyle.toString());
        logger.info("width=" + width);
        int columnwidth = width * 5;
        totalwidth += columnwidth;
        AbstractColumn reportColumn = ColumnBuilder.getNew()
                .setColumnProperty(property, className)
                .setTitle(title)
                .setWidth(columnwidth)
                .setStyle(columnStyle)
                .addFieldProperty("net.sf.jasperreports.export.xlsx.wrap.text", "false")
                .setFixedWidth(true)
                .build();
        logger.info("reportColumn= " + reportColumn.toString());
        logger.info("//************ END createAstractColumn ************//");
        return reportColumn;
    }

    private LayoutManager getLayoutManager() {
        return new ClassicLayoutManager();
    }


    public JasperReport buildExistingReport(String reportLocation) throws JRException {
        logger.info("//************ START buildExistingReport ************//");
        JasperReport jasperReport = null;
        logger.info("Cargando Objeto jasperReport...");
        try {
            jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportLocation);
        } catch (JRException ex) {
            logger.error("Error Cargando Objeto jasperReport... " + ex.getMessage(), ex);
            throw ex;
        }
        logger.info("//************ END buildExistingReport ************//");
        return jasperReport;
    }

}
