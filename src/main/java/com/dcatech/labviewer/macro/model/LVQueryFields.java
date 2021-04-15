package com.dcatech.labviewer.macro.model;

import javax.persistence.*;

@Entity
@Table(name = "LVQUERYFIELDS")
public class LVQueryFields {
    @Id
    @Column(name = "ROWNUMID")
    private Long rownumid;
    @Basic
    @Column(name = "CONFIGQUERYID")
    private String configqueryid;
    @Basic
    @Column(name = "QUERYID")
    private String queryid;
    @Basic
    @Column(name = "FIELDNAME")
    private String fieldname;
    @Basic
    @Column(name = "FIELDALIAS")
    private String fieldalias;
    @Basic
    @Column(name = "FIELDLABEL")
    private String fieldlabel;
    @Basic
    @Column(name = "FIELDORDER")
    private Long fieldorder;
    @Basic
    @Column(name = "FILTERABLE")
    private String filterable;
    @Basic
    @Column(name = "FIELDTYPE")
    private String fieldtype;
    @Basic
    @Column(name = "SUBQUERYID")
    private String subqueryid;
    @Basic
    @Column(name = "LINKSUBQUERYARG")
    private String linksubqueryarg;
    @Basic
    @Column(name = "FILEICO")
    private String fileico;
    @Basic
    @Column(name = "FIELDRPTLOCATION")
    private String fieldrptlocation;
    @Basic
    @Column(name = "SHOWCOLUMN")
    private String showcolumn;
    @Basic
    @Column(name = "WIDTHCOLUMN")
    private String widthcolumn;
    @Basic
    @Column(name = "SHOWINEXCEL")
    private String showInExcel;
    @Basic
    @Column(name = "SHOWINPDF")
    private String showInPdf;
    @Basic
    @Column(name = "COLUMNHEADER")
    private String columnHeader;
    @Basic
    @Column(name = "SHOWROWFIELDEXCEL")
    private String showrowfieldexcel;
    @Basic
    @Column(name = "HEADERQUERYID")
    private String headerqueryid;

    @Basic
    @Column(name = "JSERVER")
    private String jServer;


    public String getHeaderqueryid() {
        return headerqueryid;
    }

    public void setHeaderqueryid(String headerqueryid) {
        this.headerqueryid = headerqueryid;
    }

    public String getColumnHeader() {
        return columnHeader;
    }

    public void setColumnHeader(String columnHeader) {
        this.columnHeader = columnHeader;
    }

    public Long getRownumid() {
        return rownumid;
    }

    public void setRownumid(Long rownumid) {
        this.rownumid = rownumid;
    }

    public String getConfigqueryid() {
        return configqueryid;
    }

    public void setConfigqueryid(String configqueryid) {
        this.configqueryid = configqueryid;
    }

    public String getQueryid() {
        return queryid;
    }

    public void setQueryid(String queryid) {
        this.queryid = queryid;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getFieldalias() {
        return fieldalias;
    }

    public void setFieldalias(String fieldalias) {
        this.fieldalias = fieldalias;
    }

    public String getFieldlabel() {
        return fieldlabel;
    }

    public void setFieldlabel(String fieldlabel) {
        this.fieldlabel = fieldlabel;
    }

    public Long getFieldorder() {
        return fieldorder;
    }

    public void setFieldorder(Long fieldorder) {
        this.fieldorder = fieldorder;
    }

    public String getFilterable() {
        return filterable;
    }

    public void setFilterable(String filterable) {
        this.filterable = filterable;
    }

    public String getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(String fieldtype) {
        this.fieldtype = fieldtype;
    }

    public String getSubqueryid() {
        return subqueryid;
    }

    public void setSubqueryid(String subqueryid) {
        this.subqueryid = subqueryid;
    }

    public String getLinksubqueryarg() {
        return linksubqueryarg;
    }

    public void setLinksubqueryarg(String linksubqueryarg) {
        this.linksubqueryarg = linksubqueryarg;
    }

    public String getFileico() {
        return fileico;
    }

    public void setFileico(String fileico) {
        this.fileico = fileico;
    }

    public String getFieldrptlocation() {
        return fieldrptlocation;
    }

    public void setFieldrptlocation(String fieldrptlocation) {
        this.fieldrptlocation = fieldrptlocation;
    }

    public String getShowcolumn() {
        return showcolumn;
    }

    public void setShowcolumn(String showcolumn) {
        this.showcolumn = showcolumn;
    }

    public String getShowrowfieldexcel() {
        return showrowfieldexcel;
    }

    public void setShowrowfieldexcel(String showrowfieldexcel) {
        this.showrowfieldexcel = showrowfieldexcel;
    }

    public String getjServer() {
        return jServer;
    }

    public void setjServer(String jServer) {
        this.jServer = jServer;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LVQueryFields that = (LVQueryFields) o;

        if (rownumid != null ? !rownumid.equals(that.rownumid) : that.rownumid != null) return false;
        if (configqueryid != null ? !configqueryid.equals(that.configqueryid) : that.configqueryid != null)
            return false;
        if (queryid != null ? !queryid.equals(that.queryid) : that.queryid != null) return false;
        if (fieldname != null ? !fieldname.equals(that.fieldname) : that.fieldname != null) return false;
        if (fieldalias != null ? !fieldalias.equals(that.fieldalias) : that.fieldalias != null) return false;
        if (fieldlabel != null ? !fieldlabel.equals(that.fieldlabel) : that.fieldlabel != null) return false;
        if (fieldorder != null ? !fieldorder.equals(that.fieldorder) : that.fieldorder != null) return false;
        if (filterable != null ? !filterable.equals(that.filterable) : that.filterable != null) return false;
        if (fieldtype != null ? !fieldtype.equals(that.fieldtype) : that.fieldtype != null) return false;
        if (subqueryid != null ? !subqueryid.equals(that.subqueryid) : that.subqueryid != null) return false;
        if (linksubqueryarg != null ? !linksubqueryarg.equals(that.linksubqueryarg) : that.linksubqueryarg != null)
            return false;
        if (fileico != null ? !fileico.equals(that.fileico) : that.fileico != null) return false;
        if (fieldrptlocation != null ? !fieldrptlocation.equals(that.fieldrptlocation) : that.fieldrptlocation != null)
            return false;
        if (showcolumn != null ? !showcolumn.equals(that.showcolumn) : that.showcolumn != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rownumid != null ? rownumid.hashCode() : 0;
        result = 31 * result + (configqueryid != null ? configqueryid.hashCode() : 0);
        result = 31 * result + (queryid != null ? queryid.hashCode() : 0);
        result = 31 * result + (fieldname != null ? fieldname.hashCode() : 0);
        result = 31 * result + (fieldalias != null ? fieldalias.hashCode() : 0);
        result = 31 * result + (fieldlabel != null ? fieldlabel.hashCode() : 0);
        result = 31 * result + (fieldorder != null ? fieldorder.hashCode() : 0);
        result = 31 * result + (filterable != null ? filterable.hashCode() : 0);
        result = 31 * result + (fieldtype != null ? fieldtype.hashCode() : 0);
        result = 31 * result + (subqueryid != null ? subqueryid.hashCode() : 0);
        result = 31 * result + (linksubqueryarg != null ? linksubqueryarg.hashCode() : 0);
        result = 31 * result + (fileico != null ? fileico.hashCode() : 0);
        result = 31 * result + (fieldrptlocation != null ? fieldrptlocation.hashCode() : 0);
        result = 31 * result + (showcolumn != null ? showcolumn.hashCode() : 0);
        return result;
    }

    public String getWidthcolumn() {
        return widthcolumn;
    }

    public void setWidthcolumn(String widthcolumn) {
        this.widthcolumn = widthcolumn;
    }

    public String getShowInExcel() {
        return showInExcel;
    }

    public void setShowInExcel(String showinexcel) {
        this.showInExcel = showinexcel;
    }

    public String getShowInPdf() {
        return showInPdf;
    }

    public void setShowInPdf(String showinpdf) {
        this.showInPdf = showinpdf;
    }
}
