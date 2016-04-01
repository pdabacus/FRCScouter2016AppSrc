package org.ncfrcteams.frcscoutinghub2016.communication.sms_server;

import android.telephony.SmsManager;

/**
 * Created by Kyle Brown on 3/17/2016.
 */
public class SendSms {
    public static void send(String number, String message, String comment, String unique) {
        SmsManager smsManager = SmsManager.getDefault();
        unique = "<frc" + unique;
        smsManager.sendTextMessage(number, null, unique + ",mr>" + message, null, null);
        smsManager.sendTextMessage(number, null, unique + ",cm>" + comment, null, null);
    }
}
