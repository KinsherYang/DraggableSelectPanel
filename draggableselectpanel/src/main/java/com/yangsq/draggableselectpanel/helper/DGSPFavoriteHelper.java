package com.yangsq.draggableselectpanel.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.yangsq.draggableselectpanel.model.IDGSPItem;
import com.yangsq.draggableselectpanel.util.FileUtils;

/**
 * @author ysq 2016/10/9.
 */
public class DGSPFavoriteHelper {
    private static Object fileLock = new Object();
    /**
     * 储存的文件名
     */
    private static final String DGSP_FAVORITE_FILE_NAME = "DGSPFavorite";
    /**
     * 妆容收藏组的组code
     */
    public static final String DGSP_FAVORITE_GROUP_CODE = "favorite";
    /**
     * 收藏组头部不可移动的项的数量，添加收藏的时候将在最后一个不可移动项后面添加
     */
    public static final int DGSP_FAVORITE_CANNOT_MOVE_ITEM_COUNT = 1;

    public interface GetItemListCallback {
        void onGetResult(List<IDGSPItem> dgspItemList);
    }

    /**
     * 获取所收藏的项目
     *
     * @param context
     * @param userId       用户id
     * @param callback
     * @param dataTypeName 收藏数据类型名，用来区分是那种收藏类型（比如妆容）
     */
    public static void getFavoriteItemList(final Context context, final String userId, final String dataTypeName,
                                           final GetItemListCallback callback) {
        new AsyncTask<Void, Void, List<IDGSPItem>>() {
            @Override
            protected List<IDGSPItem> doInBackground(Void... params) {
                synchronized (fileLock) {
                    List<IDGSPItem> favoriteList =
                            (List<IDGSPItem>) FileUtils.readObject(context, getFavoriteFileName(userId, dataTypeName));
                    return favoriteList;
                }
            }

            @Override
            protected void onPostExecute(List<IDGSPItem> makeupModelList) {
                if (callback != null) {
                    callback.onGetResult(makeupModelList);
                }
            }
        }.execute();
    }

    /**
     * 保存收藏
     *
     * @param dgspItems    数据列表
     * @param userId
     * @param dataTypeName 收藏数据类型名，用来区分是那种收藏类型（比如妆容）
     */
    public static void saveFavoriteList(final Context context, List<IDGSPItem> dgspItems, final String userId, final String dataTypeName) {
        if (dgspItems == null)
            dgspItems = new ArrayList<>();
        final List<IDGSPItem> finalList = new ArrayList<>();
        finalList.addAll(dgspItems);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 剔除取消项
                synchronized (fileLock) {
                    for (int i = 0; i < finalList.size(); i++) {
                        IDGSPItem dgspItem = finalList.get(i);
                        if (DGSPItemHelper.CANCEL_MODEL_CODE.equals(dgspItem.getCode())) {
                            finalList.remove(i);
                            i--;
                        }
                    }
                    FileUtils.writeObject(context, getFavoriteFileName(userId, dataTypeName), finalList);
                }
            }
        }).start();
    }


    /**
     * itemList，有的话就改变值
     * @param favoriteList 收藏列表
     * @param itemList 要检查的item列表
     */
    public static void checkItemListFavorite(List<IDGSPItem> favoriteList, List<IDGSPItem> itemList) {
        if (favoriteList == null || itemList == null) {
            return;
        }
        for (IDGSPItem favorite : favoriteList) {
            if (DGSPItemHelper.CANCEL_MODEL_CODE.equals(favorite.getCode()))
                continue;
            for (IDGSPItem item : itemList) {
                if (DGSPItemHelper.CANCEL_MODEL_CODE.equals(item.getCode()))
                    continue;
                if (favorite.getCode().equals(item.getCode())) {
                    item.setFavorite(true);
                    break;
                }
            }
        }
    }

    /**
     * 获取储存的收藏文件名
     *
     * @param userId       用户id
     * @param dataTypeName 收藏数据类型名，用来区分是那种收藏类型（比如妆容）
     * @return
     */
    private static String getFavoriteFileName(String userId, String dataTypeName) {
        return DGSP_FAVORITE_FILE_NAME + "_" + dataTypeName + "_" + userId;
    }
}
