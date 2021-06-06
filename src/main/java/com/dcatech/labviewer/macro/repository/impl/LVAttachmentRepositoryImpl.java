package com.dcatech.labviewer.macro.repository.impl;

import com.dcatech.labviewer.macro.model.LVAttachment;
import com.dcatech.labviewer.macro.model.NaiDocuments;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.SQLException;

public class LVAttachmentRepositoryImpl implements LVAttachmentRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional(readOnly = true)
    public LVAttachment findByKeyId1AndAttachmentNum(String fileName, String keyId1, Long attachmentNum, String sdcId) {
        Session unwrap = em.unwrap(Session.class);
        NativeQuery nativeQuery = unwrap.createNativeQuery("select * from LVATTACHMENT lvattach " +
                " where lvattach.KEYID1=:keyId1" +
                " and lvattach.ATTACHMENTDESC=:fileName" +
                " and lvattach.sdcid=:sdcId" +
                " and lvattach.ATTACHMENTNUM=:attachmentNum");
        nativeQuery.setParameter("fileName", fileName);
        nativeQuery.setParameter("keyId1", keyId1);
        nativeQuery.setParameter("attachmentNum", attachmentNum);
        nativeQuery.setParameter("sdcId", sdcId);
        nativeQuery.addEntity(LVAttachment.class);

        return (LVAttachment) nativeQuery.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public LVAttachment findLastAttachmentByKeyId1(String keyId1) {
        Session unwrap = em.unwrap(Session.class);
        NativeQuery nativeQuery = unwrap.createNativeQuery("select sda.* from LVATTACHMENT sda " +
                "         where sda.keyid1=:keyId1 " +
                "         and sda.attachmentnum=(select max(attachmentnum) " +
                "          from LVATTACHMENT " +
                "         where LVATTACHMENT.keyid1=:keyId1)");
        nativeQuery.setParameter("keyId1", keyId1);
        nativeQuery.addEntity(LVAttachment.class);

        return (LVAttachment) nativeQuery.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] findHistoricDocuemntByDocumentId(String documentId) throws SQLException {
        Session unwrap = em.unwrap(Session.class);
        NativeQuery nativeQuery = unwrap.createNativeQuery("SELECT " +
                " a.* " +
                "FROM OPS$PENLIMS.NAI_DOCUMENTS a " +
                "WHERE   (a.document_id = :documentId)");
        nativeQuery.setParameter("documentId", documentId);
        nativeQuery.addEntity(NaiDocuments.class);
        NaiDocuments document = (NaiDocuments) nativeQuery.getSingleResult();
        return document.getDocumentBlob();
//        return documentStream;
    }
}
