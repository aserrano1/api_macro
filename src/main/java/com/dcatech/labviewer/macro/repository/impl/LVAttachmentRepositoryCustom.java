package com.dcatech.labviewer.macro.repository.impl;

import com.dcatech.labviewer.macro.model.LVAttachment;

import java.sql.SQLException;

public interface LVAttachmentRepositoryCustom {
    LVAttachment findByKeyId1AndAttachmentNum(String fileName, String keyId1, Long attachmentNum, String sdcid);

    LVAttachment findLastAttachmentByKeyId1(String keyId1);

    byte[] findHistoricDocuemntByDocumentId(String documentId) throws SQLException;
}
