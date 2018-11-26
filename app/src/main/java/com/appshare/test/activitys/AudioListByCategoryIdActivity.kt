package com.appshare.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.idaddy.android.opensdk.device.IDYSdkApi
import cn.idaddy.android.opensdk.lib.IDYCommon
import cn.idaddy.android.opensdk.lib.OutBean.OutAudioBean
import cn.idaddy.android.opensdk.lib.audioinfo.OnGetAudioListCallback
import cn.idaddy.android.opensdk.lib.utils.ToastUtils
import com.appshare.test.adapters.ListAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_audio_rank_list_layout.*

class AudioListByCategoryIdActivity : AppCompatActivity(){
    lateinit var  LIstAdapter: ListAdapter

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_rank_list_layout)
        LIstAdapter = ListAdapter(layoutInflater)
        audio_rank_list.adapter = LIstAdapter

        intent?.let {
            var categoryid = it.getStringExtra("categoryid")
            if (categoryid.isNullOrBlank()) {
                categoryid = "6708"
            }
            IDYSdkApi.getAudioListByCategoryId(categoryid,10,null,object : OnGetAudioListCallback {
                override fun onGetAudioListFailure(failureRet: String?) {
                    ToastUtils.showShort(IDYCommon.application,failureRet)
                }

                override fun onGetAudioListSuccess(successRet: String?) {
                    val cateaudiobean = Gson().fromJson(successRet, OutAudioBean::class.java)
                    for (audioBean in cateaudiobean.list?.let { it}
                            ?: ArrayList<OutAudioBean.AudioBean>()) {
                        audioBean?.let { list.add(Gson().toJson(audioBean)) }
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
