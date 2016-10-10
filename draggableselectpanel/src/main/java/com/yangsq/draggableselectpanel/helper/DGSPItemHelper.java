package com.yangsq.draggableselectpanel.helper;

import com.yangsq.draggableselectpanel.R;
import com.yangsq.draggableselectpanel.model.ItemModel;

/**
 * Created by Administrator on 2016/10/9 0009.
 */

public class DGSPItemHelper {
    /**
     * 取消选项的code
     */
    public static final String CANCEL_MODEL_CODE = "cancel";

    /**
     * 生成取消选择的makeupmodel
     * @return
     */
    public static ItemModel getCancelSelectModel() {
        ItemModel dgspItem = new ItemModel();
        dgspItem.setImageDrawableId(R.drawable.dgsp_cancel_select_ico);
        dgspItem.setName("无");
        dgspItem.setCode(CANCEL_MODEL_CODE);
        return dgspItem;
    }
}
