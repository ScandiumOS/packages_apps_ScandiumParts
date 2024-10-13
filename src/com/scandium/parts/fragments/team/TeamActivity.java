/*
 * Copyright (C) 2020 The Dirty Unicorns Project
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

package com.scandium.parts.fragments.team;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.android.settings.R;

public class TeamActivity extends Activity {

    private List<DevInfoAdapter> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_recyclerview);
        
        initTeam();
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(layoutParams);
    }
    private void initTeam(){
        RecyclerView mRecycleview = findViewById(R.id.listView);
        mRecycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    TextView tv = findViewById(R.id.title_dev);
                    tv.setVisibility(View.GONE);
                } else {
                    // Scrolling down
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                    TextView tv = findViewById(R.id.title_dev);
                    tv.setVisibility(View.VISIBLE);
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                    TextView tv = findViewById(R.id.title_dev);
                    //tv.setVisibility(View.GONE);
                } else {
                    // Do something
                }
            }
        });

        // Project Manager
 //       setTeamMember("Aoihara", getString(R.string.projectmanager_title)
 //               + " / " + getString(R.string.maintainer_title), 
  //              "Aoihara", "Aoihara", R.drawable.aoihara);
        // Developers
//        setTeamMember("Muhammad Fikri", getString(R.string.developer_title)
//                + " / " + getString(R.string.maintainer_title), 
 //               "Genkzsz11", "Genkzsz11", R.drawable.genkzsz11);
 //       setTeamMember("Rafi Ramadhan", getString(R.string.developer_title)
 //               + " / " + getString(R.string.maintainer_title), 
 //               "Rafiester", "Rafiester", R.drawable.rafiester);
 //       setTeamMember("Joko Narimo", getString(R.string.developer_title)
 //               + " / " + getString(R.string.maintainer_title), 
 //               "703joko", "jrInfected", R.drawable.jrinfected);
        // Designers & Website Developers
 //       setTeamMember("Rayhan Nugroho", getString(R.string.contributor_title)
 //               + " / " + getString(R.string.designer_title), 
 //               "Akemiinawa", "SayuZX", R.drawable.bwcraft);
                
        // Maintainers
 //       setTeamMember("Hatsune", getString(R.string.maintainer_title), 
  //              "Hatsune71", "hats721", R.drawable.hatsune);

        // Veterans
 //       setTeamMember("Julian Surya", getString(R.string.veteran_title), 
 //               "juliansurya", "JulianSurya",R.drawable.juliansurya);
        
        ListAdapter mAdapter = new ListAdapter(mList);
        mRecycleview.setAdapter(mAdapter);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
    }

    private void setTeamMember(String devName, String devTitle,
                               String githubLink, String telegram, int devImage) {
        DevInfoAdapter adapter;

        adapter = new DevInfoAdapter();
        adapter.setImage(devImage);
        adapter.setDevName(devName);
        adapter.setDevTitle(devTitle);
        adapter.setGithubName(githubLink);
        adapter.setTelegramName(telegram);
        mList.add(adapter);
    }
}
