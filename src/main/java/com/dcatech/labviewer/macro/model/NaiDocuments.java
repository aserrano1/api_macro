package com.dcatech.labviewer.macro.model;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Table(name = "NAI_DOCUMENTS", schema = "OPS$PENLIMS")
public class NaiDocuments {
    @Id
    @Column(name = "DOCUMENT_ID")
    private int documentId;
    @Basic
    @Column(name = "DOCUMENT_BLOB")
    private byte[] documentBlob;

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public byte[] getDocumentBlob() {
        return documentBlob;
    }

    public void setDocumentBlob(byte[] documentBlob) {
        this.documentBlob = documentBlob;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NaiDocuments that = (NaiDocuments) o;

        if (documentId != that.documentId) return false;
        if (!Arrays.equals(documentBlob, that.documentBlob)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = documentId;
        result = 31 * result + Arrays.hashCode(documentBlob);
        return result;
    }
}
