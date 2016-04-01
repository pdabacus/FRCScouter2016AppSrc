package org.ncfrcteams.frcscoutinghub2016.communication.sms_server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by Kyle Brown on 3/18/2016.
 */
public class SmsReceiver extends BroadcastReceiver {
    private Context context;
    private SmsListener smsListener = null;
    private IntentFilter smsFilter;

    /**
     * Creates an inactive SmsReceiver object
     * @param context the context to which this receiver is registered and unregistered
     */
    public SmsReceiver(Context context) {
        this.context = context;
        smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    }

    /**
     * Assigns an SMSListener to the SmsReceiver. This will replace prior SMSListeners
     * @param smsListener
     */
    public void setSmsListener(SmsListener smsListener) {
        this.smsListener = smsListener;
    }

    /**
     * Registers this SmsReceiver so that it will start receiving SMS messages
     */
    public void register() {
        context.registerReceiver(this,smsFilter);
    }

    /**
     * Registers this SmsReceiver so that it will stop receiving SMS messages
     */
    public void unregister() {
        context.unregisterReceiver(this);
    }

    /**
     * This method is called whenever this BroadcastReceiver is notified of a broadcast that matches
     * the smsFilter. For every message received notify the smsListener of the number and message.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String number;
        String message;

        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                if(pdusObj == null)
                    return;

                for (Object pdu : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdu);

                    number = currentMessage.getDisplayOriginatingAddress();
                    message = currentMessage.getDisplayMessageBody();

                    if(smsListener != null)
                        smsListener.smsReceived(number,message);

                    Log.i("SmsReceiver", "senderNum: " + number + "; message: " + message);
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

    public interface SmsListener {
        void smsReceived(String number, String message);
    }
}
