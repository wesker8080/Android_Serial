package android_serialport_api;

/**
 * @author Wesker
 * @create 2019-01-25 10:41
 */
public class SerialCommond {
    public static byte[] REGISTER = {(byte) 0xCA, 0x05, 0x02, 0x01, 0x00};//注册用户
    public static byte[] FINGERPRINT_MODE = {(byte) 0xCA, 0x11, 0x01, 0x02};//指纹登陆
    public static byte[] SYNCHRONIZE_USER = {(byte) 0xCA, 0x04, 0x04, 0x01};//同步用户数据
    public static byte[] RESET = {(byte) 0xCA, 0x11, 0x01, 0x07};//同步用户数据
    public static byte[] RESET_1 = {(byte) 0xCA, 0x11, 0x01, 0x00};//同步用户数据
    public static byte[] SUCCESS = {(byte) 0xCA, 0x02, 0x01, 0x01};//数据采集成功回复
}
