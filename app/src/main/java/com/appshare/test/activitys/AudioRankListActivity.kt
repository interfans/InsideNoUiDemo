package cn.idaddy.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.idaddy.android.opensdk.device.IDYSdkApi
import cn.idaddy.android.opensdk.lib.IDYCommon
import cn.idaddy.android.opensdk.lib.OutBean.OutAudioRankBean
import cn.idaddy.android.opensdk.lib.audioinfo.rank.OnGetAudioRankListCallback
import cn.idaddy.android.opensdk.lib.utils.ToastUtils
import com.appshare.test.adapters.ListAdapter
import com.appshare.test.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_audio_rank_list_layout.*

class AudioRankListActivity :AppCompatActivity(){
    lateinit var  LIstAdapter: ListAdapter

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_rank_list_layout)
        LIstAdapter = ListAdapter(layoutInflater)
        audio_rank_list.adapter = LIstAdapter

        intent?.let {
            val rankType = it.getStringExtra("rankType")

            IDYSdkApi.getAudioRankList(rankType,10,null,object : OnGetAudioRankListCallback {
                override fun onGetAudioRankSuccess(successRet: String?) {
                    val audioRankBean = Gson().fromJson(successRet, OutAudioRankBean::class.java)
                    for (audioBean in audioRankBean.list?.let { it}
                            ?: ArrayList<OutAudioRankBean.OutAudioBean>()) {
                        audioBean?.let { list.add(Gson().toJson(audioBean)) }
                    }
                    LIstAdapter.refresh(list)
                    if (list.isEmpty()){
                        ToastUtils.showShort(IDYCommon.application,"暂无排行信息")
                    }
                }

                override fun onGetAudioRankFailure(failureRet: String?) {
                    ToastUtils.showShort(IDYCommon.application,failureRet)
                }
            })
        }
    }
}
