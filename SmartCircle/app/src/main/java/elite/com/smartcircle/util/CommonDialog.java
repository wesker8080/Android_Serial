package elite.com.smartcircle.util;

import android.app.AlertDialog;
import android.content.Context;

import elite.com.smartcircle.R;

/**
 * @author Wesker
 * @create 2019-01-17 14:46
 */
public class CommonDialog {
    public static void showDialog(Context context, Integer title, int message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ensure, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
    public static void showDialog(Context context, Integer title, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ensure, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
    public static AlertDialog showTipDialog(Context context, Integer title, int message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}
