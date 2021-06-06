package com.dcatech.labviewer.macro.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "U_SAVEREPORTPARAM")
public class USaveReportParam {
    @Id
    @Column(name = "U_SAVEREPORTPARAMID", nullable = false)
    private String uSavereportparamid;
    @Basic
    @Column(name = "SAVEREPORTPARAMDESC")
    private String savereportparamdesc;
    @Basic
    @Column(name = "USERSEQUENCE")
    private Long usersequence;
    @Basic
    @Column(name = "AUDITSEQUENCE")
    private Long auditsequence;
    @Basic
    @Column(name = "TRACELOGID")
    private String tracelogid;
    @Basic
    @Column(name = "TEMPLATEFLAG")
    private String templateflag;
    @Basic
    @Column(name = "NOTES")
    private String notes;
    @Basic
    @Column(name = "CREATEDT")
    private Time createdt;
    @Basic
    @Column(name = "CREATEBY")
    private String createby;
    @Basic
    @Column(name = "CREATETOOL")
    private String createtool;
    @Basic
    @Column(name = "MODDT")
    private Time moddt;
    @Basic
    @Column(name = "MODBY")
    private String modby;
    @Basic
    @Column(name = "MODTOOL")
    private String modtool;
    @Basic
    @Column(name = "ACTIVEFLAG")
    private String activeflag;
    @Basic
    @Column(name = "ARGUMENTSID")
    private Long argumentsid;
    @Basic
    @Column(name = "ARGUMENTS")
    private String arguments;
    @Basic
    @Column(name = "FECHA_REGISTRO")
    private Date fechaRegistro;
    @Basic
    @Column(name = "REPORTUSEREXEC")
    private String reportuserexec;
    @Basic
    @Column(name = "REPORTQUERYID")
    private String reportqueryid;

    public String getuSavereportparamid() {
        return uSavereportparamid;
    }

    public void setuSavereportparamid(String uSavereportparamid) {
        this.uSavereportparamid = uSavereportparamid;
    }

    public String getSavereportparamdesc() {
        return savereportparamdesc;
    }

    public void setSavereportparamdesc(String savereportparamdesc) {
        this.savereportparamdesc = savereportparamdesc;
    }

    public Long getUsersequence() {
        return usersequence;
    }

    public void setUsersequence(Long usersequence) {
        this.usersequence = usersequence;
    }

    public Long getAuditsequence() {
        return auditsequence;
    }

    public void setAuditsequence(Long auditsequence) {
        this.auditsequence = auditsequence;
    }

    public String getTracelogid() {
        return tracelogid;
    }

    public void setTracelogid(String tracelogid) {
        this.tracelogid = tracelogid;
    }

    public String getTemplateflag() {
        return templateflag;
    }

    public void setTemplateflag(String templateflag) {
        this.templateflag = templateflag;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Time getCreatedt() {
        return createdt;
    }

    public void setCreatedt(Time createdt) {
        this.createdt = createdt;
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    public String getCreatetool() {
        return createtool;
    }

    public void setCreatetool(String createtool) {
        this.createtool = createtool;
    }

    public Time getModdt() {
        return moddt;
    }

    public void setModdt(Time moddt) {
        this.moddt = moddt;
    }

    public String getModby() {
        return modby;
    }

    public void setModby(String modby) {
        this.modby = modby;
    }

    public String getModtool() {
        return modtool;
    }

    public void setModtool(String modtool) {
        this.modtool = modtool;
    }

    public String getActiveflag() {
        return activeflag;
    }

    public void setActiveflag(String activeflag) {
        this.activeflag = activeflag;
    }

    public Long getArgumentsid() {
        return argumentsid;
    }

    public void setArgumentsid(Long argumentsid) {
        this.argumentsid = argumentsid;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getReportuserexec() {
        return reportuserexec;
    }

    public void setReportuserexec(String reportuserexec) {
        this.reportuserexec = reportuserexec;
    }

    public String getReportqueryid() {
        return reportqueryid;
    }

    public void setReportqueryid(String reportqueryid) {
        this.reportqueryid = reportqueryid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        USaveReportParam that = (USaveReportParam) o;

        if (uSavereportparamid != null ? !uSavereportparamid.equals(that.uSavereportparamid) : that.uSavereportparamid != null)
            return false;
        if (savereportparamdesc != null ? !savereportparamdesc.equals(that.savereportparamdesc) : that.savereportparamdesc != null)
            return false;
        if (usersequence != null ? !usersequence.equals(that.usersequence) : that.usersequence != null) return false;
        if (auditsequence != null ? !auditsequence.equals(that.auditsequence) : that.auditsequence != null)
            return false;
        if (tracelogid != null ? !tracelogid.equals(that.tracelogid) : that.tracelogid != null) return false;
        if (templateflag != null ? !templateflag.equals(that.templateflag) : that.templateflag != null) return false;
        if (notes != null ? !notes.equals(that.notes) : that.notes != null) return false;
        if (createdt != null ? !createdt.equals(that.createdt) : that.createdt != null) return false;
        if (createby != null ? !createby.equals(that.createby) : that.createby != null) return false;
        if (createtool != null ? !createtool.equals(that.createtool) : that.createtool != null) return false;
        if (moddt != null ? !moddt.equals(that.moddt) : that.moddt != null) return false;
        if (modby != null ? !modby.equals(that.modby) : that.modby != null) return false;
        if (modtool != null ? !modtool.equals(that.modtool) : that.modtool != null) return false;
        if (activeflag != null ? !activeflag.equals(that.activeflag) : that.activeflag != null) return false;
        if (argumentsid != null ? !argumentsid.equals(that.argumentsid) : that.argumentsid != null) return false;
        if (arguments != null ? !arguments.equals(that.arguments) : that.arguments != null) return false;
        if (fechaRegistro != null ? !fechaRegistro.equals(that.fechaRegistro) : that.fechaRegistro != null)
            return false;
        if (reportuserexec != null ? !reportuserexec.equals(that.reportuserexec) : that.reportuserexec != null)
            return false;
        if (reportqueryid != null ? !reportqueryid.equals(that.reportqueryid) : that.reportqueryid != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uSavereportparamid != null ? uSavereportparamid.hashCode() : 0;
        result = 31 * result + (savereportparamdesc != null ? savereportparamdesc.hashCode() : 0);
        result = 31 * result + (usersequence != null ? usersequence.hashCode() : 0);
        result = 31 * result + (auditsequence != null ? auditsequence.hashCode() : 0);
        result = 31 * result + (tracelogid != null ? tracelogid.hashCode() : 0);
        result = 31 * result + (templateflag != null ? templateflag.hashCode() : 0);
        result = 31 * result + (notes != null ? notes.hashCode() : 0);
        result = 31 * result + (createdt != null ? createdt.hashCode() : 0);
        result = 31 * result + (createby != null ? createby.hashCode() : 0);
        result = 31 * result + (createtool != null ? createtool.hashCode() : 0);
        result = 31 * result + (moddt != null ? moddt.hashCode() : 0);
        result = 31 * result + (modby != null ? modby.hashCode() : 0);
        result = 31 * result + (modtool != null ? modtool.hashCode() : 0);
        result = 31 * result + (activeflag != null ? activeflag.hashCode() : 0);
        result = 31 * result + (argumentsid != null ? argumentsid.hashCode() : 0);
        result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
        result = 31 * result + (fechaRegistro != null ? fechaRegistro.hashCode() : 0);
        result = 31 * result + (reportuserexec != null ? reportuserexec.hashCode() : 0);
        result = 31 * result + (reportqueryid != null ? reportqueryid.hashCode() : 0);
        return result;
    }
}
