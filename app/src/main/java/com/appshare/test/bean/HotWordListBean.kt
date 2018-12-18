package com.appshare.test.bean

class HotWordListBean {
    var data: List<DataBean> = ArrayList<DataBean>()

    class DataBean {
        /**
         * keyword : 冒险王
         * is_highlight : true
         */

        var keyword: String? = null
        var isIs_highlight: Boolean = false
    }
}
