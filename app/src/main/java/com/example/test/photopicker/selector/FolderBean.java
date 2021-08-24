package com.example.test.photopicker.selector;

public class FolderBean {
	private String dir;
	private String firstImgPath;
	private String name;
	private int count;
	private boolean isSelected;
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
		int lastIndexOf = dir.lastIndexOf("/");
		if (lastIndexOf+1>dir.length()) {
			lastIndexOf=dir.length()-1;
		}
		if (lastIndexOf==-1) {
			lastIndexOf=0;
		}
		setName(dir.substring(lastIndexOf+1));
	}
	public String getFirstImgPath() {
		return firstImgPath;
	}
	public void setFirstImgPath(String firstImgPath) {
		this.firstImgPath = firstImgPath;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "FolderBean [dir=" + dir + ", firstImgPath=" + firstImgPath + ", name=" + name + ", count=" + count + "]";
	}
	

}
