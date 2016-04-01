package org.ncfrcteams.frcscoutinghub2016.communication.qr;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.ncfrcteams.frcscoutinghub2016.R;


public class QRFragment extends Fragment {
    private static final String QR_TITLE = "org.ncfrcteams.FRCScoutingHub2016.QRFragment.QR_TITLE";
    private static final String QR_TEXT = "org.ncfrcteams.FRCScoutingHub2016.QRFragment.QR_TEXT";
    private String qrTitle;
    private String qrText;
    private int width = 500;
    private int height = 500;
    ImageView qrView;

    public static QRFragment newInstance(String qrTitle, String qrText) {
        QRFragment fragment = new QRFragment();
        Bundle args = new Bundle();
        args.putString(QR_TITLE, qrTitle);
        args.putString(QR_TEXT, qrText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            qrTitle = getArguments().getString(QR_TITLE);
            qrText = getArguments().getString(QR_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root=inflater.inflate(R.layout.h_fragment_qr, container, false);

        qrView = (ImageView) root.findViewById(R.id.qrView);
        TextView titleView = (TextView) root.findViewById(R.id.qrTitle);
        titleView.setText(qrTitle);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrText,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            qrView.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
