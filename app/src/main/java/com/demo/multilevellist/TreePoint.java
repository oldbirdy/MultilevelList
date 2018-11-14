package com.demo.multilevellist;

/**
 * Created by xulc on 2018/7/27.
 */

public class TreePoint {
    private String ID;        // 7241,          //账号id
    private String NNAME; // "用户原因",    //原因名称
    private String PARENTID;   // 0,           //父id     0表示父节点
    private String ISLEAF;     //0,            //是否是叶子节点   1为叶子节点
    private int DISPLAY_ORDER; // 1       //同一个级别的显示顺序
    private boolean isExpand = false;  //是否展开了
    private boolean isSelected = false; //是否选中了


    public TreePoint(String ID, String NNAME, String PARENTID, String ISLEAF, int DISPLAY_ORDER) {
        this.ID = ID;
        this.NNAME = NNAME;
        this.PARENTID = PARENTID;
        this.ISLEAF = ISLEAF;
        this.DISPLAY_ORDER = DISPLAY_ORDER;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNNAME() {
        return NNAME;
    }

    public void setNAME(String NAME) {
        this.NNAME = NNAME;
    }

    public String getPARENTID() {
        return PARENTID;
    }

    public void setPARENTID(String PARENTID) {
        this.PARENTID = PARENTID;
    }

    public String getISLEAF() {
        return ISLEAF;
    }

    public void setISLEAF(String ISLEAF) {
        this.ISLEAF = ISLEAF;
    }

    public int getDISPLAY_ORDER() {
        return DISPLAY_ORDER;
    }

    public void setDISPLAY_ORDER(int DISPLAY_ORDER) {
        this.DISPLAY_ORDER = DISPLAY_ORDER;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}