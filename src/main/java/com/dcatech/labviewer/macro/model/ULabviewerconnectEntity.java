package com.dcatech.labviewer.macro.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "U_LABVIEWERCONNECT")
public class ULabviewerconnectEntity {
    private String uLabviewerconnectid;
    private String labviewerconnectdesc;
    private Long usersequence;
    private Long auditsequence;
    private String tracelogid;
    private String templateflag;
    private String notes;
    private Date createdt;
    private String createby;
    private String createtool;
    private Date moddt;
    private String modby;
    private String modtool;
    private String activeflag;
    private String userlabviwer;
    private Date cleardtlabviwer;

    @Id
    @Basic
    @Column(name = "U_LABVIEWERCONNECTID")
    public String getuLabviewerconnectid() {
        return uLabviewerconnectid;
    }

    public void setuLabviewerconnectid(String uLabviewerconnectid) {
        this.uLabviewerconnectid = uLabviewerconnectid;
    }

    @Basic
    @Column(name = "LABVIEWERCONNECTDESC")
    public String getLabviewerconnectdesc() {
        return labviewerconnectdesc;
    }

    public void setLabviewerconnectdesc(String labviewerconnectdesc) {
        this.labviewerconnectdesc = labviewerconnectdesc;
    }

    @Basic
    @Column(name = "USERSEQUENCE")
    public Long getUsersequence() {
        return usersequence;
    }

    public void setUsersequence(Long usersequence) {
        this.usersequence = usersequence;
    }

    @Basic
    @Column(name = "AUDITSEQUENCE")
    public Long getAuditsequence() {
        return auditsequence;
    }

    public void setAuditsequence(Long auditsequence) {
        this.auditsequence = auditsequence;
    }

    @Basic
    @Column(name = "TRACELOGID")
    public String getTracelogid() {
        return tracelogid;
    }

    public void setTracelogid(String tracelogid) {
        this.tracelogid = tracelogid;
    }

    @Basic
    @Column(name = "TEMPLATEFLAG")
    public String getTemplateflag() {
        return templateflag;
    }

    public void setTemplateflag(String templateflag) {
        this.templateflag = templateflag;
    }

    @Basic
    @Column(name = "NOTES")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Basic
    @Column(name = "CREATEDT")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedt() {
        return createdt;
    }

    public void setCreatedt(Date createdt) {
        this.createdt = createdt;
    }

    @Basic
    @Column(name = "CREATEBY")
    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby;
    }

    @Basic
    @Column(name = "CREATETOOL")
    public String getCreatetool() {
        return createtool;
    }

    public void setCreatetool(String createtool) {
        this.createtool = createtool;
    }

    @Basic
    @Column(name = "MODDT")
    public Date getModdt() {
        return moddt;
    }

    public void setModdt(Date moddt) {
        this.moddt = moddt;
    }

    @Basic
    @Column(name = "MODBY")
    public String getModby() {
        return modby;
    }

    public void setModby(String modby) {
        this.modby = modby;
    }

    @Basic
    @Column(name = "MODTOOL")
    public String getModtool() {
        return modtool;
    }

    public void setModtool(String modtool) {
        this.modtool = modtool;
    }

    @Basic
    @Column(name = "ACTIVEFLAG")
    public String getActiveflag() {
        return activeflag;
    }

    public void setActiveflag(String activeflag) {
        this.activeflag = activeflag;
    }

    @Basic
    @Column(name = "USERLABVIWER")
    public String getUserlabviwer() {
        return userlabviwer;
    }

    public void setUserlabviwer(String userlabviwer) {
        this.userlabviwer = userlabviwer;
    }

    @Basic
    @Column(name = "CLEARDTLABVIWER")
    public Date getCleardtlabviwer() {
        return cleardtlabviwer;
    }

    public void setCleardtlabviwer(Date cleardtlabviwer) {
        this.cleardtlabviwer = cleardtlabviwer;
    }


}
