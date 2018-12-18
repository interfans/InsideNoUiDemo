package com.appshare.test.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import cn.idaddy.android.opensdk.device.IDYSdkApi
import cn.idaddy.android.opensdk.lib.IDYCommon
import cn.idaddy.android.opensdk.lib.OutBean.OutSearchAudioBean
import cn.idaddy.android.opensdk.lib.audioinfo.search.OnSearchAudioCallback
import cn.idaddy.android.opensdk.lib.audioinfo.search.SearchHotWordCallback
import cn.idaddy.android.opensdk.lib.utils.ToastUtils
import com.appshare.test.R
import com.appshare.test.adapters.ListAdapter
import com.appshare.test.bean.HotWordListBean
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_audio_rank_list_layout.*
import org.json.JSONObject

class SearchAudioActivity:AppCompatActivity(){
    lateinit var  LIstAdapter: ListAdapter

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_rank_list_layout)
        LIstAdapter = ListAdapter(layoutInflater)
        audio_rank_list.adapter = LIstAdapter

        intent?.let {
            var age:String? = it.getStringExtra("age")

            if (age.isNullOrEmpty()){
                age = null
            }

            IDYSdkApi.getSearchHotkeyWord(age?.toInt(),10,object :SearchHotWordCallback{
                override fun onSearchHotWordSuccess(successRet: String?) {
                    val hotKeyWordListBean = Gson().fromJson(successRet, HotWordListBean::class.java)

                    for (hotkey in hotKeyWordListBean.data) {
                        hotkey?.let { list.add(Gson().toJson(hotkey)) }
                    }
                    LIstAdapter.refresh(list)
                    if (list.isEmpty()){
                        ToastUtils.showShort(IDYCommon.application,"暂无热词信息")
                    }
                }

                override fun onSearchHotWordFailure(failureRet: String?) {
                    ToastUtils.showShort(IDYCommon.application,failureRet)
                }
            })

            audio_rank_list.setOnItemClickListener(object :AdapterView.OnItemClickListener{
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    IDYSdkApi.getSearchAudioList(JSONObject(LIstAdapter.getItem(position)).getString("keyword"),10,null,object :OnSearchAudioCallback{
                        override fun onSearchAudioFailure(failureRet: String?) {
                            ToastUtils.showShort(IDYCommon.application,"暂无热词信息")
                        }

                        override fun onSearchAudioSuccess(successRet: String?) {
                            val searchAudioListBean = Gson().fromJson(successRet, OutSearchAudioBean::class.java)
                            list.clear()
                            for (audioinfo in searchAudioListBean.list?.let { it }?: ArrayList()) {
                                audioinfo?.let { list.add(Gson().toJson(audioinfo)) }
                            }

                            LIstAdapter.refresh(list)
                            if (list.isEmpty()){
                                ToastUtils.showShort(IDYCommon.application,"暂无热词信息")
                            }
                        }
                    })
                }
            })
        }
    }

}
