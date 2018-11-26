package com.appshare.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.idaddy.android.opensdk.device.IDYSdkApi
import cn.idaddy.android.opensdk.lib.IDYCommon

import cn.idaddy.android.opensdk.lib.OutBean.OutAudioBean
import cn.idaddy.android.opensdk.lib.audioinfo.topic.OnGetTopicListCallBack
import cn.idaddy.android.opensdk.lib.utils.ToastUtils
import com.appshare.test.adapters.ListAdapter
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_audio_rank_list_layout.*

class TopicAudioListActivity : AppCompatActivity(){
    lateinit var  LIstAdapter: ListAdapter

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_rank_list_layout)
        LIstAdapter = ListAdapter(layoutInflater)
        audio_rank_list.adapter = LIstAdapter

        intent?.let {
            var topicId = it.getStringExtra("topicId")
            if (topicId.isNullOrBlank()) {
                topicId = "542";
            }
            IDYSdkApi.getTopicList(topicId,10,null,object : OnGetTopicListCallBack {
                override fun onGetTopicListSuccess(successRet: String?) {
                    val topicAudioBean = Gson().fromJson(successRet, OutAudioBean::class.java)
                    for (audioBean in topicAudioBean.list?.let { it}
                            ?: ArrayList<OutAudioBean.AudioBean>()) {
                        audioBean?.let { list.add(Gson().toJson(audioBean)) }
                    }
                    LIstAdapter.refresh(list)

                    if (list.isEmpty()){
                        ToastUtils.showShort(IDYCommon.application,"暂无列表信息")
                    }
                }


                override fun onGetTopicListFailure(failureRet: String?) {
                    ToastUtils.showShort(IDYCommon.application,failureRet)
                }
            })
        }
    }
}
