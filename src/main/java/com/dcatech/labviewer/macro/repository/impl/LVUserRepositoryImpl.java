package com.dcatech.labviewer.macro.repository.impl;

import com.dcatech.labviewer.macro.model.LVMenu;
import com.dcatech.labviewer.macro.model.LVMenuItems;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.Iterator;
import java.util.List;

//import javax.persistence.Query;

/**
 * Created by LuiFer on 30/06/2017.
 */
public class LVUserRepositoryImpl implements LVUserRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<String> getDataSources() throws PersistenceException {
        Session session = em.unwrap(Session.class);
//        Map<String, Object> properties = session.getProperties();
//        Metamodel metamodel = em.getMetamodel();
//        boolean isAdmSchema = false;
//        for (EntityType<?> entityType : metamodel.getEntities()) {
//            if (entityType.getName().equalsIgnoreCase("DATABASELIST")) {
//                isAdmSchema = true;
//                break;
//            }
//        }
//        if (isAdmSchema) {
        NativeQuery query = session.createNativeQuery("SELECT DATABASELISTID from DATABASELIST");
        return query.getResultList();
//        }
//        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<LVMenu> getLVMenuFiltered(String userId) {
        Session session = em.unwrap(Session.class);

        String sql = "select distinct {menu.*} from lvmenu  menu";
        session.enableFilter("SECURITYSET_FILTER").setParameter("userId", userId);
        NativeQuery query = session.createNativeQuery(sql);
        query.addEntity("menu", LVMenu.class);
//        query.addEntity("items", LVMenuItems.class);
//        query.setParameter("userId", userId);
        List<LVMenu> resultList = query.getResultList();
        for (Iterator<LVMenu> iterator = resultList.iterator(); iterator.hasNext(); ) {
            LVMenu menu = iterator.next();
            if (menu.getlVMenuItems().size() == 0) {
                iterator.remove();
            }
        }
        return resultList;
    }

    @Override
    public List<LVMenu> getLVMenuForUsuariosExternos(String addressId) {
        Session session = em.unwrap(Session.class);
        String sql = "select distinct {menu.*} " +
                "  from lvmenu menu " +
                " inner join lvmenuitems items " +
                " on items.menuoptionid = menu.u_menuoptionid " +
                " and items.securityset " +
                " in (select securityset from address where addressid = :addressId)";
        NativeQuery query = session.createNativeQuery(sql);
        query.addEntity("menu", LVMenu.class);
        query.setParameter("addressId", addressId);
        return query.getResultList();
    }

    @Override
    public List<LVMenuItems> getLVMenuItemsForInternalUsers(String userId) {
        Session session = em.unwrap(Session.class);
        String sql = "select {mi.*} " +
                "  from lvmenuitems mi, " +
                "       (select distinct securitysetid " +
                "  from securitysetitem ssi " +
                "         inner join (Select sysuserid securitysetitemid, " +
                "           'U' itemtypeflag " +
                "      from sysuser u " +
                "     where u.sysuserid = :userId " +
                "    union " +
                "    Select JOBTYPEID securitysetitemid, " +
                "           'J' itemtypeflag " +
                "      from sysuserjobtype uj " +
                "     where sysuserid = :userId " +
                "    union " +
                "    Select departmentid securitysetitemid, " +
                "           'D' itemtypeflag " +
                "      from departmentsysuser du " +
                "     where du.sysuserid = :userId " +
                "    union " +
                "    SELECT departmentid securitysetitemid, " +
                "           'D' itemtypeflag " +
                "      FROM jobtypedepartment jd " +
                "     inner join sysuserjobtype uj " +
                "        on jd.jobtypeid = uj.JOBTYPEID " +
                "     where uj.sysuserid = :userId) sec " +
                "    on ssi.securitysetitemid = sec.securitysetitemid " +
                "   and ssi.itemtypeflag = sec.itemtypeflag " +
                "         where ssi.securitysetsdcid = 'menuItem' " +
                "   AND ssi.operationid = 'list' " +
                "        union " +
                "        Select 'Total' " +
                "  from dual) SS " +
                " where nvl(mi.securityset, 'Total') = SS.securitysetid";
        NativeQuery query = session.createNativeQuery(sql);
        query.addEntity("mi", LVMenuItems.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<LVMenuItems> getLVMenuItemsForUsers(String userId) {
        Session session = em.unwrap(Session.class);
        String sql = "select distinct {items.*} from " +
                " lvmenuitems items " +
                "where items.u_menuitemid in (select mi.u_menuitemid " +
                "from u_menuitem mi, " +
                "( " +
                "  select distinct  securitysetid from  securitysetitem ssi inner join " +
                "  ( " +
                "  Select sysuserid securitysetitemid , 'U' itemtypeflag  from sysuser u " +
                "  where u.sysuserid = :userId " +
                "  union " +
                "  Select  JOBTYPEID securitysetitemid , 'J' itemtypeflag  from  sysuserjobtype uj " +
                "  where sysuserid = :userId " +
                "  union " +
                "  Select  departmentid securitysetitemid , 'D' itemtypeflag  from departmentsysuser du " +
                "  where du.sysuserid = :userId " +
                "  union " +
                "  SELECT departmentid securitysetitemid , 'D' itemtypeflag FROM jobtypedepartment jd inner join sysuserjobtype uj " +
                "  on jd.jobtypeid =  uj.JOBTYPEID " +
                "  where uj.sysuserid = :userId " +
                "  ) sec " +
                "  on ssi.securitysetitemid = sec.securitysetitemid " +
                "  and ssi.itemtypeflag = sec.itemtypeflag " +
                "  where ssi.securitysetsdcid = 'menuItem' " +
                "  AND ssi.operationid = 'list' " +
                "  union " +
                "  Select 'Total'  from  dual " +
                ") SS " +
                "where nvl(mi.securityset,'Total') = SS.securitysetid)" +
                " order by items.OrderItem";
        NativeQuery query = session.createNativeQuery(sql);
        query.addEntity("items", LVMenuItems.class);

        query.setParameter("userId", userId);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public String[] getUserAccess(String userName) {
        Session session = em.unwrap(Session.class);
        NativeQuery query = session.createNativeQuery("SELECT u_accesolabviewer, sysuserdesc, nvl(logonname, 'null') logonname, " +
                " sysuserid, email, lastjobtype " +
                " from sysuser " +
                " where upper(sysuserid)=upper(:userName) " +
                " or upper(logonname) = upper(:userName) " +
                " and activeflag = 'Y'");
        query.setParameter("userName", userName.toUpperCase());
        Object[] results = (Object[]) query.uniqueResult();

        String[] resultados = new String[results.length];
        for (int i = 0; i < results.length; i++) {
            resultados[i] = (results[i] != null ? results[i].toString() : "");
        }
        return resultados;
    }

}
