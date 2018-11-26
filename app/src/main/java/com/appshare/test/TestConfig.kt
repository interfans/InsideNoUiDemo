package com.appshare.test

/**
 * Created by journey on 2018/9/1.
 */
object TestConfig {
    var uniquedId = (System.currentTimeMillis()/100).toString()
    //模拟支付成功回执
    var IDY_PAY_URL:String = "https://wt2-open.idaddy.cn/inside/api/v1/callback/pay/simulate"
}