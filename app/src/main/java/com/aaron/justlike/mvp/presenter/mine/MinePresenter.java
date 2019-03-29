package com.aaron.justlike.mvp.presenter.mine;

import com.aaron.justlike.entity.Image;
import com.aaron.justlike.mvp.model.mine.BaseModel;
import com.aaron.justlike.mvp.model.mine.IModel;
import com.aaron.justlike.mvp.view.mine.IMineView;
import com.aaron.justlike.util.FileUtil;

import java.util.List;

public class MinePresenter implements IMinePresenter<Image> {

    private static final String TAG = "MinePresenter";
    private static final int NO_SORT_STATUS = 0;
    public static final int SORT_BY_DATE = 1;
    public static final int SORT_BY_NAME = 2;
    public static final int SORT_BY_SIZE = 3;
    public static final boolean ASCENDING_ORDER = true;

    private int mSortType;
    private boolean mAscendingOrder;

    private IMineView<Image> mView;
    private IModel<Image> mModel;

    public MinePresenter(IMineView<Image> view) {
        // 同时持有 IMineView 和 IModel 引用
        mView = view;
        mModel = new BaseModel();
    }

    /**
     * 断开与 IMineView 的连接
     */
    @Override
    public void detachView() {
        mView = null;
    }

    /**
     * 请求数据并回调 IMineView 函数显示图片
     */
    @Override
    public void requestImage(List<Image> imageList, boolean refreshMode) {
        // Part 1, 请求排序状态
        if (mSortType == NO_SORT_STATUS) {
            String[] sortArray = mModel.querySortInfo();
            if (sortArray != null) {
                mSortType = Integer.parseInt(sortArray[0]);
                mAscendingOrder = Boolean.parseBoolean(sortArray[1]);
            } else {
                mSortType = SORT_BY_DATE;
                mAscendingOrder = ASCENDING_ORDER;
            }
        }
        // Part 2, 向 IModel 请求数据
        mModel.queryImage(new IModel.OnQueryImageListener<Image>() {
            @Override
            public void onSuccess(List<Image> list) {
                mView.onHideRefresh();
                if (refreshMode) {
                    if (imageList.containsAll(list) && imageList.size() == list.size()) {
                        mView.onHideRefresh();
                        mView.onShowMessage("暂时没有新增的图片");
                        return;
                    }
                }
                mView.onHideEmptyView();
                sortImageList(list, mSortType, mAscendingOrder);
                mView.onShowImage(list, mSortType, mAscendingOrder);
            }

            @Override
            public void onFailure(String args) {
                mView.onShowEmptyView();
                mView.onHideRefresh();
                mView.onShowMessage(args);
                mView.onShowImage(imageList, mSortType, mAscendingOrder);
            }
        });
    }

    @Override
    public void addImage(List<Image> list, List<String> pathList) {
        mModel.saveImage(pathList, savedList -> {
            mView.onHideEmptyView();
            mView.onShowAddImage(savedList);
        });
    }

    @Override
    public void deleteImage(String path, boolean isEmpty) {
        if (isEmpty) mView.onShowEmptyView();
        mModel.deleteImage(path);
    }

    /**
     * 设置排序类型，设立标志位，并将排序结果插入数据库
     */
    @Override
    public void setSortType(int sortType, boolean ascendingOrder) {
        mSortType = sortType;
        mAscendingOrder = ascendingOrder;
        mModel.insertSortInfo(sortType, ascendingOrder);
    }

    /**
     * 对 List 进行排序，根据传入参数决定排序类型
     */
    private void sortImageList(List<Image> imageList, int sortType, boolean ascendingOrder) {
        switch (sortType) {
            case SORT_BY_DATE:
                FileUtil.sortByDate(imageList, ascendingOrder);
                break;
            case SORT_BY_NAME:
                FileUtil.sortByName(imageList, ascendingOrder);
                break;
            case SORT_BY_SIZE:
                FileUtil.sortBySize(imageList, ascendingOrder);
                break;
        }
    }
}