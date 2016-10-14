package com.chinasoft.ctams.activity.addresssBook.bean;

public class AddressBookSortModel {

	private String name; // 联系人姓名
	private String sortLetters; // 索引字母
	private String phoneNumber;// 手机号

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
}
