package com.chinasoft.ctams.activity.addresssBook.util;


import com.chinasoft.ctams.bean.bean_json.OftenContactAddressBean;

import java.util.Comparator;

/**
 *根据首字母比较排序
 *
 */
public class PinyinComparator implements Comparator<OftenContactAddressBean> {

	public int compare(OftenContactAddressBean lhs, OftenContactAddressBean rhs) {
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
