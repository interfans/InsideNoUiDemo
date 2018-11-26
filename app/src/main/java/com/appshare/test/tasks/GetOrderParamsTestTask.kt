package cn.idaddy.test

import android.os.AsyncTask
import cn.idaddy.android.opensdk.lib.api.OnTaskCallback
import com.appshare.test.TestConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class GetOrderParamsTestTask(private val orderParamsBean: OrderParamsBean, val callback: OnTaskCallback?) : AsyncTask<Void, Void, String>() {
    private var responseCode = 0
    private var responseMessage: String = ""

    override fun onPreExecute() {
        super.onPreExecute()
        callback?.onStart()
    }

    override fun doInBackground(vararg params: Void): String? {
        var response: Response? = null
        try {
            response = requestExe()
        } catch (e: Exception) {
            e.printStackTrace()
            responseCode = -1
            responseMessage = e.message.toString()
            return "response Exception " + e.message.toString()
        }
        responseCode = response?.code()
        responseMessage = response?.message() ?: ""
        return response?.body()?.string()
    }

    override fun onPostExecute(response: String?) {
        super.onPostExecute(response)
        if (response.isNullOrBlank()) {
            callback?.onError("response.isNullOrBlank")
            return
        }
        callback?.onSuccess(response!!)
    }

    @Throws(Exception::class)
    fun requestExe(): Response {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(TestConfig.IDY_PAY_URL +
                        "?sysId=" + orderParamsBean.params!!.sysId +
                        "&appId=" + orderParamsBean.params!!.appId +
                        "&tradeNo=" + orderParamsBean.params!!.tradeNo +
                        "&tradeTs=" + orderParamsBean.params!!.tradeTs +
                        "&goodsId=" + orderParamsBean.params!!.goodsId +
                        "&goodsDesc=" + orderParamsBean.params!!.goodsDesc +
                        "&amount=" + orderParamsBean.params!!.amount +
                        "&userId=" + orderParamsBean.params!!.userId +
                        "&deviceId=" + orderParamsBean.params!!.deviceId +
                        "&extra=" + orderParamsBean.params!!.extra +
                        "&ts=" + orderParamsBean.params!!.ts +
                        "&sign=" + orderParamsBean.params!!.sign +
                        "&signType=" + orderParamsBean.params!!.signType)
                .get()
                .build()

        return client.newCall(request).execute()
    }

}
