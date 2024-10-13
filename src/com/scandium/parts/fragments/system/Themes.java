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

package com.nusantara.wings.fragments.system;

import android.app.UiModeManager;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.view.View;

import androidx.preference.ListPreference;
import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import com.nusantara.wings.UtilsNad;

import java.util.ArrayList;
import java.util.List;

import com.android.internal.util.nad.NadUtils;

import com.nusantara.support.preferences.CustomSeekBarPreference;
import com.nusantara.support.colorpicker.ColorPickerPreference;
import com.nusantara.support.preferences.SecureSettingListPreference;
import com.nusantara.support.preferences.SystemSettingListPreference;

@SearchIndexable(forTarget = SearchIndexable.ALL & ~SearchIndexable.ARC)
public class Themes extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    private static final String WALLPAPER_KEY = "monet_engine_use_wallpaper_color";
    private static final String COLOR_KEY = "monet_engine_color_override";
    private static final String CHROMA_KEY = "monet_engine_chroma_factor";
    private static final String SETTINGS_STYLE = "settings_style";

    private SwitchPreference mUseWallpaper;
    private ColorPickerPreference mColorOverride;
    private CustomSeekBarPreference mChromaFactor;
    private SecureSettingListPreference mTheme;
    private UiModeManager mUiModeManager;
    private SystemSettingListPreference mSettingsStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.nad_themes);
        final PreferenceScreen prefScreen = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();

        String color = Settings.Secure.getString(resolver, COLOR_KEY);
        boolean wall = color == null || color.isEmpty();
        mUseWallpaper = findPreference(WALLPAPER_KEY);
        mUseWallpaper.setChecked(wall);
        mUseWallpaper.setOnPreferenceChangeListener(this);

        mColorOverride = findPreference(COLOR_KEY);
        mColorOverride.setEnabled(!wall);
        if (!wall) {
            mColorOverride.setNewPreviewColor(
                ColorPickerPreference.convertToColorInt(color));
        }
        mColorOverride.setOnPreferenceChangeListener(this);

        mChromaFactor = findPreference(CHROMA_KEY);
        float chromaFactor = Settings.Secure.getFloat(resolver, CHROMA_KEY, 1) * 100;
        mChromaFactor.setValue(Math.round(chromaFactor));
        mChromaFactor.setOnPreferenceChangeListener(this);
        mUiModeManager = getContext().getSystemService(UiModeManager.class);
        mTheme = (SecureSettingListPreference) findPreference("system_theme");
        int theme = Settings.Secure.getInt(resolver, Settings.Secure.SYSTEM_THEME, 0);
        mTheme.setValue(String.valueOf(theme));
        boolean lightMode = (getContext().getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_YES) == 0;
        mTheme.setSummary(lightMode ? "Light" : mTheme.getEntry());
        mTheme.setOnPreferenceChangeListener(this);
        SystemSettingListPreference settingsStyle = mSettingsStyle = findPreference(SETTINGS_STYLE);
        int settingsStyleVal = Settings.System.getInt(resolver, SETTINGS_STYLE, 0);
        settingsStyle.setValue(String.valueOf(settingsStyleVal));
        settingsStyle.setSummary(settingsStyle.getEntry());
        settingsStyle.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mUseWallpaper) {
            boolean value = (Boolean) newValue;
            mColorOverride.setEnabled(!value);
            if (value) {
                Settings.Secure.putString(resolver, COLOR_KEY, "");
            }
            return true;
        } else if (preference == mColorOverride) {
            int value = (Integer) newValue;
            Settings.Secure.putString(resolver, COLOR_KEY,
                    ColorPickerPreference.convertToRGB(value));
            return true;
        } else if (preference == mChromaFactor) {
            int value = (Integer) newValue;
            Settings.Secure.putFloat(resolver, CHROMA_KEY, value / 100f);
            return true;
        } else if (preference == mTheme) {
            int value = Integer.parseInt((String) newValue);
            int index = mTheme.findIndexOfValue((String) newValue);
            Settings.Secure.putInt(resolver,
                    Settings.Secure.SYSTEM_THEME, value);
            boolean lightMode = (getContext().getResources().getConfiguration().uiMode
                    & Configuration.UI_MODE_NIGHT_YES) == 0;
            mTheme.setSummary(lightMode ? "Light" : mTheme.getEntries()[index]);
            if (lightMode) mUiModeManager.setNightModeActivated(true);
            return true;
        } else if (preference == mSettingsStyle) {
            int value = Integer.parseInt((String) newValue);
            SystemSettingListPreference settingsStyle = mSettingsStyle;
            int index = settingsStyle.findIndexOfValue((String) newValue);
            Settings.System.putInt(resolver,
                    SETTINGS_STYLE, value);
            settingsStyle.setSummary(settingsStyle.getEntries()[index]);
            UtilsNad.showSettingsRestartDialog(getContext());
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.NUSANTARA_PRJ;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                            boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();

                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.nad_themes;
                    result.add(sir);
                    return result;
                }

                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
            };
}
