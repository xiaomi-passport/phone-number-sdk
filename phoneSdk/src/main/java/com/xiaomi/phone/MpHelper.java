package com.xiaomi.phone;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.xiaomi.simactivate.IXiaomiActivateService;

import java.util.List;


import static android.app.Activity.RESULT_CANCELED;

public class MpHelper {
    private Context context;
    private ServiceConnection mServiceConn;
    private IXiaomiActivateService mService;

    private VerifyPhoneListener verifyPhoneListener;
    private GetPhoneListener getPhoneListener;

    // Is setup done?
    private boolean mSetupDone = false;

    private boolean mDisposed = false;

    private String mClientId;

    private String ACTION_BIND_SERVICE = "com.xiaomi.simactivate.service.ACTION_BIND_PHONE_SERVICE";
    private String PHONE_SERVICE_PACKAGE = "com.xiaomi.simactivate.service";

    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_RESULT = "RESPONSE_RESULT";

    private int getPhoneRequestCode = 1001;
    private int verifyPhoneRequestCode = 1002;

    public MpHelper(Context context, String clientId) {
        this.context = context.getApplicationContext();
        this.mClientId = clientId;
    }

    public void setUp(final SetupFinishedListener listener) {
        if (!isSupport(context)) {
            listener.onSetupFinished(new MpResult(Error.NOT_SUPPORTED, null));
            return;
        }
        Intent intent = new Intent(ACTION_BIND_SERVICE);
        intent.setPackage(PHONE_SERVICE_PACKAGE);
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, final IBinder iBinder) {
                mService = IXiaomiActivateService.Stub.asInterface(iBinder);
                mSetupDone = true;
                listener.onSetupFinished(new MpResult(Error.OK, null));
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
            }
        };
        context.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    public void setGetPhoneListener(GetPhoneListener getPhoneListener) {
        this.getPhoneListener = getPhoneListener;
    }

    public void setVerifyPhoneListener(VerifyPhoneListener verifyPhoneListener) {
        this.verifyPhoneListener = verifyPhoneListener;
    }

    /**
     *
     * @param trace 一段字符串，用来追踪一次请求，会在加密结果中原样返回
     * */
    public int getPhone(Activity act, String trace) throws RemoteException, IntentSender.SendIntentException {
        checkSetUp();
        if (getPhoneListener == null) {
            throw new IllegalStateException("getPhoneListener has not been set yet");
        }
        Bundle bundle = mService.getPhone(1, mClientId, trace, null);
        int code = bundle.getInt(RESPONSE_CODE);
        PendingIntent pendingIntent = bundle.getParcelable(RESPONSE_RESULT);
        if (code == Error.OK.code && pendingIntent != null) {
            act.startIntentSenderForResult(pendingIntent.getIntentSender(), getPhoneRequestCode, new Intent(),
                    0, 0, 0);
        }
        return code;
    }

    /**
     *
     * @param trace 一段字符串，用来追踪一次请求，会在加密结果中原样返回
     * @param phoneToVerify 需要对比的手机号
     * */
    public int verifyPhone(Activity act, String trace, String phoneToVerify) throws RemoteException, IntentSender.SendIntentException {
        checkSetUp();
        if (verifyPhoneListener == null) {
            throw new IllegalStateException("verifyPhoneListener has not been set yet");
        }
        Bundle bundle = mService.verifyPhone(1, mClientId, trace, phoneToVerify, null);
        int code = bundle.getInt(RESPONSE_CODE);
        PendingIntent pendingIntent = bundle.getParcelable(RESPONSE_RESULT);
        if (code == Error.OK.code && pendingIntent != null) {
            act.startIntentSenderForResult(pendingIntent.getIntentSender(), verifyPhoneRequestCode, new Intent(),
                    0, 0, 0);
        }
        return code;
    }

    public boolean handleOnActivatyResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == verifyPhoneRequestCode && verifyPhoneListener != null) {
            if (resultCode == RESULT_CANCELED) {
                verifyPhoneListener.onVerifyFinished(new MpResult(Error.CANCELED, null), null);
                return true;
            }
            Bundle bundle = data.getExtras();
            int code = bundle.getInt(RESPONSE_CODE);
            verifyPhoneListener.onVerifyFinished(new MpResult(code, null), bundle.getString(RESPONSE_RESULT));
            return true;
        }

        if (requestCode == getPhoneRequestCode && getPhoneListener != null) {
            if (resultCode == RESULT_CANCELED) {
                getPhoneListener.onGetPhoneFinished(new MpResult(Error.CANCELED, null), null);
                return true;
            }
            Bundle bundle = data.getExtras();
            int code = bundle.getInt(RESPONSE_CODE);
            getPhoneListener.onGetPhoneFinished(new MpResult(code, null), bundle.getString(RESPONSE_RESULT));
            return true;
        }
        return false;
    }

    public void dispose() {
        if (mServiceConn != null && context != null) {
            context.unbindService(mServiceConn);
        }
        mServiceConn = null;
        mService = null;
        mDisposed = true;
        mSetupDone = false;
    }

    private void checkSetUp() {
        if (mService == null || mSetupDone == false || mDisposed == true) {
            throw new IllegalStateException("MpHelper is not setup.");
        }
    }

    public interface SetupFinishedListener {
        void onSetupFinished(MpResult mpResult);
    }

    public interface VerifyPhoneListener {
        void onVerifyFinished(MpResult mpResult, String data);
    }

    public interface GetPhoneListener {
        void onGetPhoneFinished(MpResult mpResult, String data);
    }

    private boolean isSupport(Context context) {
        Intent intent = new Intent(ACTION_BIND_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> services = packageManager.queryIntentServices(intent, 0);
        return services.size() > 0;
    }
}
