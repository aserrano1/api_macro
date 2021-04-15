package com.dcatech.labviewer.macro.util;

import com.dcatech.labviewer.macro.model.Argument;
import com.dcatech.labviewer.macro.repository.LVQueryFieldsRepository;
import com.dcatech.labviewer.macro.repository.LVQueryRepository;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CreateReportWithPOIExcelForRepCombinado {

    @Autowired
    LVQueryRepository lVQueryRepository;

    @Autowired
    LVQueryFieldsRepository lvQueryFieldsRepository;


    @Autowired
    LVQueryRepository lvQueryRepository;

    @PersistenceContext
    EntityManager em;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String labvantage_home = System.getenv().get("LABVANTAGE_HOME").replaceAll("\"", "/");

    private boolean isNumeric(String content) {
        // return (content.matches("^-?\\d+(?:,\\d+)?$")) ? true : false;
        return (content.matches("^(\\d|-)?(\\d)*\\.?\\d*$")) ? true : false;
    }

    private XSSFCellStyle generateBorder(XSSFCellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private XSSFFont generateFont(XSSFFont font, boolean isBold) {

        XSSFFont fontCustom = font;
        font.setFontHeightInPoints((short) 7);
        font.setFontName("SansSerif");
        if (isBold)
            font.setBold(true);
        else
            font.setBold(false);

        return fontCustom;
    }

    private XSSFWorkbook createWorkbook(XSSFWorkbook xssfWorkbook, List valh1, List listColumnh1,
                                        int numPage) throws IOException {

        // estilo general
        XSSFCellStyle style = xssfWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true);
        style.setFont(generateFont(xssfWorkbook.createFont(), false));

        try {


            int rowCount = 6;
            int columnCount = 0;
            Sheet sheet = xssfWorkbook.getSheetAt(numPage);
            Row row = sheet.createRow(rowCount);
            Cell celda;

            //Hoja1
            for (Object o : listColumnh1) {

                List listObjectTotal = Arrays.asList(o);
                List listObject = Arrays.asList(listObjectTotal.get(0));
                Object[] object = (Object[]) listObject.get(0);
                Object record = object[5];
                celda = row.createCell((short) columnCount);
                if (record.toString() != null || record.toString().equals("null")) {

                    XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                    style1.setWrapText(true);
                    style1.setFont(generateFont(xssfWorkbook.createFont(), true));
                    style1 = generateBorder(style1);
                    style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                    style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    celda.setCellValue(record.toString());
                    celda.setCellStyle(style1);

                } else {
                    XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                    style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                    style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    style1 = generateBorder(style1);
                    celda.setCellValue("");
                    celda.setCellStyle(style1);
                }
                columnCount++;

            }

            rowCount++;
            columnCount = 0;

            for (Object o : valh1) {

                List valueTotal = Arrays.asList(o);
                List listObject = Arrays.asList(valueTotal.get(0));
                Object[] object = (Object[]) listObject.get(0);
                Row row1 = sheet.createRow(rowCount);
                for (Object record : object) {
                    celda = row1.createCell((short) columnCount);
                    if (record == null || record.toString().equals("null")) {

                        XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                        style1 = generateBorder(style1);
                        String s = "";
                        celda.setCellValue(s);
                        celda.setCellStyle(style1);

                    } else if (this.isNumeric(record.toString()) && !record.toString().equalsIgnoreCase("-")) {

                        XSSFCellStyle style1 = style;
                        style1 = generateBorder(style1);
                        celda.setCellStyle(style1);
                        double d = Double.parseDouble(record.toString());
                        celda.setCellValue(d);
                        celda.setCellStyle(style1);

                    } else {

                        XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                        style1 = generateBorder(style1);
                        celda.setCellValue(record.toString());
                        celda.setCellStyle(style1);

                    }
                    columnCount++;
                }
                rowCount++;
                columnCount = 0;

            }


        } catch (Exception e) {
            logger.error(e.getMessage());

        }

        return xssfWorkbook;
    }


    private List excecuteQueryValues(List<Argument> arguments, int number, String name, int skip, int take) {
        List results = new ArrayList<>();
        Session session = em.unwrap(Session.class);
        StoredProcedureQuery queryExecute = session.createStoredProcedureCall(name);

        try {

            queryExecute.registerStoredProcedureParameter("cuenca", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("campo", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("producto", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("fechaIni", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("fechaFin", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("muestra", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("request", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("punto", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("sitio", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("atributo1", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("value1", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("operador", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("atributo2", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("value2", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("p_LogonName", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("p_SysUserid", String.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("p_Skip", Integer.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("p_Take", Integer.class, ParameterMode.IN);
            queryExecute.registerStoredProcedureParameter("Formato", Integer.class, ParameterMode.IN);

            queryExecute.setParameter("cuenca", arguments.get(0).getValue().toString());
            queryExecute.setParameter("campo", arguments.get(1).getValue().toString());
            queryExecute.setParameter("producto", arguments.get(2).getValue().toString());
            queryExecute.setParameter("fechaIni", arguments.get(3).getValue().toString());
            queryExecute.setParameter("fechaFin", arguments.get(4).getValue().toString());
            queryExecute.setParameter("muestra", arguments.get(5).getValue().toString());
            queryExecute.setParameter("request", arguments.get(6).getValue().toString());
            queryExecute.setParameter("punto", arguments.get(7).getValue().toString());
            queryExecute.setParameter("sitio", arguments.get(8).getValue().toString());
            queryExecute.setParameter("atributo1", arguments.get(9).getValue().toString());
            queryExecute.setParameter("value1", arguments.get(10).getValue().toString());
            queryExecute.setParameter("operador", arguments.get(11).getValue().toString());
            queryExecute.setParameter("atributo2", arguments.get(12).getValue().toString());
            queryExecute.setParameter("value2", arguments.get(13).getValue().toString());
            queryExecute.setParameter("p_LogonName", arguments.get(14).getValue().toString());
            queryExecute.setParameter("p_SysUserid", arguments.get(15).getValue().toString());
            queryExecute.setParameter("p_Skip", skip);
            queryExecute.setParameter("p_Take", take);
            queryExecute.setParameter("Formato", number);
            queryExecute.registerStoredProcedureParameter("P_DATOS", void.class, ParameterMode.REF_CURSOR);
            queryExecute.execute();
            results = queryExecute.getResultList();
        } catch (Exception ex) {
            logger.error("Error ejecutando la consulta : " + ex.getMessage(), ex);
        }
        return results;
    }



    public byte[] createReport(List<Argument> arguments, int skip, int take, String format) {

        try {

            int procedureformat = (format.equalsIgnoreCase("xls")) ? 2 : 1;
            OPCPackage pkg = OPCPackage.open(new File(this.labvantage_home +
                    "applications/labvantage/reports/labviewer/templates/LBIOTCombinado.xlsm"));

            List listOfResultsH1 = this.excecuteQueryValues(arguments, procedureformat, "BIOTECHNOLOGYREPORT.getValueBiotecCombinado", skip, take);
            List listParametersH1 = this.excecuteQueryParameters(arguments, procedureformat, "BIOTECHNOLOGYREPORT.getColumBiotecCombinado");
            List listOfResultsH2 = this.excecuteQueryValues(arguments, procedureformat, "BIOTECHNOLOGYREPORT.getValueBiotecVarGeo", skip, take);
            List listParametersH2 = this.excecuteQueryParameters(arguments, procedureformat, "BIOTECHNOLOGYREPORT.getColumBiotecVarGeo");
            List listOfResultsH3 = this.excecuteQueryValues(arguments, procedureformat, "BIOTECHNOLOGYREPORT.getValueBiotecCombinadoH3", skip, take);

            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(pkg);
            xssfWorkbook = this.createWorkbook(xssfWorkbook, listOfResultsH1, listParametersH1, 0);
            xssfWorkbook = this.createWorkbook(xssfWorkbook, listOfResultsH2, listParametersH2, 1);
            xssfWorkbook = this.createWorkbook(xssfWorkbook, listOfResultsH3, listParametersH1, 2);
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            logger.info("listOfResultsh1= " + listOfResultsH1.size());
            logger.info("listOfResultsh1= " + listOfResultsH2.size());
            logger.info("listOfResultsh1= " + listOfResultsH3.size());

            xssfWorkbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            logger.error("Error exportando archivo " + e.getMessage());
            return null;
        }
    }
}







