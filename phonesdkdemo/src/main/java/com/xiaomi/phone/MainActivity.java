package com.xiaomi.phone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.xiaomi.phone.phonesdkdemo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity implements View.OnClickListener, MpHelper.SetupFinishedListener, MpHelper.VerifyPhoneListener, MpHelper.GetPhoneListener {
    private static final String TAG = "XiaomiPhoneSdkDemo";

    private MpHelper mpHelper;
    private boolean setUpFlag = false;

    // 前往 dev.mi.com 申请, 替换
    private static String clientId = "2882303761517597158";

    // 前往 dev.mi.com 申请, 替换. 请妥善保存这个公钥，不要泄露，尽量不要放在客户端，最好在服务端完成解密
    private static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIf3D4IvT53RqEAh7DaKZ2PE+XUSvDBxExORpt\n" +
            "+1S8KcCPna7YBdBcBok8mWjnMntfWlLgsoPExrkNgg3zXjt9VPrpfCdQbrTcWSn11nHhmeHOSL8v\n" +
            "RG2vNej8du7IXFVJIJJKJl4mj4GCWNG+YEvYfOxPbM5jqrCWJPBccc/geQIDAQAB\n";
    private RSAUtils mRSAUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.verify).setOnClickListener(this);
        findViewById(R.id.get_phone).setOnClickListener(this);

        try {
            mRSAUtils = new RSAUtils(publicKeyStr);
        } catch (RSAUtils.RSAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 初始化帮助类，setup 的回调是 onSetupFinished
        mpHelper = new MpHelper(this, clientId);
        mpHelper.setUp(this);

        mpHelper.setGetPhoneListener(this);
        mpHelper.setVerifyPhoneListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 使用完记得dispose，防止内存泄露
        mpHelper.dispose();
    }

    @Override
    public void onClick(View v) {
        if (!setUpFlag) {
            return;
        }
        switch (v.getId()) {
            case R.id.verify:
                EditText et = (EditText) findViewById(R.id.phone);
                verifyPhoneNumber(et.getText().toString());
                break;
            case R.id.get_phone:
                getPhoneNumber();
                break;
            default:
                throw new IllegalStateException("not here");
        }
    }

    @Override
    public void onSetupFinished(MpResult mpResult) {
        if (mpResult.isFailure()) {
            showDialogText("setUp failed " + mpResult.getMessage());
        }
        setUpFlag = true;
        Log.v(TAG, mpResult.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 在 onActivityResult 回调 handleOnActivatyResult 处理返回结果
        mpHelper.handleOnActivatyResult(requestCode, resultCode, data);
    }

    private void verifyPhoneNumber(final String phoneToVerify) {
        try {
            // traceId 一段字符串，用来追踪一次请求，会在加密结果中原样返回
            // phoneToVerify 需要对比的手机号
            mpHelper.verifyPhone(this, "traceId", phoneToVerify);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVerifyFinished(MpResult mpResult, String data) {
        if (data == null) {
            showDialogText(mpResult.toString());
            return;
        }

        try {
            // 返回结果的详细格式 错误码，请参考 dev.mi.com
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.getInt("code");
            if (code == 0) {
                JSONObject dataObj = jsonObject.getJSONObject("data");
                showDialogText(mRSAUtils.decrypt(dataObj.getString("validate_result"), dataObj.getString("sym")));
            } else {
                showDialogText(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RSAUtils.RSAException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (AESCoder.CipherException e) {
            e.printStackTrace();
        }
    }

    private void getPhoneNumber() {
        try {
            // traceId 一段字符串，用来追踪一次请求，会在加密结果中原样返回
            mpHelper.getPhone(this, "traceId");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGetPhoneFinished(MpResult mpResult, String data) {
        if (data == null) {
            showDialogText(mpResult.toString());
            return;
        }

        try {
            // 返回结果的详细格式 错误码，请参考 dev.mi.com
            JSONObject jsonObject = new JSONObject(data);
            int code = jsonObject.getInt("code");
            if (code == 0) {
                JSONObject dataObj = jsonObject.getJSONObject("data");
                String result = mRSAUtils.decrypt(dataObj.getString("resolve_result"), dataObj.getString("sym"));
                showDialogText(result);
            } else {
                showDialogText(data);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RSAUtils.RSAException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (AESCoder.CipherException e) {
            e.printStackTrace();
        }
    }

    private void showDialogText(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(MainActivity.this).setMessage(msg).show();
            }
        });
    }
}