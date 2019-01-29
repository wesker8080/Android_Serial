package elite.com.smartcircle.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import android_serialport_api.SerialCommond;
import android_serialport_api.SerialPortUtil;
import elite.com.smartcircle.R;
import elite.com.smartcircle.serialport.HexUtil;
import elite.com.smartcircle.util.AgeOrTallDialogUtil;
import elite.com.smartcircle.util.CommonDialog;
import elite.com.smartcircle.util.FileUtil;

/**
 * @author Wesker
 * @create 2019-01-24 10:45
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private Button btnCancel;
    private Button btnEnsure;
    private Button btnLogin;
    private Button btnLoginF;
    private Button btnRest;
    private Button btnRest1;
    private Button btnTemp;
    private Button btnBlood;
    private Button btnRate;
    private RelativeLayout rlInfo;
    private EditText etName;
    private RadioGroup rgSex;
    private TextView tvChoiceAge;
    private TextView tvChoiceTall;
    private AgeOrTallDialogUtil ageDialog;
    private AgeOrTallDialogUtil tallDialog;
    private String age, tall;
    private SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCache();
        initView();
        initViewController();
        initSerial();
        test();
    }

    private void initCache() {
        mPreferences = getSharedPreferences("elite", Context.MODE_PRIVATE);
    }

    private void test() {
        mSerialPortUtil.sendSerialPort(SerialCommond.RESET);
    }
    private AtomicBoolean mAtomicBoolean = new AtomicBoolean();
    private void initSerial() {
        mSerialPortUtil = new SerialPortUtil();
        mSerialPortUtil.openSerialPort();
        mSerialPortUtil.setOnDataReceiveListener((buffer, size) -> {
            Log.d(TAG, "进入数据监听事件中。。。" + HexUtil.bytesToHexString(buffer));
            String result = HexUtil.bytesToHexString(buffer);
            if (result != null && result.startsWith("ca060101")) {
                // 报告用户成功录入指纹信息
                runOnUiThread(() -> {
                    if (mDialog != null && mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    saveUserInfo();
                });
            } else if (result != null && result.startsWith("ca040101")) {
                Log.d(TAG, "录入成功");
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();

                });

            } else if (result != null && result.startsWith("ca01")) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    if (mLoginDialog != null && mLoginDialog.isShowing()) {
                        mLoginDialog.dismiss();
                    }
                });
            } else if (result != null && result.startsWith("ca02")) {
                if (mLoginDialog != null && mLoginDialog.isShowing()) {
                    mLoginDialog.dismiss();
                }
                // 采集完成
                JSONArray mObjects;
                String mCache = mPreferences.getString("cache", null);
                HashMap<String, String> map;
                if (mCache != null) {
                    mObjects = JSONArray.parseArray(mCache);
                    Log.d("wesker", mObjects.toString());
                } else {
                    mObjects = new JSONArray();
                }
                SharedPreferences.Editor mEdit = mPreferences.edit();
                mSerialPortUtil.sendSerialPort(SerialCommond.SUCCESS);
                List<String> list = mSerialPortUtil.getStrList(result, 2);
                String[] strings = new String[list.size()];
                String[] mArray = list.toArray(strings);
                StringBuilder mBuilder = new StringBuilder();
                String mWeight = getWeight(mArray);
                String mFat = getFat(mArray);
                String mWater = getWater(mArray);
                String mMuscle = getMuscle(mArray);
                String mBMI = getBMI(mArray);
                String mBone = getBone(mArray);
                String mHeightBlood = getHeightBlood(mArray);
                String mLowtBlood = getLowtBlood(mArray);
                String mRate = getRate(mArray);
                String mProtein = getProtein(mArray);
                String mBMR = getBMR(mArray);
                String mVisceralFat = getVisceralFat(mArray);
                String mTemp = getTemp(mArray);
                mBuilder.append(new Date().getTime());
                mBuilder.append("\n");
                mBuilder.append("体重 : ").append(mWeight);
                mBuilder.append("\n");
                mBuilder.append("体脂 : ").append(mFat);
                mBuilder.append("\n");
                mBuilder.append("水分 : ").append(mWater);
                mBuilder.append("\n");
                mBuilder.append("肌肉 : ").append(mMuscle);
                mBuilder.append("\n");
                mBuilder.append("BMI : ").append(mBMI);
                mBuilder.append("\n");
                mBuilder.append("骨量 : ").append(mBone);
                mBuilder.append("\n");
                mBuilder.append("高压 : ").append(mHeightBlood);
                mBuilder.append("\n");
                mBuilder.append("低压 : ").append(mLowtBlood);
                mBuilder.append("\n");
                mBuilder.append("心率 : ").append(mRate);
                mBuilder.append("\n");
                mBuilder.append("蛋白质 : ").append(mProtein);
                mBuilder.append("\n");
                mBuilder.append("BMR : ").append(mBMR);
                mBuilder.append("\n");
                mBuilder.append("内脏脂肪 : ").append(mVisceralFat);
                mBuilder.append("\n");
                mBuilder.append("体温 : ").append(mTemp);
                mObjects.add(mBuilder.toString());
                mEdit.putString("cache", mObjects.toString());
                mEdit.apply();
                runOnUiThread(() -> CommonDialog.showDialog(MainActivity.this, R.string.toast_success,mBuilder.toString()));
            }
            mBuffer = buffer;
        });
    }

    private String getTemp(String[] mArray) {
        long size = Long.parseLong(mArray[31]+mArray[30], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        try {
            mWeight.insert(2,".");
        } catch (Exception mE) {
            Log.e(TAG, "insert error : " + mE.getMessage());
        }
        return mWeight.toString();
    }

    private String getVisceralFat(String[] mArray) {
        long size = Long.parseLong(mArray[29]+mArray[28], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        try {
            mWeight.insert(1,".");
        } catch (Exception mE) {
            Log.e(TAG, "insert error : " + mE.getMessage());
        }
        return mWeight.toString();
    }

    private String getProtein(String[] mArray) {
        long size = Long.parseLong(mArray[25]+mArray[24], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        return mWeight.toString();
    }

    private String getBMR(String[] mArray) {
        long size = Long.parseLong(mArray[27]+mArray[26], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        mWeight.append(" Kcal");
        return mWeight.toString();
    }

    private String getRate(String[] mArray) {
        long size = Long.parseLong(mArray[23]+mArray[22], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        mWeight.append(" 次/分钟");
        return mWeight.toString();
    }

    private String getLowtBlood(String[] mArray) {
        long size = Long.parseLong(mArray[21]+mArray[20], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        mWeight.append(" mmHg");
        return mWeight.toString();
    }

    private String getHeightBlood(String[] mArray) {
        long size = Long.parseLong(mArray[19]+mArray[18], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        mWeight.append(" mmHg");
        return mWeight.toString();
    }

    private String getBone(String[] mArray) {
        long size = Long.parseLong(mArray[17]+mArray[16], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        try {
            mWeight.insert(2,".");
        } catch (Exception mE) {
            Log.e(TAG, "insert error : " + mE.getMessage());
        }
        mWeight.append("%");
        return mWeight.toString();
    }

    private String getBMI(String[] mArray) {
        long size = Long.parseLong(mArray[15]+mArray[14], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        try {
            mWeight.insert(2,".");
        } catch (Exception mE) {
            Log.e(TAG, "insert error : " + mE.getMessage());
        }
        return mWeight.toString();
    }

    private String getMuscle(String[] mArray) {
        long size = Long.parseLong(mArray[13]+mArray[12], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        try {
            mWeight.insert(2,".");
        } catch (Exception mE) {
            Log.e(TAG, "insert error : " + mE.getMessage());
        }
        mWeight.append("%");
        return mWeight.toString();
    }

    private String getWater(String[] mArray) {
        long size = Long.parseLong(mArray[11]+mArray[10], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        try {
            mWeight.insert(2,".");
        } catch (Exception mE) {
            Log.e(TAG, "insert error : " + mE.getMessage());
        }
        mWeight.append("%");
        return mWeight.toString();
    }

    private String getFat(String[] mArray) {
        long size = Long.parseLong(mArray[9]+mArray[8], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        try {
            mWeight.insert(2,".");
        } catch (Exception mE) {
            Log.e(TAG, "insert error : " + mE.getMessage());
        }
        mWeight.append("%");
        return mWeight.toString();
    }

    private void saveUserInfo() {
        int sexHex = 0;
        RadioButton select = findViewById(rgSex.getCheckedRadioButtonId());
        String sex = select.getText().toString();
        if (getResources().getString(R.string.register_sex_man).equals(sex)) {
            sexHex = 0;
        } else {
            sexHex = 1;
        }
        byte[] arr = {(byte) 0xCA, 0x04, 0x04, 0x01, Byte.valueOf(age), Byte.valueOf(tall), (byte) sexHex};
        mSerialPortUtil.sendSerialPort(arr);
        Log.d(TAG, "录入用户信息");

    }

    SerialPortUtil mSerialPortUtil;
    byte[] mBuffer;
    private AlertDialog mDialog;
    private AlertDialog mLoginDialog;
    private final String PATH = "/sys/bus/platform/drivers/hx5200-ctrl/";
    private void initViewController() {
        btnRest1.setOnClickListener(v -> {
            mSerialPortUtil.sendSerialPort(SerialCommond.RESET_1);
        });
        btnRate.setOnClickListener(v -> {
            Toast.makeText(this, R.string.dialog_test_entry, Toast.LENGTH_SHORT).show();
            FileUtil.writeString(new File(PATH + "hx5200_control_state"), false, "0");
            SystemClock.sleep(500);
            FileUtil.writeString(new File(PATH + "hx5200_control_state"), false, "2");
            ScheduledExecutorService mService = Executors.newScheduledThreadPool(1);
            mService.schedule(() -> {
                String rate = FileUtil.readBytesToString(new File("/sys/bus/platform/drivers/hx5200-ctrl/hx5200_heartrate_data"));
                Log.d(TAG, "心率是 :" + rate);
                runOnUiThread(() -> Toast.makeText(this, "心率是 :" + rate, Toast.LENGTH_SHORT).show());
                FileUtil.writeString(new File(PATH + "hx5200_control_state"), false, "0");
            }, 10, TimeUnit.SECONDS);
        });
        btnBlood.setOnClickListener(v -> {
            Toast.makeText(this, R.string.dialog_test_entry, Toast.LENGTH_SHORT).show();
            FileUtil.writeString(new File(PATH + "hx5200_control_state"), false, "0");
            SystemClock.sleep(500);
            FileUtil.writeString(new File(PATH + "hx5200_control_state"), false, "3");
            ScheduledExecutorService mService = Executors.newScheduledThreadPool(1);
            mService.schedule(() -> {
                String blood = FileUtil.readBytesToString(new File("/sys/bus/platform/drivers/hx5200-ctrl/hx5200_bloodoxygen_data"));
                Log.d(TAG, "血压是 :" + blood);
                runOnUiThread(() ->  Toast.makeText(this, "血压是 :" + blood, Toast.LENGTH_SHORT).show());
                FileUtil.writeString(new File(PATH + "hx5200_control_state"), false, "0");
            }, 20, TimeUnit.SECONDS);
        });
        btnTemp.setOnClickListener(v -> {
            String temp = FileUtil.readBytesToString(new File("/sys/bus/i2c/drivers/hx8100/2-0044/hx8100_temp_data"));
            StringBuilder mStringBuilder = new StringBuilder();
            mStringBuilder.append(temp);
            mStringBuilder.insert(2,".");
            Log.d(TAG, "温度是 :" + mStringBuilder.toString());
            Toast.makeText(this, "温度是 :" + mStringBuilder.toString(), Toast.LENGTH_SHORT).show();
        });
        btnRest.setOnClickListener(v -> mSerialPortUtil.sendSerialPort(SerialCommond.RESET));
        btnLoginF.setOnClickListener(v -> {
            mLoginDialog = CommonDialog.showTipDialog(this, null, R.string.dialog_fingerprint_entry);
            mLoginDialog.show();
            mSerialPortUtil.sendSerialPort(SerialCommond.FINGERPRINT_MODE);
            mAtomicBoolean.set(false);
        });
        btnLogin.setOnClickListener(v -> {
            if (rlInfo.getVisibility() == View.GONE) {
                rlInfo.setVisibility(View.VISIBLE);
            }
        });
        btnCancel.setOnClickListener(v -> {
            clearAll();
            rlInfo.setVisibility(View.GONE);
        });
        tvChoiceAge.setOnClickListener(v -> {
            if (ageDialog == null) {
                ageDialog = new AgeOrTallDialogUtil(this, true);
                ageDialog.setSelectedTime(50);
                ageDialog.setOnClickCallBack((a) -> {
                    Log.d(TAG, "age select : " + a);
                    age = a;
                    tvChoiceAge.setText(a);
                });
                ageDialog.show();
            }
            ageDialog.show();
        });
        tvChoiceTall.setOnClickListener(v -> {
            if (tallDialog == null) {
                tallDialog = new AgeOrTallDialogUtil(this, false);
                tallDialog.setSelectedTime(50);
                tallDialog.setOnClickCallBack((a) -> {
                    Log.d(TAG, "tall select : " + a);
                    tall = a;
                    tvChoiceTall.setText(a);
                });
                tallDialog.show();
            }
            tallDialog.show();
        });
        btnEnsure.setOnClickListener(v -> {
            String name = etName.getText().toString();
            if ("".equals(name)) {
                CommonDialog.showDialog(this, null, R.string.dialog_name_null);
                return;
            }
            if (Objects.equals(getResources().getString(R.string.register_age_hint), tvChoiceAge.getText().toString())) {
                CommonDialog.showDialog(this, null, R.string.register_age_hint);
                return;
            }
            if (Objects.equals(getResources().getString(R.string.register_tall_hint), tvChoiceTall.getText().toString())) {
                CommonDialog.showDialog(this, null, R.string.register_tall_hint);
                return;
            }
            RadioButton select = findViewById(rgSex.getCheckedRadioButtonId());
            String sex = select.getText().toString();
            Log.d(TAG, "save info --name : " + name + " sex : " + sex + " age : " + age + " tall : " + tall);
            Toast.makeText(this, R.string.toast_success, Toast.LENGTH_SHORT).show();
            clearAll();
            mDialog = CommonDialog.showTipDialog(this, null, R.string.dialog_fingerprint_entry);
            mDialog.show();
            mSerialPortUtil.sendSerialPort(SerialCommond.REGISTER);
            //Intent mIntent = new Intent(this, InfoActivity.class);
            //startActivity(mIntent);
        });
    }

    private void initView() {
        btnCancel = findViewById(R.id.btn_cancel);
        btnEnsure = findViewById(R.id.btn_ensure);
        btnLogin = findViewById(R.id.btn_login);
        btnLoginF = findViewById(R.id.btn_login_f);
        btnBlood = findViewById(R.id.btn_get_blood);
        btnRate = findViewById(R.id.btn_get_rate);
        btnTemp = findViewById(R.id.btn_get_temp);
        btnRest = findViewById(R.id.btn_rest);
        btnRest1 = findViewById(R.id.btn_rest1);
        rlInfo = findViewById(R.id.rl_info);
        etName = findViewById(R.id.et_name);
        rgSex = findViewById(R.id.rg_sex);
        tvChoiceAge = findViewById(R.id.tv_choice_age);
        tvChoiceTall = findViewById(R.id.tv_choice_tall);
    }
    private void clearAll() {
        etName.setText("");
        tvChoiceAge.setText(R.string.register_age_hint);
        tvChoiceTall.setText(R.string.register_tall_hint);
    }
    private String getWeight(String[] s) {
        long size = Long.parseLong(s[7]+s[6], 16);
        StringBuilder mWeight = new StringBuilder();
        mWeight.append(size);
        mWeight.insert(2,".");
        return mWeight.toString();
    }

}
