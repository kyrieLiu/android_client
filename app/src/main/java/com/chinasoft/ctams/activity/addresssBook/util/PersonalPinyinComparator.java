package com.chinasoft.ctams.activity.addresssBook.util;


import com.chinasoft.ctams.bean.bean_json.PersonalAddressBookBean;

import java.util.Comparator;

/**
 *根据首字母比较排序
 *
 */
public class PersonalPinyinComparator implements Comparator<PersonalAddressBookBean> {

	public int compare(PersonalAddressBookBean lhs, PersonalAddressBookBean rhs) {
		if (lhs.getSortLetters().equals("@")
				|| rhs.getSortLetters().equals("#")) {
			return -1;
		} else if (lhs.getSortLetters().equals("#")
				|| rhs.getSortLetters().equals("@")) {
			return 1;
		} else {
			return lhs.getSortLetters().compareTo(rhs.getSortLetters());
		}
	}

}
