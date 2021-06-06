package com.dcatech.labviewer.macro.repository.impl;

import com.dcatech.labviewer.macro.model.LVMenu;
import com.dcatech.labviewer.macro.model.LVMenuItems;

import java.util.List;

/**
 * Created by LuiFer on 30/06/2017.
 */
public interface LVUserRepositoryCustom {

    List<String> getDataSources();

    List<LVMenu> getLVMenuFiltered(String userId);

    String[] getUserAccess(String userName);

    List<LVMenuItems> getLVMenuItemsForUsers(String addressId);

    List<LVMenuItems> getLVMenuItemsForInternalUsers(String addressId);

    List<LVMenu> getLVMenuForUsuariosExternos(String addressId);

}
