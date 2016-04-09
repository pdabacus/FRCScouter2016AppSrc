package org.ncfrcteams.frcscoutinghub2016.communication.http;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.ncfrcteams.frcscoutinghub2016.R;

/**
 * Created by pavan on 4/9/16.
 */
public class SignInDialog {


    private static final String POSITIVE_TEXT = "Sign In";
    private static final String NEGATIVE_TEXT = "Cancel";
    private static final String GETAUTH = "manage/auth/index.php";
    private Dialog dialog;
    private Context context;
    private View view;
    private SignInDialogListener listener;

    public SignInDialog(final Context context, final SignInDialogListener listener, String user, String pass, String url) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        final SignInDialog thisDialog = this;

        thisDialog.context = context;
        thisDialog.view = LayoutInflater.from(context).inflate(R.layout.http_dialog_signin, null);
        thisDialog.listener = listener;

        alert.setView(view);

        ((EditText) view.findViewById(R.id.user)).setText(user);
        ((EditText) view.findViewById(R.id.pass)).setText(pass);
        ((EditText) view.findViewById(R.id.url)).setText(url);

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                final String user = ((EditText) view.findViewById(R.id.user)).getText().toString();
                final String pass = ((EditText) view.findViewById(R.id.pass)).getText().toString();
                final String url = ((EditText) view.findViewById(R.id.url)).getText().toString();

                String[][] P = {{"user", user}, {"pass", pass}, {"type", "auth"}};
                String[][] F = { {"NONONO", "NONONO"} };

                new POST(context, url + GETAUTH, P, F, false, new POST.POSTListener() {
                    @Override
                    public void POSTResult(String returnAddress, String result) {
                        thisDialog.listener.onSignIn(user, pass, url);
                        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                    }
                }, "download").execute();
            }
        };

        final DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //dismiss
            }
        };

        alert.setPositiveButton(POSITIVE_TEXT, positiveListener);
        alert.setNegativeButton(NEGATIVE_TEXT, negativeListener);

        dialog = alert.create();
    }

    public void show() {
        dialog.show();
    }

    public interface SignInDialogListener{
        void onSignIn(String user, String pass, String url);
    }


}
