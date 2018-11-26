package com.appshare.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.idaddy.android.opensdk.device.IDYSdkApi
import cn.idaddy.android.opensdk.lib.IDYCommon
import cn.idaddy.android.opensdk.lib.OutBean.OutCategoryBean
import cn.idaddy.android.opensdk.lib.audioinfo.category.categorylist.OnGetCategoryListCallback
import cn.idaddy.android.opensdk.lib.utils.ToastUtils
import com.appshare.test.adapters.ListAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_audio_rank_list_layout.*

class CategoryListActivity : AppCompatActivity(){
    lateinit var  LIstAdapter: ListAdapter

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_rank_list_layout)
        LIstAdapter = ListAdapter(layoutInflater)
        audio_rank_list.adapter = LIstAdapter

        intent?.let {
            IDYSdkApi.getCategoryList(10,null,object : OnGetCategoryListCallback {
                override fun onGetCategoryListFailure(failureRet: String?) {
                    ToastUtils.showShort(IDYCommon.application,failureRet)
                }

                override fun onGetCategoryListSuccess(successRet: String?) {
                    val categoryBean = Gson().fromJson(successRet, OutCategoryBean::class.java)
                    for (bean in categoryBean.list?.let { it}
                            ?: ArrayList<OutCategoryBean.CategoryBean>()) {
                        bean?.let { list.add(Gson().toJson(bean)) }
                    }
                    LIstAdapter.refresh(list)

                    if (list.isEmpty()){
                        ToastUtils.showShort(IDYCommon.application,"暂无列表信息")
                    }
                }
            })
        }
    }
}
