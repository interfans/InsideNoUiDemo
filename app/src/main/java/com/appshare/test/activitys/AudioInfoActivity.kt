package cn.idaddy.test

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.idaddy.android.opensdk.device.IDYSdkApi
import cn.idaddy.android.opensdk.lib.IDYCommon
import cn.idaddy.android.opensdk.lib.audioinfo.OnGetAudioInfoCallback
import cn.idaddy.android.opensdk.lib.audioinfo.PushAudioInfo
import cn.idaddy.android.opensdk.lib.utils.ToastUtils
import com.appshare.test.adapters.ListAdapter
import com.appshare.test.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_audio_rank_list_layout.*


class AudioInfoActivity : AppCompatActivity(){
    lateinit var  LIstAdapter: ListAdapter

    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_rank_list_layout)
        LIstAdapter = ListAdapter(layoutInflater)
        audio_rank_list.adapter = LIstAdapter

        intent?.let {
            var audioId = it.getStringExtra("audioId")
            var chapterId = it.getStringExtra("chapterId")
            if (audioId.isNullOrBlank()) {
                audioId = "3807";
            }
            IDYSdkApi.getAudioInfo(null,audioId,chapterId,object : OnGetAudioInfoCallback {
                override fun onGetAudioInfoSuccess(successRet: String?) {
                    val pushAudioInfo = Gson().fromJson(successRet, PushAudioInfo::class.java)
                    if (pushAudioInfo != null) {
                        pushAudioInfo?.let {
                            list.add(Gson().toJson(pushAudioInfo))
                        }
                        LIstAdapter.refresh(list)

                        if (list.isEmpty()) {
                            ToastUtils.showShort(IDYCommon.application,"暂无列表信息")
                        }
                    }
                }

                override fun onGetAudioInfoFailed(failureRet: String?) {
                    ToastUtils.showShort(IDYCommon.application,failureRet)
                }
            })
        }
    }
}
