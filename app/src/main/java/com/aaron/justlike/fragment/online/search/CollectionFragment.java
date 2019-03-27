package com.aaron.justlike.fragment.online.search;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aaron.justlike.JustLike;
import com.aaron.justlike.R;
import com.aaron.justlike.adapter.online.CollectionAdapter;
import com.aaron.justlike.http.unsplash.entity.collection.Collection;
import com.aaron.justlike.mvp.presenter.online.search.CollectionPresenter;
import com.aaron.justlike.mvp.presenter.online.search.ISearchPresenter;
import com.aaron.justlike.mvp.view.online.ISearchView;
import com.aaron.justlike.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionFragment extends Fragment implements ISearchView<Collection>,
        CollectionAdapter.Callback<Collection> {

    private ISearchPresenter<Collection> mPresenter;
    private Context mContext;

    private View mParentLayout;
    private View mErrorView;
    private ProgressBar mProgressBar;
    private View mFooterProgress;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    private String mKeyWord;
    private List<Collection> mCollectionList = new ArrayList<>();

    public CollectionFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParentLayout = inflater.inflate(R.layout.fragment_search_photo, container, false);
        mContext = getActivity();
        initView();
        attachPresenter();
        return mParentLayout;
    }

    @Override
    public void attachPresenter() {
        mPresenter = new CollectionPresenter();
        mPresenter.attachView(this);
    }

    @Override
    public void onShow(List<Collection> list) {
        mCollectionList.clear();
        mCollectionList.addAll(list);
        mAdapter.notifyItemRangeChanged(0, mCollectionList.size());
    }

    @Override
    public void onShowMore(List<Collection> list) {
        mCollectionList.addAll(list);
        mAdapter.notifyItemRangeInserted(mCollectionList.size() - list.size(), list.size());
    }

    @Override
    public void onShowMessage(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShowProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onHideEmptyView() {
        mErrorView.setVisibility(View.GONE);
    }

    @Override
    public void onShowLoading() {
        mFooterProgress.setVisibility(View.VISIBLE);
        ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
        animation.setFillAfter(true);
        animation.setDuration(250);
        mFooterProgress.startAnimation(animation);
    }

    @Override
    public void onHideLoading() {
        mFooterProgress.postDelayed(() -> {
            ScaleAnimation animation = new ScaleAnimation(1, 0, 1, 0, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
            animation.setFillAfter(true);
            animation.setDuration(250);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mFooterProgress.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            mFooterProgress.startAnimation(animation);
        }, 500);
    }

    @Override
    public void onPress(Collection collection) {

    }

    /**
     * Called by Activity
     */
    public void setKeyWord(String keyWord) {
        mKeyWord = keyWord;
        mPresenter.requestPhotos(ISearchPresenter.FIRST_REQUEST, keyWord);
    }

    private void initView() {
        mRecyclerView = mParentLayout.findViewById(R.id.recycler_view);
        mErrorView = mParentLayout.findViewById(R.id.error_view);
        mProgressBar = mParentLayout.findViewById(R.id.progress_bar);
        mFooterProgress = mParentLayout.findViewById(R.id.footer_progress);

        initRecyclerView();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new YItemDecoration());
        mAdapter = new CollectionAdapter(mCollectionList, this);
        mRecyclerView.setAdapter(mAdapter);
        // 监听是否滑到底部
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mCollectionList.size() != 0) {
                    boolean canScrollVertical = mRecyclerView.canScrollVertically(1);
                    if (!canScrollVertical && mFooterProgress.getVisibility() == View.GONE) {
                        mPresenter.requestPhotos(ISearchPresenter.LOAD_MORE, mKeyWord);
                    }
                }
            }
        });
    }

    private class YItemDecoration extends RecyclerView.ItemDecoration {

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.bottom = 0;
            outRect.top = SystemUtil.dp2px(JustLike.getContext(), 9.9F);
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = 0;
            }
        }
    }
}