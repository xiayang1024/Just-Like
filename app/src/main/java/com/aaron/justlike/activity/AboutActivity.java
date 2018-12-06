package com.aaron.justlike.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.aaron.justlike.R;
import com.aaron.justlike.adapter.AboutLibraryAdapter;
import com.aaron.justlike.adapter.AboutMessageAdapter;
import com.aaron.justlike.another.AboutLibrary;
import com.aaron.justlike.another.AboutMessage;
import com.aaron.justlike.util.SystemUtils;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AboutActivity extends AppCompatActivity {

    private final String[] mLibraryDetails = new String[]{
            "An image loading and caching library for Android focused on smooth scrolling",
            "A circular ImageView for Android",
            "A util for setting status bar style on Android App",
            "PhotoView 图片浏览缩放控件",
            "A well-designed local image and video selector for Android",
            "Image Cropping Library for Android",
            "An unofficial Splash API library for Android"};
    private List<AboutMessage> mAboutMessageList = new ArrayList<>();
    private List<AboutLibrary> mAboutLibraryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimary));
        initView();
    }

    /**
     * 标题栏返回键销毁活动
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        Toolbar toolbar = findViewById(R.id.activity_about_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        TextView versionName = findViewById(R.id.activity_about_version);
        versionName.setText("Version " + SystemUtils.getVersionName(this));
        initAboutMessage();
        initAboutLibrary();

        RecyclerView recyclerMessage = findViewById(R.id.activity_about_message_recycler);
        LinearLayoutManager messageManager = new LinearLayoutManager(this);
        recyclerMessage.setLayoutManager(messageManager);
        AboutMessageAdapter messageAdapter = new AboutMessageAdapter(this, mAboutMessageList);
        recyclerMessage.setAdapter(messageAdapter);

        RecyclerView recyclerLibrary = findViewById(R.id.activity_about_library_recycler);
        LinearLayoutManager libraryManager = new LinearLayoutManager(this);
        recyclerLibrary.setLayoutManager(libraryManager);
        AboutLibraryAdapter libraryAdapter = new AboutLibraryAdapter(this, mAboutLibraryList);
        recyclerLibrary.setAdapter(libraryAdapter);
    }

    private void initAboutMessage() {
        AboutMessage introduce = new AboutMessage(R.drawable.ic_introduce, "介绍");
        mAboutMessageList.add(introduce);
        AboutMessage email = new AboutMessage(R.drawable.ic_email, "反馈");
        mAboutMessageList.add(email);
        AboutMessage sourceCode = new AboutMessage(R.drawable.ic_source_code, "源码");
        mAboutMessageList.add(sourceCode);
        AboutMessage github = new AboutMessage(R.drawable.ic_github, "GITHUB");
        mAboutMessageList.add(github);
    }

    private void initAboutLibrary() {
        AboutLibrary glide = new AboutLibrary("Glide",
                "bumptech", mLibraryDetails[0]);
        mAboutLibraryList.add(glide);
        AboutLibrary circleImageview = new AboutLibrary("CircleImageView",
                "hdodenhof", mLibraryDetails[1]);
        mAboutLibraryList.add(circleImageview);
        AboutLibrary statusBarUtil = new AboutLibrary("StatusBarUtil",
                "Jaeger", mLibraryDetails[2]);
        mAboutLibraryList.add(statusBarUtil);
        AboutLibrary photoView = new AboutLibrary("PhotoView",
                "bm-x", mLibraryDetails[3]);
        mAboutLibraryList.add(photoView);
        AboutLibrary pictureSelector = new AboutLibrary("Matisse",
                "zhihu", mLibraryDetails[4]);
        mAboutLibraryList.add(pictureSelector);
        AboutLibrary uCrop = new AboutLibrary("uCrop",
                "Yalantis", mLibraryDetails[5]);
        mAboutLibraryList.add(uCrop);
        AboutLibrary androidUnsplash = new AboutLibrary("AndroidUnsplash(Unofficial)",
                "KeenenCharles", mLibraryDetails[6]);
        mAboutLibraryList.add(androidUnsplash);
    }
}
