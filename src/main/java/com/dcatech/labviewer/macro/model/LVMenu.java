package com.dcatech.labviewer.macro.model;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "LVMENU")
@FilterDef(name = "SECURITYSET_FILTER", parameters = @ParamDef(name = "userId", type = "string"))
public class LVMenu implements Serializable {
    @Id
    @Column(name = "ROWNUMID")
    private Long rowNumId;
    @Basic
    @Column(name = "U_MENUOPTIONID")
    private String uMenuOptionId;
    @Basic
    @Column(name = "LABELMENU")
    private String labelMenu;
    @Basic
    @Column(name = "ICONMENU")
    private String iconMenu;
    @Basic
    @Column(name = "ORDERMENU")
    @OrderColumn(name = "ORDERMENU")
    private Long orderMenu;

    @OneToMany(mappedBy = "lVMenuId", fetch = FetchType.EAGER)
    @OrderBy("orderItem ASC")
    @Filter(name = "SECURITYSET_FILTER", condition = "nvl(securityset, 'Total') in " +
            "(select distinct  ssi.securitysetid from  securitysetitem ssi " +
            "inner join   " +
            "(   Select sysuserid securitysetitemid , 'U' itemtypeflag  from sysuser u   where upper(u.sysuserid) = upper(:userId)   " +
            "union   " +
            "Select  JOBTYPEID securitysetitemid , 'J' itemtypeflag  from  sysuserjobtype uj   where upper(sysuserid) = upper(:userId)   " +
            "union   " +
            "Select  departmentid securitysetitemid , 'D' itemtypeflag  from departmentsysuser du   where upper(du.sysuserid) = upper(:userId)   " +
            "union   " +
            "SELECT departmentid securitysetitemid , 'D' itemtypeflag FROM jobtypedepartment jd " +
            "inner join sysuserjobtype uj   " +
            "on jd.jobtypeid =  uj.JOBTYPEID   " +
            "where upper(uj.sysuserid) = upper(:userId)   ) sec   " +
            "on ssi.securitysetitemid = sec.securitysetitemid   " +
            "and ssi.itemtypeflag = sec.itemtypeflag   " +
            "where ssi.securitysetsdcid = 'menuItem'   " +
            "AND ssi.operationid = 'list'   " +
            "union   " +
            "Select 'Total'  from  dual ) ", deduceAliasInjectionPoints = false)
//    @Filter(name = "SECURITYSET_FILTER", condition = "nvl(securityset, 'Total') in " +
//            "(select distinct  ssi.securitysetid from  securitysetitem ssi " +
//            "inner join   " +
//            "(   Select sysuser.sysuserid securitysetitemid , 'U' itemtypeflag  from sysuser u   where upper(u.sysuserid) = :userId   " +
//            "union   " +
//            "Select  sysuserjobtype.JOBTYPEID securitysetitemid , 'J' itemtypeflag  from  sysuserjobtype uj   where upper(sysuserid) = :userId   " +
//            "union   " +
//            "Select  departmentsysuser.departmentid securitysetitemid , 'D' itemtypeflag  from departmentsysuser du   where upper(du.sysuserid) = :userId   " +
//            "union   " +
//            "SELECT departmentid securitysetitemid , 'D' itemtypeflag FROM jobtypedepartment jd " +
//            "inner join sysuserjobtype uj   " +
//            "on jd.jobtypeid =  uj.JOBTYPEID   " +
//            "where upper(uj.sysuserid) = :userId   ) sec   " +
//            "on ssi.securitysetitemid = sec.securitysetitemid   " +
//            "and ssi.itemtypeflag = sec.itemtypeflag   " +
//            "where ssi.securitysetsdcid = 'menuItem'   " +
//            "AND ssi.operationid = 'list'   " +
//            "union   " +
//            "Select 'Total'  from  dual )", deduceAliasInjectionPoints = false)
//    @Where(clause = "SECURITYSET in (select  mi.u_menuitemid from u_menuitem mi, (   select distinct  securitysetid from  securitysetitem ssi inner join   (   Select sysuserid securitysetitemid , 'U' itemtypeflag  from sysuser u   where upper(u.sysuserid) = :userId   union   Select  JOBTYPEID securitysetitemid , 'J' itemtypeflag  from  sysuserjobtype uj   where upper(sysuserid) = :userId   union   Select  departmentid securitysetitemid , 'D' itemtypeflag  from departmentsysuser du   where upper(du.sysuserid) = :userId   union   SELECT departmentid securitysetitemid , 'D' itemtypeflag FROM jobtypedepartment jd inner join sysuserjobtype uj   on jd.jobtypeid =  uj.JOBTYPEID   where upper(uj.sysuserid) = :userId   ) sec   on ssi.securitysetitemid = sec.securitysetitemid   and ssi.itemtypeflag = sec.itemtypeflag   where ssi.securitysetsdcid = 'menuItem'   AND ssi.operationid = 'list'   union   Select 'Total'  from  dual ) SS where nvl(mi.securityset,'Total') = SS.securitysetid)")
    private Set<LVMenuItems> lVMenuItems;

    public Long getRowNumId() {
        return rowNumId;
    }

    public void setRowNumId(Long rownumid) {
        this.rowNumId = rownumid;
    }

    public String getuUMenuOptionId() {
        return uMenuOptionId;
    }

    public void setuUMenuOptionId(String uMenuoptionid) {
        this.uMenuOptionId = uMenuoptionid;
    }

    public String getLabelMenu() {
        return labelMenu;
    }

    public void setLabelMenu(String labelmenu) {
        this.labelMenu = labelmenu;
    }

    public String getIconMenu() {
        return iconMenu;
    }

    public void setIconMenu(String iconmenu) {
        this.iconMenu = iconmenu;
    }

    public Long getOrderMenu() {
        return orderMenu;
    }

    public void setOrderMenu(Long ordermenu) {
        this.orderMenu = ordermenu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LVMenu lvMenu = (LVMenu) o;

        if (rowNumId != null ? !rowNumId.equals(lvMenu.rowNumId) : lvMenu.rowNumId != null) return false;
        if (uMenuOptionId != null ? !uMenuOptionId.equals(lvMenu.uMenuOptionId) : lvMenu.uMenuOptionId != null)
            return false;
        if (labelMenu != null ? !labelMenu.equals(lvMenu.labelMenu) : lvMenu.labelMenu != null) return false;
        if (iconMenu != null ? !iconMenu.equals(lvMenu.iconMenu) : lvMenu.iconMenu != null) return false;
        if (orderMenu != null ? !orderMenu.equals(lvMenu.orderMenu) : lvMenu.orderMenu != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = rowNumId != null ? rowNumId.hashCode() : 0;
        result = 31 * result + (uMenuOptionId != null ? uMenuOptionId.hashCode() : 0);
        result = 31 * result + (labelMenu != null ? labelMenu.hashCode() : 0);
        result = 31 * result + (iconMenu != null ? iconMenu.hashCode() : 0);
        result = 31 * result + (orderMenu != null ? orderMenu.hashCode() : 0);
        return result;
    }

    public Set<com.dcatech.labviewer.macro.model.LVMenuItems> getlVMenuItems() {
        return lVMenuItems;
    }

    public void setlVMenuItems(Set<com.dcatech.labviewer.macro.model.LVMenuItems> lVMenuItems) {
        this.lVMenuItems = lVMenuItems;
    }
}
