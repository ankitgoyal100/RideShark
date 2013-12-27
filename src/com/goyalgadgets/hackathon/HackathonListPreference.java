package com.goyalgadgets.hackathon;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class HackathonListPreference extends ListPreference {
	public HackathonListPreference(final Context context) {
		this(context, null);
	}

	public HackathonListPreference(final Context context, final AttributeSet attrs) {
		super(context, attrs);

	}
	@Override
	public void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);
		if(positiveResult){
			super.setSummary(getEntry());
			notifyChanged();
		}
	}
}