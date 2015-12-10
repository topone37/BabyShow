package com.tp.bsclient.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * @author 谭盼
 * @description 自定义的FragmentPagerAdapter
 * @date 2015年8月5日 上午11:35:21
 */
public class ContentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;


	public ContentPagerAdapter(FragmentManager fm, List<Fragment> mFragments
	) {
		super(fm);
		this.mFragments = mFragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return (mFragments == null || mFragments.size() == 0) ? null
				: mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return (mFragments == null) ? 0 : mFragments.size();
	}

}
