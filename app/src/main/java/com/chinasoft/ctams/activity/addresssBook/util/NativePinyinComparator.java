package com.chinasoft.ctams.activity.addresssBook.util;


import com.chinasoft.ctams.activity.addresssBook.bean.AddressBookSortModel;

import java.util.Comparator;


/**
 *根据首字母比较排序
 *
 */
public class NativePinyinComparator implements Comparator<AddressBookSortModel> {

	public int compare(AddressBookSortModel lhs, AddressBookSortModel rhs) {
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
