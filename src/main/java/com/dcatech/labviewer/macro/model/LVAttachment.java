package com.dcatech.labviewer.macro.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;

@Entity
public class LVAttachment {
    @Id
    @Column(name = "ROWNUMID")
    private Long rownumId;
    @Basic
    @Column(name = "SDCID")
    private String sdcid;
    @Basic
    @Column(name = "KEYID1")
    private String keyId1;
    @Basic
    @Column(name = "ATTACHMENTDESC")
    private String attachmentDesc;
    @Basic
    @Column(name = "FILENAME")
    private String fileName;
    @Basic
    @Column(name = "ATTACHMENT")
    private byte[] attachment;
    @Basic
    @Column(name = "U_AUTOR")
    private String uAutor;
    @Basic
    @Column(name = "FECHAINICIO")
    private String fechaInicio;
    @Basic
    @Column(name = "FECHAFIN")
    private String fechaFin;
    @Basic
    @Column(name = "ATTACHMENTNUM")
    private long attachmentNum;
    @Basic
    @Column(name = "SOURCEFILENAME")
    private String sourceFilename;

    public Long getRownumId() {
        return rownumId;
    }

    public void setRownumId(Long rownumId) {
        this.rownumId = rownumId;
    }

    public String getSdcid() {
        return sdcid;
    }

    public void setSdcid(String sdcid) {
        this.sdcid = sdcid;
    }

    public String getKeyId1() {
        return keyId1;
    }

    public void setKeyId1(String keyId1) {
        this.keyId1 = keyId1;
    }

    public String getAttachmentDesc() {
        return attachmentDesc;
    }

    public void setAttachmentDesc(String attachmentDesc) {
        this.attachmentDesc = attachmentDesc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String filename) {
        this.fileName = filename;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getuAutor() {
        return uAutor;
    }

    public void setuAutor(String uAutor) {
        this.uAutor = uAutor;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public long getAttachmentNum() {
        return attachmentNum;
    }

    public void setAttachmentNum(long attachmentNum) {
        this.attachmentNum = attachmentNum;
    }

    public String getSourceFilename() {
        return sourceFilename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LVAttachment that = (LVAttachment) o;

        if (attachmentNum != that.attachmentNum) return false;
        if (rownumId != null ? !rownumId.equals(that.rownumId) : that.rownumId != null) return false;
        if (sdcid != null ? !sdcid.equals(that.sdcid) : that.sdcid != null) return false;
        if (keyId1 != null ? !keyId1.equals(that.keyId1) : that.keyId1 != null) return false;
        if (attachmentDesc != null ? !attachmentDesc.equals(that.attachmentDesc) : that.attachmentDesc != null)
            return false;
        if (fileName != null ? !fileName.equals(that.fileName) : that.fileName != null) return false;
        if (!Arrays.equals(attachment, that.attachment)) return false;
        if (uAutor != null ? !uAutor.equals(that.uAutor) : that.uAutor != null) return false;
        if (fechaInicio != null ? !fechaInicio.equals(that.fechaInicio) : that.fechaInicio != null) return false;
        if (fechaFin != null ? !fechaFin.equals(that.fechaFin) : that.fechaFin != null) return false;
        return sourceFilename != null ? sourceFilename.equals(that.sourceFilename) : that.sourceFilename == null;
    }

    @Override
    public int hashCode() {
        int result = rownumId != null ? rownumId.hashCode() : 0;
        result = 31 * result + (sdcid != null ? sdcid.hashCode() : 0);
        result = 31 * result + (keyId1 != null ? keyId1.hashCode() : 0);
        result = 31 * result + (attachmentDesc != null ? attachmentDesc.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(attachment);
        result = 31 * result + (uAutor != null ? uAutor.hashCode() : 0);
        result = 31 * result + (fechaInicio != null ? fechaInicio.hashCode() : 0);
        result = 31 * result + (fechaFin != null ? fechaFin.hashCode() : 0);
        result = 31 * result + (int) (attachmentNum ^ (attachmentNum >>> 32));
        result = 31 * result + (sourceFilename != null ? sourceFilename.hashCode() : 0);
        return result;
    }

    public void setSourceFilename(String sourceFilename) {
        this.sourceFilename = sourceFilename;
    }
}
