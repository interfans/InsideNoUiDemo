package com.appshare.test

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import cn.idaddy.android.opensdk.device.IDYSdkApi
import cn.idaddy.android.opensdk.lib.IDYCommon
import cn.idaddy.android.opensdk.lib.IDYConfig
import cn.idaddy.android.opensdk.lib.phone.OnGetVerifyCodeCallback
import cn.idaddy.android.opensdk.lib.user.IDYLoginIdaddyCallback
import cn.idaddy.android.opensdk.lib.utils.StringUtils
import cn.idaddy.test.*
import kotlinx.android.synthetic.main.activity_layout.*

class MainActivity : AppCompatActivity() {
    var heh:String = "heh"
    var pushText = ""  //推送的文案

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layout)

        findViewById<TextView>(R.id.prd_textView).text = BuildConfig.outputFileName

        //默认使用手机号登录
        usesilentlogincheckbox.isChecked = false
        IDYSdkApi.setUseIdaddyAccountValidate(true)
        usesilentlogincheckbox.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked){
                    login_with_mobile_cl.visibility = View.INVISIBLE
                    login_silent_cl.visibility = View.VISIBLE
                    IDYSdkApi.setUseIdaddyAccountValidate(false)
                }else{
                    login_with_mobile_cl.visibility = View.VISIBLE
                    login_silent_cl.visibility = View.INVISIBLE
                    IDYSdkApi.setUseIdaddyAccountValidate(true)
                }
            }
        })

        //静默登录
        login_silent.setOnClickListener {
            IDYConfig.userInfoBean.data = null

            if (editText_uniqueid.text.toString().isNullOrBlank()){
                IDYSdkApi.logout()
                Toast.makeText(this@MainActivity,"uniqueid 不能为空",Toast.LENGTH_SHORT).show()
            }else{
                TestConfig.uniquedId = editText_uniqueid.text.toString()
                IDYCommon.resetAuthorization()
                IDYCommon.idyTokenInterface?.idyNeedAccessToken()
                //可直接调用loginIdaddySilent 实现静默绑定登录
                //IDYSdkApi.loginIdaddySilent("uniqueId", null)
            }

            Handler().postDelayed(object :Runnable{
                override fun run() {
                    IDYConfig.userInfoBean.data?.let {
                        Toast.makeText(this@MainActivity,"  user_id :"+IDYConfig.userInfoBean.data!!.user_id
                                + "   nickname :"+IDYConfig.userInfoBean.data!!.nickname
                                +"    is_vip :"+ IDYConfig.userInfoBean.data!!.is_vip
                                + "   is_guest :"+IDYConfig.userInfoBean.data!!.is_guest  ,Toast.LENGTH_SHORT).show()
                    }?: Toast.makeText(this@MainActivity,"用户登录失败",Toast.LENGTH_SHORT).show()
                }
            },2000)
        }

        //发送验证码
        sendValidatecode.setOnClickListener {
            if (StringUtils.isValidOfPhoneNumber( edit_phoneNumber.text.toString())){
                IDYSdkApi.sendMobileVerifyCodeWithMobile( edit_phoneNumber.text.toString(),object :OnGetVerifyCodeCallback{
                    override fun onVerifyCodeFailed(failureRet: String?) {
                        Toast.makeText(this@MainActivity,failureRet,Toast.LENGTH_SHORT).show()
                    }

                    override fun onVerifyCodeSuccess(successRet: String?) {
                        Toast.makeText(this@MainActivity,"验证码获取成功",Toast.LENGTH_SHORT).show()
                    }
                })
            }else{
                Toast.makeText(this,"手机号码不正确",Toast.LENGTH_SHORT).show()
            }
        }

        //手机号 + 验证码登录
        login_with_mobile.setOnClickListener {
            if (!StringUtils.isValidOfPhoneNumber( edit_phoneNumber.text.toString())){
                Toast.makeText(this,"手机号码不正确",Toast.LENGTH_SHORT).show()
            }else if (StringUtils.isEmpty(edit_Validatecode.text.toString())){
                Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show()
            }else{
                IDYSdkApi.loginWithMobileVerifyCode(edit_phoneNumber.text.toString(),edit_Validatecode.text.toString(),object : IDYLoginIdaddyCallback {
                    override fun beforeLogin() {
                        //登录前，可自行显示loading...
                    }

                    override fun onLoginFailure(failureRet: Any?) {
                        Toast.makeText(this@MainActivity,"登录失败：+ ${failureRet}",Toast.LENGTH_SHORT).show()
                    }

                    override fun onLoginSuccess() {
                        Toast.makeText(this@MainActivity,"登录成功",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        findViewById<View>(R.id.main_user_logout_button).setOnClickListener {
            IDYSdkApi.logout()
        }

        findViewById<Button>(R.id.getOrderlistBtn).setOnClickListener{
            startActivity(Intent(this@MainActivity, OrderListActivity::class.java))
        }

        findViewById<Button>(R.id.getAudioRanklistBtn).setOnClickListener{
            startActivity(Intent(this@MainActivity, AudioRankTypeActivity::class.java))
        }

        findViewById<Button>(R.id.getPayParam_btn).setOnClickListener{
            startActivity(Intent(this@MainActivity, OrderPayParamActivity::class.java).putExtra("goodsId",editText_goodsId.text.toString()))
        }

        getTopicList_btn.setOnClickListener {
            startActivity(Intent(this@MainActivity,TopicAudioListActivity::class.java).putExtra("topicId",editText_topicId.text.toString()))
        }

        getAudioInfo_btn.setOnClickListener {
            startActivity(Intent(this@MainActivity, AudioInfoActivity::class.java).putExtra("audioId",editText_audioId.text.toString()).putExtra("chapterId",editText_chapterId.text.toString()))
        }

        getCategorylistBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, CategoryListActivity::class.java))
        }

        getCategoryAudiolistBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, AudioListByCategoryIdActivity::class.java)
                    .putExtra("categoryid",editText_audioId.text.toString()))
        }

        auth_create_btn.setOnClickListener{
            startActivity(Intent(this@MainActivity, AuthCreateActivity::class.java).putExtra("redeemCode",editText_redeemCode.text.toString()))
        }


//        //工爸账号验证----------
//        useIdaddyAccountValidate_box.isChecked = false
//        edit_phoneNumber_btn.isEnabled = false
//
//        edit_phoneNumber_btn.setTextColor(Color.GRAY)
//        useIdaddyAccountValidate_box.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
//            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
//                if (isChecked){
//                    IDYSdkApi.setUseIdaddyAccountValidate(true)
//                    edit_phoneNumber_btn.isEnabled = true
//                    edit_phoneNumber_btn.setTextColor(Color.BLACK)
//                }else{
//                    IDYSdkApi.setUseIdaddyAccountValidate(false)
//                    edit_phoneNumber_btn.isEnabled = false
//                    edit_phoneNumber_btn.setTextColor(Color.GRAY)
//                }
//            }
//        })
//
//        edit_phoneNumber_btn.setOnClickListener{
//            phoneNumber = edit_phoneNumber.text.toString()
//            if (StringUtils.isValidOfPhoneNumber(phoneNumber)){
//                IDYSdkApi.setUseIdaddyAccountValidate(true)
//                IDYSdkApi.setIdaddyAccountPhoneNumber(phoneNumber)
//            }else{
//                ToastUtils.showShort(this,"请输入正确的手机号")
//            }
//        }
        //工爸账号验证----------
    }
}
