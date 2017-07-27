package com.xiaomi.phone.phonesdkdemo;

import com.xiaomi.phone.AESCoder;
import com.xiaomi.phone.RSAUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.UnsupportedEncodingException;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RSAUtilsTest {
    private static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfXtNiHDjbV0CvvLcvLnus688zZUyG2sKUBnu/JW0fwna3Ikutr+acypk2FB1vDx0GJSClL3J/wj1Ah/h5Hk4lK6uMjWPF4ryfW9BgpwCtc8VHV3dC2XVwDfkCRhjf6/lVeeBiAp9U5eQnvjbEjGhCR6cNXJtGsqwP7CYnaDUmEwIDAQAB";
    private RSAUtils mRSAUtils;

    private String content = "8hMdIdbVzORq+bNZI0A/wohujghKZ/zNb6HxxSKH6lsq05uESAvDAXBNGCdK5ns5t3x3tcQhsZKRFRiRFVXf7vkMkc3qCdSE5GC+rKISy1+2sQObmszH+Z956XU9Xi1wiHasH7JsCkamPvgFPPjU8yjtALprYaKLiUkR95P3VJk=";
    private String sym = "N5ZU9x7P0KHZCiDjtr1r4doYgoPrGZD8bFjPaF7sGTfWSLKiqO3MvYPl3G9zU3KYXii8jZDUwbRp\r\n1pPs1C9Eqf85U88WFtJJHSTYblqlTsaDOUTgUhz3+WmOu2LBXJHGMiBmxkAfCWB5cAYX4FoZ+cP9\r\nNZmDh6hTDOv+N/+rO/w=\r\n";

    @Test
    public void test() throws RSAUtils.RSAException, UnsupportedEncodingException, AESCoder.CipherException {
        mRSAUtils = new RSAUtils(publicKeyStr);
        mRSAUtils.decrypt(content, sym);
    }
}
