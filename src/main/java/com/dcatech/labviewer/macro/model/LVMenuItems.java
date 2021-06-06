package com.dcatech.labviewer.macro.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "LVMENUITEMS")
//@FilterDef(name="minLength", parameters=@ParamDef( name="SECURITYSET", type="string" ) )
//@Filters( {
//        @Filter(name="securityEq", condition=":SECURITYSET = SECURITYSET")
//} )
//@WhereJoinTable(clause = "SECURITYSET = ")
//@NamedQueries({
//        @NamedQuery(
//                name = "findStockByStockCode",
//                query = "from LVMenu s where s.stockCode = :stockCode"
//        )
//})
public class LVMenuItems implements Serializable {
    @Id
    @Column(name = "ROWNUMID")
    private Long rowNumId;
    @Basic
    @Column(name = "U_MENUITEMID")
    private String uMenuItemId;
    @Basic
    @Column(name = "LABELITEM")
    private String labelItem;
    @Basic
    @Column(name = "ICONITEM")
    private String iconItem;
    @Basic
    @Column(name = "QUERYID")
    private String queryId;
    @Basic
    @OrderBy
    @Column(name = "ORDERITEM")
    private Long orderItem;
    @Basic
    @Column(name = "TYPEITEM")
    private String typeItem;
    @Basic
    @Column(name = "SECURITYSET")
    private String securitySet;
    @Basic
    @Column(name = "VISIBLE")
    private String visible;
    @Basic
    @Column(name = "CLICK_COUNT")
    private Long clickcount;

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    @ManyToOne
    @JoinColumn(name = "MENUOPTIONID", referencedColumnName = "U_MENUOPTIONID")
    @JsonIgnoreProperties("lVMenuItems")
    private LVMenu lVMenuId;

    public String getSecuritySet() {
        return securitySet;
    }

    public void setSecuritySet(String securitySet) {
        this.securitySet = securitySet;
    }

    public Long getRowNumId() {
        return rowNumId;
    }

    public void setRowNumId(Long rowNumId) {
        this.rowNumId = rowNumId;
    }

    public String getuMenuItemId() {
        return uMenuItemId;
    }

    public void setuMenuItemId(String uMenuItemId) {
        this.uMenuItemId = uMenuItemId;
    }

    public String getLabelItem() {
        return labelItem;
    }

    public void setLabelItem(String labelItem) {
        this.labelItem = labelItem;
    }

    public String getIconItem() {
        return iconItem;
    }

    public void setIconItem(String iconItem) {
        this.iconItem = iconItem;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public Long getClickCount() {
        return clickcount;
    }

    public void setOClickCount(Long count) {
        this.clickcount = orderItem;
    }

    public Long getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(Long orderItem) {
        this.orderItem = orderItem;
    }

    public LVMenu getlVMenuId() {
        return lVMenuId;
    }

    public void setlVMenuId(LVMenu lVMenuId) {
        this.lVMenuId = lVMenuId;
    }

    public String getTypeItem() {
        return typeItem;
    }

    public void setTypeItem(String typeItem) {
        this.typeItem = typeItem;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LVMenuItems that = (LVMenuItems) o;

        if (rowNumId != null ? !rowNumId.equals(that.rowNumId) : that.rowNumId != null) return false;
        if (uMenuItemId != null ? !uMenuItemId.equals(that.uMenuItemId) : that.uMenuItemId != null) return false;
        if (labelItem != null ? !labelItem.equals(that.labelItem) : that.labelItem != null) return false;
        if (iconItem != null ? !iconItem.equals(that.iconItem) : that.iconItem != null) return false;
        if (queryId != null ? !queryId.equals(that.queryId) : that.queryId != null) return false;
        if (orderItem != null ? !orderItem.equals(that.orderItem) : that.orderItem != null) return false;
        if (typeItem != null ? !typeItem.equals(that.typeItem) : that.typeItem != null) return false;
        if (securitySet != null ? !securitySet.equals(that.securitySet) : that.securitySet != null) return false;
        if (visible != null ? !visible.equals(that.visible) : that.visible != null) return false;
        return lVMenuId != null ? lVMenuId.equals(that.lVMenuId) : that.lVMenuId == null;
    }

    @Override
    public int hashCode() {
        int result = rowNumId != null ? rowNumId.hashCode() : 0;
        result = 31 * result + (uMenuItemId != null ? uMenuItemId.hashCode() : 0);
        result = 31 * result + (labelItem != null ? labelItem.hashCode() : 0);
        result = 31 * result + (iconItem != null ? iconItem.hashCode() : 0);
        result = 31 * result + (queryId != null ? queryId.hashCode() : 0);
        result = 31 * result + (orderItem != null ? orderItem.hashCode() : 0);
        result = 31 * result + (typeItem != null ? typeItem.hashCode() : 0);
        result = 31 * result + (securitySet != null ? securitySet.hashCode() : 0);
        result = 31 * result + (lVMenuId != null ? lVMenuId.hashCode() : 0);
        result = 31 * result + (visible != null ? visible.hashCode() : 0);
        return result;
    }
}
