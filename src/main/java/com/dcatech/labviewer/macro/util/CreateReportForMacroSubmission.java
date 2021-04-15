package com.dcatech.labviewer.macro.util;

import com.dcatech.labviewer.macro.model.Argument;
import com.dcatech.labviewer.macro.model.LVQuery;
import com.dcatech.labviewer.macro.repository.LVQueryRepository;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@Component
public class CreateReportForMacroSubmission {

    @Autowired
    LVQueryRepository lvQueryRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private String labvantage_home = System.getenv().get("LABVANTAGE_HOME").replaceAll("\"", "/");

    private HSSFWorkbook createWorkbook(List<Argument> arguments, InputStream pkg) throws IOException {


        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(pkg);
        Sheet sheet = hssfWorkbook.getSheetAt(2);

        Cell submission = sheet.getRow(5).getCell(1);
        Cell material_Type = sheet.getRow(6).getCell(1);
        Cell muestra = sheet.getRow(7).getCell(1);
        Cell userId = sheet.getRow(8).getCell(1);

        try {
            // Recorido de parametros

            double submission1 = Double.parseDouble(arguments.get(0).getValue().toString());
            submission.setCellValue(submission1);
            material_Type.setCellValue(arguments.get(1).getValue().toString());
            muestra.setCellValue(arguments.get(2).getValue().toString().replace("'", ""));
            userId.setCellValue(arguments.get(3).getValue().toString());

        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return hssfWorkbook;
    }


    public byte[] createReport(LVQuery byQueryId, List<Argument> arguments, String format) {

        try {

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int procedureformat = (format.equalsIgnoreCase("xls")) ? 2 : 1;
            List listOfResults = lvQueryRepository.executeQueryGridWithProcedure(byQueryId, arguments, 0, 0, procedureformat);
            if (listOfResults.get(0).equals("OK")) {
                InputStream pkg = new FileInputStream(this.labvantage_home + "applications/labvantage/reports/labviewer/templates/Submission_macro_Ws.xls");
                HSSFWorkbook hssfWorkbook = this.createWorkbook(arguments, pkg);
                hssfWorkbook.write(out);
                return out.toByteArray();
            } else {
                return null;
            }


        } catch (Exception ex) {
            logger.error("Error Exportando a Excel: Class CreateReportForMacroSubmission " + ex.getMessage());
            return null;
        }

    }
}
