package elite.com.smartcircle.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import elite.com.smartcircle.R;
import elite.com.smartcircle.customview.WheelView;

/**
 * @author Wesker
 * @create 2019-01-24 13:54
 */
public class AgeOrTallDialogUtil implements View.OnClickListener {


    private Context mContext;

    private Dialog dialog;

    private onClickCallBack onClickCallBack = null;
    private TextView tvEnsure;
    private TextView tvCancel;
    private WheelView wheelView;
    private boolean isAge;

    private List<String> tallList = new ArrayList<>();
    private List<String> ageList = new ArrayList<>();

    public AgeOrTallDialogUtil(Context context, boolean isAge) {
        mContext = context;
        this.isAge = isAge;
        init();

    }


    private void init() {
        for (int i = 0; i < 99; i++) {
            if (i < 10) {
                ageList.add("0" + i);
            } else {
                ageList.add(i + "");
            }
        }
        for (int i = 50; i < 200; i++) {
            tallList.add(i + "");
        }

        dialog = new Dialog(mContext, R.style.translucentDialogStyle);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_time_set, null, false);

        tvEnsure = (TextView) view.findViewById(R.id.tv_ensure);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvEnsure.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        wheelView = (WheelView) view.findViewById(R.id.wv_age_or_tall);

        wheelView.setOffset(4);
        wheelView.setSelectColor(ContextCompat.getColor(mContext, R.color.black));
        if (isAge) {
            wheelView.setItems(ageList);
        } else {
            wheelView.setItems(tallList);
        }
        wheelView.setSeletion(50);



        int w = (int) (DensityUtils.getW(mContext) / 1.2);
        dialog.setContentView(view, new LinearLayout.LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialog.setCanceledOnTouchOutside(true);
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                if(i == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0){
//                    return true;
//                }
//                return false;
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        dialog.dismiss();
        switch (view.getId()) {
            case R.id.tv_cancel:
                disMiss();
                break;
            case R.id.tv_ensure:
                disMiss();
                if (onClickCallBack != null) {
                    String hour = wheelView.getSeletedItem();
                    onClickCallBack.onClick(hour);
                }
                break;
            default:break;
        }

    }


    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }


    public void setSelectedTime(int selected) {
        try {
            wheelView.setSeletion(selected);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disMiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public interface onClickCallBack {
        void onClick(String time);
    }

    public void setOnClickCallBack(AgeOrTallDialogUtil.onClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }

}
