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

package com.nusantara.wings.tabs;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

public class System extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {


    private static final String GAME_SPACE_CATEGORY = "game_space_category";
    private static final String NOTIFICATIONS_CATEGORY = "notifications_category";
    private static final String MISC_CATEGORY = "miscellaneous_category";
    private static final String THEMES_CATEGORY = "themes_category";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getContext().getContentResolver();
        boolean blurEnabled = Settings.System.getIntForUser(resolver,
                    Settings.System.BLUR_STYLE_PREFERENCE_KEY, 0, UserHandle.USER_CURRENT) == 1;
        addPreferencesFromResource(blurEnabled ? R.xml.tab_system_blur : R.xml.tab_system);

        Preference GameSpace = findPreference(GAME_SPACE_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_game_space_available)) {
            getPreferenceScreen().removePreference(GameSpace);
        }

        Preference Notifications = findPreference(NOTIFICATIONS_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_notifications)) {
            getPreferenceScreen().removePreference(Notifications);
        }

        Preference MiscOptions = findPreference(MISC_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_misc_options)) {
            getPreferenceScreen().removePreference(MiscOptions);
        }

        Preference Themes = findPreference(THEMES_CATEGORY);
        if (!getResources().getBoolean(R.bool.has_themes)) {
            getPreferenceScreen().removePreference(Themes);
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
        return MetricsProto.MetricsEvent.NUSANTARA_PRJ;
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return false;
    }
}
