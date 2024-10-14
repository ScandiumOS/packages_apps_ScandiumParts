/*
 * Copyright (C) 2014-2016 The Dirty Unicorns Project
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

package com.scandium.parts;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.ViewPager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.nano.MetricsProto;

import com.scandium.parts.fragments.team.TeamActivity;
import com.scandium.parts.bottomnav.BubbleNavigationConstraintView;
import com.scandium.parts.bottomnav.BubbleNavigationChangeListener;

public class ScandiumParts extends SettingsPreferenceFragment {

    Context mContext;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.scandiumparts, container, false);
        ActionBar actionBar = getActivity().getActionBar();
        mContext = getActivity();
        if (actionBar != null) {
            actionBar.setTitle(R.string.scandiumparts_title);
        }

        Fragment system = new com.scandium.parts.tabs.System();
        Fragment lockscreen = new com.scandium.parts.tabs.Lockscreen();
        Fragment statusbar = new com.scandium.parts.tabs.Statusbar();
        Fragment hardware = new com.scandium.parts.tabs.Hardware();

        Fragment fragment = (Fragment) getFragmentManager().findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentContainer, system);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        BubbleNavigationConstraintView bubbleNavigationConstraintView =  (BubbleNavigationConstraintView) view.findViewById(R.id.bottom_navigation_view_constraint);
        bubbleNavigationConstraintView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
            int id = view.getId();
               if (id == R.id.system) {
                    launchFragment(system);
               } else if (id == R.id.lockscreen) {
                    launchFragment(lockscreen);
               } else if (id == R.id.statusbar) {
                    launchFragment(statusbar);
               } else if (id == R.id.hardware) {
                    launchFragment(hardware);
               }
           }
       });

        ContentResolver resolver = getContext().getContentResolver();
        boolean blurEnabled = Settings.System.getIntForUser(resolver, Settings.System.BLUR_STYLE_PREFERENCE_KEY, 0, -2) == 1;
        boolean clearEnabled = Settings.Secure.getInt(resolver, Settings.Secure.SYSTEM_THEME, 0) == 2;

        int colorSurface = com.android.settingslib.Utils.getColorAttr(getContext(),
                com.android.internal.R.attr.colorSurfaceHeader).getDefaultColor();
        int colorPrimary = com.android.settingslib.Utils.getColorAttr(getContext(),
                com.android.internal.R.attr.colorPrimary).getDefaultColor();
        boolean setCustomColor = blurEnabled || clearEnabled || blurEnabled && clearEnabled;
        int customColor = setCustomColor ? colorSurface :  colorPrimary;
        View headerBg = view.findViewById(R.id.scandium_nav);
        View topRound = view.findViewById(R.id.top_round);

        Resources r = getContext().getResources();
        headerBg.setBackground(setCustomColor ? null :  r.getDrawable(R.drawable.scandium_bg_gaydient));
        Drawable header = headerBg.getBackground();
        if (header != null) header.setTint(colorSurface);
        bubbleNavigationConstraintView.setBackgroundColor(setCustomColor ? Color.TRANSPARENT : colorSurface);
        topRound.setVisibility(setCustomColor ? View.GONE : View.VISIBLE);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }

    private void launchFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        view = getView();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP &&
                    keyCode == KeyEvent.KEYCODE_BACK) {
                getActivity().finish();
                return true;
            }
            return false;
        });
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.SCANDIUM_PRJ;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_option, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.team) {
            Intent intent = new Intent(mContext, TeamActivity.class);
            mContext.startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.changelog) {
            Intent intent = new Intent(mContext, ChangelogActivity.class);
            mContext.startActivity(intent);
            return true;
        }
        return false;
    }
}
