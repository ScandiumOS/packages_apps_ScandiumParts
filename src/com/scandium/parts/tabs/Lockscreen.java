/*
 * Copyright (C) 2017-2019 The Dirty Unicorns Project
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

package com.scandium.parts.tabs;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.scandium.NadUtils;

public class Lockscreen extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String FINGERPRINT_PREFS_CATEGORY = "fingerprint_prefs_category";
    private static final String LOCKSCREEN_ITEMS_CATEGORY = "lockscreen_items_category";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getContext().getContentResolver();
        boolean blurEnabled = Settings.System.getIntForUser(resolver,
                    Settings.System.BLUR_STYLE_PREFERENCE_KEY, 0, UserHandle.USER_CURRENT) == 1;
        addPreferencesFromResource(blurEnabled ? R.xml.tab_lockscreen_blur : R.xml.tab_lockscreen);

        Preference LockscreenItems = findPreference(LOCKSCREEN_ITEMS_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_lockscreen_items)) {
            getPreferenceScreen().removePreference(LockscreenItems);
        }

        Preference FingerprintPrefs = findPreference(FINGERPRINT_PREFS_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_fingerprint_prefs)) {
            getPreferenceScreen().removePreference(FingerprintPrefs);
        } else {
            if (!NadUtils.hasFingerprintSupport(getContext())) {
                getPreferenceScreen().removePreference(FingerprintPrefs);
            }
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.SCANDIUM_PRJ;
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return false;
    }
}
