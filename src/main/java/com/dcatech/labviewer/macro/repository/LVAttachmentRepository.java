package com.dcatech.labviewer.macro.repository;


import com.dcatech.labviewer.macro.model.LVAttachment;
import com.dcatech.labviewer.macro.repository.impl.LVAttachmentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LVAttachmentRepository extends JpaRepository<LVAttachment, String>, LVAttachmentRepositoryCustom {
}
