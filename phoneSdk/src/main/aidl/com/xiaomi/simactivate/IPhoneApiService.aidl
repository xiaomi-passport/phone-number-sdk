// PhoneApiService.aidl
package com.xiaomi.simactivate;

import android.os.Bundle;

interface IPhoneApiService {
    int isSupported(int apiVersion, String clientId, String type);
    Bundle isSimActivated(int apiVersion, String clientId);
    Bundle verifyPhoneIntent(int apiVersion, String clientId, String trace, String phoneToVerify);
    Bundle getPhoneIntent(int apiVersion, String clientId, String trace);
}
