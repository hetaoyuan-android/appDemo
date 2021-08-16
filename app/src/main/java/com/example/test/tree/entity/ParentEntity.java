package com.example.test.tree.entity;

import java.util.ArrayList;

/**
 *  子类分组的实体
 */
public class ParentEntity {

	private int groupColor;

	private String groupName;

	private ArrayList<ChildEntity> childs;

	
	/* ==========================================================
	 * ======================= get method =======================
	 * ========================================================== */
	
	public int getGroupColor() {
		return groupColor;
	}

	public String getGroupName() {
		return groupName;
	}

	public ArrayList<ChildEntity> getChilds() {
		return childs;
	}
	
	/* ==========================================================
	 * ======================= set method =======================
	 * ========================================================== */

	public void setGroupColor(int groupColor) {
		this.groupColor = groupColor;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setChilds(ArrayList<ChildEntity> childs) {
		this.childs = childs;
	}

}
