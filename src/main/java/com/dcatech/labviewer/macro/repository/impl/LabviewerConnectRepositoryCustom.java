package com.dcatech.labviewer.macro.repository.impl;

import com.dcatech.labviewer.macro.model.ULabviewerconnectEntity;

import java.util.List;

/**
 * Created by LuiFer on 30/06/2017.
 */
public interface LabviewerConnectRepositoryCustom {


    List<ULabviewerconnectEntity> getUserConnections();

    ULabviewerconnectEntity findByUserId(String userId);

    String deleteUserId(String user);


}
