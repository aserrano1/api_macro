package com.dcatech.labviewer.macro.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "LVQUERY")
public class LVQuery {
    @Basic
    @Column(name = "ROWNUMID")
    private Long rownumid;
    @Id
    @Column(name = "QUERYID")
    private String queryid;
    @Basic
    @Column(name = "QUERYDESC")
    private String querydesc;

    @Basic
    @Column(name = "SELECTCLAUSE")
    private String selectclause;

    @Basic
    @Column(name = "FROMCLAUSE")
    private String fromclause;

    @Basic
    @Column(name = "WHERECLAUSE")
    private String whereclause;
    @Basic
    @Column(name = "SECTION")
    private String section;

    @Basic
    @Column(name = "REPORTTITLE")
    private String reporttitle;
    @Basic
    @Column(name = "REPORTLOCATION")
    private String reportlocation;
    @Basic
    @Column(name = "SELECTSHOWCLAUSE")
    private String selectshowclause;
    @Basic
    @Column(name = "QUERYLABEL")
    private String querylabel;

    @Basic
    @Column(name = "U_SUBQUERYID")
    private String uSubqueryid;
    @Basic
    @Column(name = "U_QUERYCOLUMNKEY")
    private String uQuerycolumnkey;
    @Basic
    @Column(name = "U_LINKSUBQUERYARG")
    private String uLinksubqueryarg;

    @Column(name = "SELECTSHOWPDFCLAUSE")
    private String selectShowPdfClause;
    @Basic
    @Column(name = "SELECTSHOWEXCELCLAUSE")
    private String selectShowExcelClause;
    @Basic
    @Column(name = "PRINTLABEL")
    private String printlabel;

    @Basic
    @Column(name = "QUERYCONFTYPE")
    private String queryconftype;

    @Basic
    @Column(name = "REPORTSUBTITLE")
    private String reportSubtitle;

    @Basic
    @Column(name = "PROCEDURECOLUMS")
    private String procedureColums;

    @Basic
    @Column(name = "PROCEDUREEXECUTE")
    private String procedureExecute;

    @Basic
    @Column(name = "EMBEDSECURITYFLAG")
    private String embedSecurityFlag;


    @Basic
    @Column(name = "accesscontrolledflag")
    private String accesscontrolledflag;

    @Basic
    @Column(name = "basedonid")
    private String basedonid;

    @Basic
    @Column(name = "reportlabelname")
    private String reportlabelname;

    @Basic
    @Column(name = "graphicreport")
    private String graphicreport;

    @Basic
    @Column(name = "macrourl")
    private String macroURL;

    @Basic
    @Column(name = "macrohoja")
    private String macroHoja;

    @Basic
    @Column(name = "macrorow")
    private BigDecimal macroRow;

    @Basic
    @Column(name = "u_count_click")
    private Long clickCount;

    @Basic
    @Column(name = "jserver")
    private String Jserver;


    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LVQuery lvQuery = (LVQuery) o;

        if (rownumid != null ? !rownumid.equals(lvQuery.rownumid) : lvQuery.rownumid != null) return false;
        if (queryid != null ? !queryid.equals(lvQuery.queryid) : lvQuery.queryid != null) return false;
        if (querydesc != null ? !querydesc.equals(lvQuery.querydesc) : lvQuery.querydesc != null) return false;
        if (selectclause != null ? !selectclause.equals(lvQuery.selectclause) : lvQuery.selectclause != null)
            return false;
        if (fromclause != null ? !fromclause.equals(lvQuery.fromclause) : lvQuery.fromclause != null) return false;
        if (whereclause != null ? !whereclause.equals(lvQuery.whereclause) : lvQuery.whereclause != null) return false;
        if (section != null ? !section.equals(lvQuery.section) : lvQuery.section != null) return false;
        if (reporttitle != null ? !reporttitle.equals(lvQuery.reporttitle) : lvQuery.reporttitle != null) return false;
        if (reportlocation != null ? !reportlocation.equals(lvQuery.reportlocation) : lvQuery.reportlocation != null)
            return false;
        if (selectshowclause != null ? !selectshowclause.equals(lvQuery.selectshowclause) : lvQuery.selectshowclause != null)
            return false;
        if (querylabel != null ? !querylabel.equals(lvQuery.querylabel) : lvQuery.querylabel != null) return false;
        if (uSubqueryid != null ? !uSubqueryid.equals(lvQuery.uSubqueryid) : lvQuery.uSubqueryid != null) return false;
        if (uQuerycolumnkey != null ? !uQuerycolumnkey.equals(lvQuery.uQuerycolumnkey) : lvQuery.uQuerycolumnkey != null)
            return false;
        if (uLinksubqueryarg != null ? !uLinksubqueryarg.equals(lvQuery.uLinksubqueryarg) : lvQuery.uLinksubqueryarg != null)
            return false;
        if (selectShowPdfClause != null ? !selectShowPdfClause.equals(lvQuery.selectShowPdfClause) : lvQuery.selectShowPdfClause != null)
            return false;
        if (selectShowExcelClause != null ? !selectShowExcelClause.equals(lvQuery.selectShowExcelClause) : lvQuery.selectShowExcelClause != null)
            return false;
        if (printlabel != null ? !printlabel.equals(lvQuery.printlabel) : lvQuery.printlabel != null) return false;
        if (queryconftype != null ? !queryconftype.equals(lvQuery.queryconftype) : lvQuery.queryconftype != null)
            return false;
        if (reportSubtitle != null ? !reportSubtitle.equals(lvQuery.reportSubtitle) : lvQuery.reportSubtitle != null)
            return false;
        if (procedureColums != null ? !procedureColums.equals(lvQuery.procedureColums) : lvQuery.procedureColums != null)
            return false;
        if (procedureExecute != null ? !procedureExecute.equals(lvQuery.procedureExecute) : lvQuery.procedureExecute != null)
            return false;
        if (embedSecurityFlag != null ? !embedSecurityFlag.equals(lvQuery.embedSecurityFlag) : lvQuery.embedSecurityFlag != null)
            return false;
        if (accesscontrolledflag != null ? !accesscontrolledflag.equals(lvQuery.accesscontrolledflag) : lvQuery.accesscontrolledflag != null)
            return false;
        if (basedonid != null ? !basedonid.equals(lvQuery.basedonid) : lvQuery.basedonid != null) return false;
        if (reportlabelname != null ? !reportlabelname.equals(lvQuery.reportlabelname) : lvQuery.reportlabelname != null)
            return false;
        if (graphicreport != null ? !graphicreport.equals(lvQuery.graphicreport) : lvQuery.graphicreport != null)
            return false;
        if (macroURL != null ? !macroURL.equals(lvQuery.macroURL) : lvQuery.macroURL != null) return false;
        if (macroHoja != null ? !macroHoja.equals(lvQuery.macroHoja) : lvQuery.macroHoja != null) return false;
        if (macroRow != null ? !macroRow.equals(lvQuery.macroRow) : lvQuery.macroRow != null) return false;
        if (clickCount != null ? !clickCount.equals(lvQuery.clickCount) : lvQuery.clickCount != null) return false;
        if (buttonaction != null ? !buttonaction.equals(lvQuery.buttonaction) : lvQuery.buttonaction != null)
            return false;
        if (actionid != null ? !actionid.equals(lvQuery.actionid) : lvQuery.actionid != null) return false;
        if (actionversion != null ? !actionversion.equals(lvQuery.actionversion) : lvQuery.actionversion != null)
            return false;
        return jspostaction != null ? jspostaction.equals(lvQuery.jspostaction) : lvQuery.jspostaction == null;
    }

    @Override
    public int hashCode() {
        int result = rownumid != null ? rownumid.hashCode() : 0;
        result = 31 * result + (queryid != null ? queryid.hashCode() : 0);
        result = 31 * result + (querydesc != null ? querydesc.hashCode() : 0);
        result = 31 * result + (selectclause != null ? selectclause.hashCode() : 0);
        result = 31 * result + (fromclause != null ? fromclause.hashCode() : 0);
        result = 31 * result + (whereclause != null ? whereclause.hashCode() : 0);
        result = 31 * result + (section != null ? section.hashCode() : 0);
        result = 31 * result + (reporttitle != null ? reporttitle.hashCode() : 0);
        result = 31 * result + (reportlocation != null ? reportlocation.hashCode() : 0);
        result = 31 * result + (selectshowclause != null ? selectshowclause.hashCode() : 0);
        result = 31 * result + (querylabel != null ? querylabel.hashCode() : 0);
        result = 31 * result + (uSubqueryid != null ? uSubqueryid.hashCode() : 0);
        result = 31 * result + (uQuerycolumnkey != null ? uQuerycolumnkey.hashCode() : 0);
        result = 31 * result + (uLinksubqueryarg != null ? uLinksubqueryarg.hashCode() : 0);
        result = 31 * result + (selectShowPdfClause != null ? selectShowPdfClause.hashCode() : 0);
        result = 31 * result + (selectShowExcelClause != null ? selectShowExcelClause.hashCode() : 0);
        result = 31 * result + (printlabel != null ? printlabel.hashCode() : 0);
        result = 31 * result + (queryconftype != null ? queryconftype.hashCode() : 0);
        result = 31 * result + (reportSubtitle != null ? reportSubtitle.hashCode() : 0);
        result = 31 * result + (procedureColums != null ? procedureColums.hashCode() : 0);
        result = 31 * result + (procedureExecute != null ? procedureExecute.hashCode() : 0);
        result = 31 * result + (embedSecurityFlag != null ? embedSecurityFlag.hashCode() : 0);
        result = 31 * result + (accesscontrolledflag != null ? accesscontrolledflag.hashCode() : 0);
        result = 31 * result + (basedonid != null ? basedonid.hashCode() : 0);
        result = 31 * result + (reportlabelname != null ? reportlabelname.hashCode() : 0);
        result = 31 * result + (graphicreport != null ? graphicreport.hashCode() : 0);
        result = 31 * result + (macroURL != null ? macroURL.hashCode() : 0);
        result = 31 * result + (macroHoja != null ? macroHoja.hashCode() : 0);
        result = 31 * result + (macroRow != null ? macroRow.hashCode() : 0);
        result = 31 * result + (clickCount != null ? clickCount.hashCode() : 0);
        result = 31 * result + (buttonaction != null ? buttonaction.hashCode() : 0);
        result = 31 * result + (actionid != null ? actionid.hashCode() : 0);
        result = 31 * result + (actionversion != null ? actionversion.hashCode() : 0);
        result = 31 * result + (jspostaction != null ? jspostaction.hashCode() : 0);
        return result;
    }

    public BigDecimal getMacroRow() {
        return macroRow;
    }

    public void setMacroRow(BigDecimal macroRow) {
        this.macroRow = macroRow;
    }

    public String getMacroURL() {
        return macroURL;
    }

    public void setMacroURL(String macroURL) {
        this.macroURL = macroURL;
    }

    public String getMacroHoja() {
        return macroHoja;
    }

    public void setMacroHoja(String macroHoja) {
        this.macroHoja = macroHoja;
    }

    @Column(name = "buttonaction")
    private String buttonaction;

    @Basic
    @Column(name = "actionid")
    private String actionid;

    @Basic
    @Column(name = "actionversion")
    private String actionversion;

    @Basic
    @Column(name = "jspostaction")
    private String jspostaction;

    public String getJspostaction() {
        return jspostaction;
    }

    public void setJspostaction(String jspostaction) {
        this.jspostaction = jspostaction;
    }

    public String getButtonaction() {
        return buttonaction;
    }

    public void setButtonaction(String buttonaction) {
        this.buttonaction = buttonaction;
    }

    public String getActionid() {
        return actionid;
    }

    public void setActionid(String actionid) {
        this.actionid = actionid;
    }

    public String getActionversion() {
        return actionversion;
    }

    public void setActionversion(String actionversion) {
        this.actionversion = actionversion;
    }

    public String getGraphicreport() {
        return graphicreport;
    }

    public void setGraphicreport(String graphicreport) {
        this.graphicreport = graphicreport;
    }

    public String getReportlabelname() {
        return reportlabelname;
    }

    public void setReportlabelname(String reportlabelname) {
        this.reportlabelname = reportlabelname;
    }

    public String getEmbedSecurityFlag() {
        return embedSecurityFlag;
    }

    public void setEmbedSecurityFlag(String embedSecurityFlag) {
        this.embedSecurityFlag = embedSecurityFlag;
    }

    public String getAccesscontrolledflag() {
        return accesscontrolledflag;
    }

    public void setAccesscontrolledflag(String accesscontrolledflag) {
        this.accesscontrolledflag = accesscontrolledflag;
    }

    public String getBasedonid() {
        return basedonid;
    }

    public void setBasedonid(String basedonid) {
        this.basedonid = basedonid;
    }

    public String getProcedureColums() {
        return procedureColums;
    }

    public void setProcedureColums(String procedureColums) {
        this.procedureColums = procedureColums;
    }

    public String getProcedureExecute() {
        return procedureExecute;
    }

    public void setProcedureExecute(String procedureExecute) {
        this.procedureExecute = procedureExecute;
    }

    public String getReportSubtitle() {
        return reportSubtitle;
    }

    public void setReportSubtitle(String reportSubtitle) {
        this.reportSubtitle = reportSubtitle;
    }


    public Long getRownumid() {
        return rownumid;
    }

    public void setRownumid(Long rownumid) {
        this.rownumid = rownumid;
    }

    public String getQueryid() {
        return queryid;
    }

    public void setQueryid(String queryid) {
        this.queryid = queryid;
    }

    public String getQuerydesc() {
        return querydesc;
    }

    public void setQuerydesc(String querydesc) {
        this.querydesc = querydesc;
    }

    public String getSelectclause() {
        return selectclause;
    }

    public void setSelectclause(String selectclause) {
        this.selectclause = selectclause;
    }

    public String getFromclause() {
        return fromclause;
    }

    public void setFromclause(String fromclause) {
        this.fromclause = fromclause;
    }

    public String getWhereclause() {
        return whereclause;
    }

    public void setWhereclause(String whereclause) {
        this.whereclause = whereclause;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getReporttitle() {
        return reporttitle;
    }

    public void setReporttitle(String reporttitle) {
        this.reporttitle = reporttitle;
    }

    public String getReportlocation() {
        return reportlocation;
    }

    public void setReportlocation(String reportlocation) {
        this.reportlocation = reportlocation;
    }

    public String getSelectshowclause() {
        return selectshowclause;
    }

    public void setSelectshowclause(String selectshowclause) {
        this.selectshowclause = selectshowclause;
    }

    public String getuSubqueryid() {
        return uSubqueryid;
    }

    public void setuSubqueryid(String uSubqueryid) {
        this.uSubqueryid = uSubqueryid;
    }

    public String getuQuerycolumnkey() {
        return uQuerycolumnkey;
    }

    public void setuQuerycolumnkey(String uQuerycolumnkey) {
        this.uQuerycolumnkey = uQuerycolumnkey;
    }

    public String getuLinksubqueryarg() {
        return uLinksubqueryarg;
    }

    public void setuLinksubqueryarg(String uLinksubqueryarg) {
        this.uLinksubqueryarg = uLinksubqueryarg;
    }


    public String getQuerylabel() {
        return querylabel;
    }

    public void setQuerylabel(String querylabel) {
        this.querylabel = querylabel;
    }

    public String getSelectShowPdfClause() {
        return selectShowPdfClause;
    }

    public void setSelectShowPdfClause(String selectShowPdfClause) {
        this.selectShowPdfClause = selectShowPdfClause;
    }

    public String getSelectShowExcelClause() {
        return selectShowExcelClause;
    }

    public void setSelectShowExcelClause(String selectShowExcelClause) {
        this.selectShowExcelClause = selectShowExcelClause;
    }

    public String getPrintlabel() {
        return printlabel;
    }

    public void setPrintlabel(String printlabel) {
        this.printlabel = printlabel;
    }

    public String getQueryconftype() {
        return queryconftype;
    }

    public void setQueryconftype(String queryconftype) {
        this.queryconftype = queryconftype;
    }

    public String getJserver() {
        return Jserver;
    }

    public void setJserver(String jserver) {
        Jserver = jserver;
    }

}
