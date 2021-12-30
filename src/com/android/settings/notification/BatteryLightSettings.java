/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.notification;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import androidx.preference.*;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.dashboard.suggestions.SuggestionFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;

import com.huexxx.support.preferences.CustomSeekBarPreference;

import java.util.Arrays;
import java.util.List;

@SearchIndexable
public class BatteryLightSettings extends DashboardFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String TAG = "BatteryLightSettings";

    private Context mContext;

    private static final String KEY_BATTERY_LIGHT_INTENSITY = "battery_light_intensity";
    private static final String KEY_BATTERY_LIGHT_ON_TIME = "battery_light_on_time";
    private static final String KEY_BATTERY_LIGHT_OFF_TIME = "battery_light_off_time";

    private CustomSeekBarPreference mLightIntensity;
    private CustomSeekBarPreference mLightOnTime;
    private CustomSeekBarPreference mLightOffTime;

    private static final int mI1 = 0xFF0F0F0F;
    private static final int mI2 = 0xFF5F5F5F;
    private static final int mI3 = 0xFFAFAFAF;
    private static final int mI4 = 0xFFFFFFFF;

    //public static int mLightIntensityTemp = 0;
    //public static int mLightOnTimeTemp = 0;
    //public static int mLightOffTimeTemp = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
        ContentResolver resolver = mContext.getContentResolver();

        boolean mMultiColorLed = mContext.getResources().getBoolean(
                    com.android.internal.R.bool.config_multiColorBatteryLed);
        if (!mMultiColorLed) {
            mLightIntensity = (CustomSeekBarPreference) findPreference(KEY_BATTERY_LIGHT_INTENSITY);
            int lightIntensity = Settings.Global.getInt(resolver, Settings.Global.BATTERY_LIGHT_INTENSITY, mI4);
            int defaultLightIntensity = 4;
            mLightIntensity.setDefaultValue(defaultLightIntensity);
            mLightIntensity.setIntervalValue(1);
            if (lightIntensity == mI1) {
                lightIntensity = 1;
            } else if (lightIntensity == mI2) {
                lightIntensity = 2;
            } else if (lightIntensity == mI3) {
                lightIntensity = 3;
            } else if (lightIntensity == mI4) {
                lightIntensity = 4;
            } else {
                lightIntensity = defaultLightIntensity;
            }
            mLightIntensity.setValue(lightIntensity);
            mLightIntensity.setOnPreferenceChangeListener(this);
            //mLightIntensityTemp = lightIntensity;
        } else {
            getPreferenceScreen().removePreference(findPreference(KEY_BATTERY_LIGHT_INTENSITY));
        }
        mLightOnTime = (CustomSeekBarPreference) findPreference(KEY_BATTERY_LIGHT_ON_TIME);
        int defaultLightOn = mContext.getResources().getInteger(
                com.android.internal.R.integer.config_notificationsBatteryLedOn);
        int lightOn = Settings.Global.getInt(resolver, Settings.Global.BATTERY_LIGHT_ON_TIME, defaultLightOn);
        mLightOnTime.setDefaultValue(defaultLightOn);
        mLightOnTime.setIntervalValue(100);
        mLightOnTime.setValue(lightOn);
        mLightOnTime.setOnPreferenceChangeListener(this);
        //mLightOnTimeTemp = lightOn;

        mLightOffTime = (CustomSeekBarPreference) findPreference(KEY_BATTERY_LIGHT_OFF_TIME);
        int defaultLightOff = mContext.getResources().getInteger(
                com.android.internal.R.integer.config_notificationsBatteryLedOff);
        int lightOff = Settings.Global.getInt(resolver, Settings.Global.BATTERY_LIGHT_OFF_TIME, defaultLightOff);
        mLightOffTime.setDefaultValue(defaultLightOff);
        mLightOffTime.setIntervalValue(100);
        mLightOffTime.setValue(lightOff);
        mLightOffTime.setOnPreferenceChangeListener(this);
        //mLightOffTimeTemp = lightOff;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = mContext.getContentResolver();
        if (preference == mLightIntensity) {
            int val = (Integer) newValue;
            if (val == 1) {
                val = mI1;
            } else if (val == 2) {
                val = mI2;
            } else if (val == 3) {
                val = mI3;
            } else if (val == 4) {
                val = mI4;
            } else {
                val = mI4;
            }
            Settings.Global.putInt(resolver, Settings.Global.BATTERY_LIGHT_INTENSITY, val);
            //showLedPreview();
            //mLightIntensityTemp = val;
            return true;
        } else if (preference == mLightOnTime) {
            int val = (Integer) newValue;
            Settings.Global.putInt(resolver, Settings.Global.BATTERY_LIGHT_ON_TIME, val);
            //showLedPreview();
            //mLightOnTimeTemp = val;
            return true;
        } else if (preference == mLightOffTime) {
            int val = (Integer) newValue;
            Settings.Global.putInt(resolver, Settings.Global.BATTERY_LIGHT_OFF_TIME, val);
            //showLedPreview();
            //mLightOffTimeTemp = val;
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return -1;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.battery_light_settings;
    }
/*
    public static int getLightIntensityTemp() {
        return mLightIntensityTemp;
    }

    public static int getLightOnTimeTemp() {
        return mLightOnTimeTemp;
    }

    public static int getLightOffTimeTemp() {
        return mLightOffTimeTemp;
    }

    private void showLedPreview() {
        mNm.forcePulseLedLight(mLightIntensityTemp, mLightOnTimeTemp, mLightOffTimeTemp);
    }*/

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(
                        Context context, boolean enabled) {
                    final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.battery_light_settings;
                    return Arrays.asList(sir);
                }
            };
}
