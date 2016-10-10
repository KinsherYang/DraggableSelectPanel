package com.meitu.makeuppicker.app.makeuppicker.widget.makeuppanel.data;

import java.util.ArrayList;
import java.util.List;

import com.meitu.makeuppicker.app.makeuppicker.widget.makeuppanel.model.MakeupModel;
import com.yangsq.draggableselectpanel.model.IDGSPItem;
import com.yangsq.draggableselectpanel.model.ItemGroupModel;

/**
 * 妆容的数据操作
 * @author ysq 2016/9/28.
 */
public class MakeupProvider {


    /**
     * 管理中心的组code
     */
    public static final String MANAGE_CENTER_GROUP_CODE = "manageCenter";
    private static final String TEST_IMAGE_URI = "http://mvimg1.meitudata.com/569d0ff40833b9466.jpg";// 测试图片地址


    public interface GetMakeupListCallback {
        void onGetResult(List<MakeupModel> makeupModelList);
    }

    public interface GetMakeupGroupCallback {
        void onGetResult(List<ItemGroupModel> makeupGroupModelList);
    }





    /**
     * 获取妆容列表
     * @param callback
     */
    public void getMakeupGroup(GetMakeupGroupCallback callback) {
        // 模拟数据
        List<ItemGroupModel> makeupGroupModelList = new ArrayList<>();
        List<IDGSPItem> makeupModelList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MakeupModel makeupModel = new MakeupModel();
            makeupModel.setMakeupUri(TEST_IMAGE_URI);
            makeupModel.setMakeupName("测试妆容" + i);
            makeupModel.setMakeupId("ceshigroup1_" + i);
            makeupModelList.add(makeupModel);
        }
        ItemGroupModel makeupGroupModel = new ItemGroupModel();
        makeupGroupModel.setItemList(makeupModelList);
        makeupGroupModel.setGroupCode("group1");
        makeupGroupModel.setGroupName("自然");
        makeupGroupModelList.add(makeupGroupModel);
        // 第二组
        makeupModelList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            MakeupModel makeupModel = new MakeupModel();
            makeupModel.setMakeupUri(TEST_IMAGE_URI);
            makeupModel.setMakeupName("测试" + i);
            makeupModel.setMakeupId("ceshigroup2_" + i);
            makeupModelList.add(makeupModel);
        }
        makeupGroupModel = new ItemGroupModel();
        makeupGroupModel.setItemList(makeupModelList);
        makeupGroupModel.setGroupCode("group2");
        makeupGroupModel.setGroupName("韩妆");
        makeupGroupModelList.add(makeupGroupModel);
        // 第三组
        makeupModelList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MakeupModel makeupModel = new MakeupModel();
            makeupModel.setMakeupUri(TEST_IMAGE_URI);
            makeupModel.setMakeupName("测" + i);
            makeupModel.setMakeupId("ceshigroup3_" + i);
            makeupModelList.add(makeupModel);
        }
        // 添加两个前面组有的妆容
        MakeupModel makeupModel = new MakeupModel();
        makeupModel.setMakeupUri(TEST_IMAGE_URI);
        makeupModel.setMakeupName("测试妆容2");
        makeupModel.setMakeupId("ceshigroup1_2");
        makeupModelList.add(2, makeupModel);
        makeupModel = new MakeupModel();
        makeupModel.setMakeupUri(TEST_IMAGE_URI);
        makeupModel.setMakeupName("测试3");
        makeupModel.setMakeupId("ceshigroup2_3");
        makeupModelList.add(3, makeupModel);
        makeupGroupModel = new ItemGroupModel();
        makeupGroupModel.setItemList(makeupModelList);
        makeupGroupModel.setGroupCode("group3");
        makeupGroupModel.setGroupName("明星（存在之前重复的）");
        makeupGroupModelList.add(makeupGroupModel);
        if (callback != null) {
            callback.onGetResult(makeupGroupModelList);
        }
    }
}
