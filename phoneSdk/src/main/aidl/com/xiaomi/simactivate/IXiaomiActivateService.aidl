// PhoneApiService.aidl
package com.xiaomi.simactivate;

import android.os.Bundle;

interface IXiaomiActivateService {
    int isSupported(int apiVersion, String clientId, String type);
    Bundle verifyPhone(int apiVersion, String clientId, String trace, String phoneToVerify, in Bundle options);
    Bundle getPhone(int apiVersion, String clientId, String trace, in Bundle options);
}
