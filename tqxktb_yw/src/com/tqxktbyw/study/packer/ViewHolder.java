package com.tqxktbyw.study.packer;

import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder
{
	ImageView mIV, backImage;
	TextView mTV,mTV2;
	public TextView getmTV2()
	{
		return mTV2;
	}
	public void setmTV2(TextView mTV2)
	{
		this.mTV2 = mTV2;
	}
	public ImageView getmIV()
	{
		return mIV;
	}
	public void setmIV(ImageView mIV)
	{
		this.mIV = mIV;
	}
	public ImageView getBackImage()
	{
		return backImage;
	}
	public void setBackImage(ImageView backImage)
	{
		this.backImage = backImage;
	}
	public TextView getmTV()
	{
		return mTV;
	}
	public void setmTV(TextView mTV)
	{
		this.mTV = mTV;
	}
}
