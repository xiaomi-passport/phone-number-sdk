# XiaomiPhoneSdk

这个 sdk 可以用来 对比/获取 小米手机上sim卡激活后的手机号, 支持最新的miui版本

## 准备
在 dev.mi.com 注册申请成为开发者，接入应用，申请权限

## 使用 demo

使用申请的　clientId，publicKey　替换　demo　的　MainActivity　的值
在　demo　的　build.gradle　中替换签名文件为你的签名文件

## 使用 sdk

添加权限：

```xml
    <uses-permission android:name="com.xiaomi.simactivate.service.PERMISSION_PHONE" />
```

## 获取手机号接口返回结果（getPhone）
+ 成功

```json
{
    "code": 0,
    "data": {
        "resolve_result": "获取手机号结果的对称加密信息",
        "sym": "对称密钥经过RSA算法加密后的结果"
    },
    "description": "描述信息",
    "result": "ok"
}
```
resolve_result 中包含的是经过 AES 对称加密的结果信息，对称密钥位于 sym 字段中，sym 采用 RSA 非对称加密，相应 APP 可以由自己的公钥进行解密。

如果获取手机号成功，则 resolve_result 解密结果参考如下：

```json
{
    "status": "ok",
    "client_id": "client id",
    "pn": "获取到的手机号",
    "timestamp": "时间戳",
    "trace": "APP 传递的 trace"
}
```

如果获取手机号失败，则 resolve_result 解密结果参考如下：

```json
{
    "status": "error",
    "code":"错误码",
    "description": "错误描述信息",
    "timestamp": "时间戳",
    "trace": "APP 传递的 trace"
}
```

+ 失败

```json
{
    "code": "错误码",
    "description": "错误描述信息",
    "reason": "具体错误原因",
    "result": "error"
}
```

2. 验证手机号返回结果 （verifyPhone）
+ 成功

```json
{
    "code": 0,
    "data": {
        "resolve_result": "获取手机号结果的对称加密信息",
        "sym": "对称密钥经过RSA算法加密后的结果"
    },
    "description": "描述信息",
    "result": "ok"
}
```

resolve_result 中包含的是经过 AES 对称加密的结果信息，对称密钥位于 sym 字段中，sym 采用 RSA 非对称加密，相应 APP 可以由自己的公钥进行解密。

如果获取手机号成功，则 resolve_result 解密结果参考如下：

```json
{
    "status": "ok",
    "client_id": "client id",
    "pn": "获取到的手机号",
    "timestamp": "时间戳",
    "trace": "APP 传递的 trace"
}
```

如果获取手机号失败，则 resolve_result 解密结果参考如下：

```json
{
    "status": "error",
    "code":"错误码",
    "description": "错误描述信息",
    "timestamp": "时间戳",
    "trace": "APP 传递的 trace"
}
```

+ 失败

```json
{
    "code": "错误码",
    "description": "错误描述信息",
    "reason": "具体错误原因",
    "result": "error"
}
```