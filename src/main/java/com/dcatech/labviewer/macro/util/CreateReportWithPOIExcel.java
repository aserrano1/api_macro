package com.dcatech.labviewer.macro.util;

import com.dcatech.labviewer.macro.model.Argument;
import com.dcatech.labviewer.macro.model.LVQuery;
import com.dcatech.labviewer.macro.repository.LVQueryFieldsRepository;
import com.dcatech.labviewer.macro.repository.LVQueryRepository;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CreateReportWithPOIExcel {

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

    private XSSFWorkbook createWorkbook(List values, List listParameters, OPCPackage pkg, String name) throws IOException {

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(pkg);
        Sheet sheet = xssfWorkbook.getSheetAt(0);
        xssfWorkbook.setSheetName(0, name);
        // estilo general
        XSSFCellStyle style = xssfWorkbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // Ajustar texto en las celdas
        style.setWrapText(true);
        // Agregar fuente al estilo
        style.setFont(generateFont(xssfWorkbook.createFont(), false));

        int columnTotal = 0;
        int rowCountParam = 2;
        int columnCountParam = 0;

        try {
            // Recorido de parametros
            for (Object o : listParameters) {
                Row row = sheet.createRow(rowCountParam);
                List listObjectTotal = Arrays.asList(o);
                List listObject = Arrays.asList(listObjectTotal.get(0));
                Object[] objectParam = (Object[]) listObject.get(0);
                for (Object record : objectParam) {
                    XSSFCellStyle styleParam = style;
                    styleParam = generateBorder(styleParam);
                    if (rowCountParam < 5) {
                        Cell cell = row.createCell(columnCountParam);
                        cell.setCellValue(record.toString());
                        if (columnCountParam == 0) {
                            XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                            style1.setWrapText(true);
                            style1 = generateBorder(style1);
                            style1.setFont(generateFont(xssfWorkbook.createFont(), true));
                            style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                            style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            cell.setCellValue(record.toString());
                            cell.setCellStyle(style1);
                        } else {
                            XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                            style1.setWrapText(true);
                            style1 = generateBorder(style1);
                            style1.setFont(generateFont(xssfWorkbook.createFont(), false));
                            cell.setCellValue(record.toString());
                            cell.setCellStyle(style1);
                        }
                        columnCountParam++;
                    }
                }

                rowCountParam++;
                columnCountParam = 0;
            }

            for (int w = 0; w <= 2; w++) sheet.setColumnWidth(w, 5000);
            int rowCount = 8;
            int columnCount = 0;
            for (Object o : values) {

                List listObjectTotal = Arrays.asList(o);
                List listObject = Arrays.asList(listObjectTotal.get(0));
                Object[] object = (Object[]) listObject.get(0);

                // Se crea una fila dentro de la hoja
                Row row = sheet.createRow(rowCount);

                for (Object record : object) {
                    // Se crea una celda dentro de la fila
                    Cell celda = row.createCell((short) columnCount);

                    // Se crea el contenido de la celda y se mete en ella.

                    if (record == null) {

                        if (rowCount <= 17 && columnCount < 4) {
                            XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                            String s = "";
                            celda.setCellValue(s);
                            celda.setCellStyle(style1);
                        } else {
                            XSSFCellStyle style1 = style;
                            style1 = generateBorder(style1);
                            String s = "";
                            celda.setCellValue(s);
                            celda.setCellStyle(style1);
                        }

                    } else if (this.isNumeric(record.toString()) && !record.toString().equalsIgnoreCase("-")) {
                        XSSFCellStyle style1 = style;
                        style1 = generateBorder(style1);
                        celda.setCellStyle(style1);
                        double d = Double.parseDouble(record.toString());
                        celda.setCellValue(d);
                        celda.setCellStyle(style1);

                    } else {
                        if (rowCount == 18) {
                            XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                            style1.setWrapText(true);
                            style1.setFont(generateFont(xssfWorkbook.createFont(), true));
                            style1 = generateBorder(style1);
                            style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                            style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                            celda.setCellValue(record.toString());
                            celda.setCellStyle(style1);
                        } else {
                            if (rowCount <= 17 && columnCount == 3) {
                                XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                                style1.setWrapText(true);
                                style1 = generateBorder(style1);
                                style1.setFont(generateFont(xssfWorkbook.createFont(), true));
                                style1.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                                style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                                celda.setCellValue(record.toString());
                                celda.setCellStyle(style1);
                            } else {
                                XSSFCellStyle style1 = xssfWorkbook.createCellStyle();
                                style1.setWrapText(true);
                                style1.setFont(generateFont(xssfWorkbook.createFont(), false));
                                style1 = generateBorder(style1);
                                celda.setCellStyle(style1);
                                celda.setCellValue(record.toString());
                                celda.setCellStyle(style1);
                            }
                        }
                    }
                    columnCount++;
                }
                rowCount++;

                columnCount = 0;
            }

            Object[] objects = (Object[]) listParameters.get(3);
            Row rowObervacion = sheet.createRow(++rowCount);
            Cell c = rowObervacion.createCell(0);
            c.setCellValue(objects[0].toString());
            XSSFCellStyle styleobs = xssfWorkbook.createCellStyle();
            styleobs.setWrapText(true);
            styleobs = generateBorder(styleobs);
            styleobs.setFont(generateFont(xssfWorkbook.createFont(), true));
            styleobs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            styleobs.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            c.setCellStyle(styleobs);
            String[] obsArr = objects[1].toString().split(";;");
            for (int obs = 0; obs < obsArr.length; obs++) {
                Row r = sheet.createRow(++rowCount);
                Cell oCell = r.createCell(1);
                XSSFCellStyle styleo = xssfWorkbook.createCellStyle();
                styleo.setWrapText(true);
                styleo.setFont(generateFont(xssfWorkbook.createFont(), false));
                styleo = generateBorder(styleo);
                oCell.setCellValue(obsArr[obs]);
                oCell.setCellStyle(styleo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return xssfWorkbook;
    }


    public byte[] createReport(LVQuery byQueryId, List<Argument> arguments, int skip, int take, String format, String name) {

        try {
            String header = (byQueryId.getProcedureColums());
            int procedureformat = (format.equalsIgnoreCase("xls")) ? 2 : 1;
            List listOfResults = lvQueryRepository.executeQueryGridWithProcedure(byQueryId, arguments, 0, 0, procedureformat);
            List listParameters = this.excecuteQueryParameters(arguments.get(0).getValue().toString(), header);
            logger.info("listOfResults= " + listOfResults.size());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (listOfResults.size() < 2) {
                byte[] in = Files.readAllBytes(new File(this.labvantage_home + "applications/labvantage/reports/labviewer/templates/template_no_data.xlsx").toPath());
                return in;
            }
            OPCPackage pkg = OPCPackage.open(new File(this.labvantage_home + "applications/labvantage/reports/labviewer/templates/template.xlsx"));
            XSSFWorkbook xssfWorkbook = this.createWorkbook(listOfResults, listParameters, pkg, name);
            xssfWorkbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            return null;
        }

    }


}
