package org.ncfrcteams.frcscoutinghub2016.communication.qr;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import org.ncfrcteams.frcscoutinghub2016.R;

/**
 * Created by Kyle Brown on 3/20/2016.
 */
public class MultiQRDialog {
    private Dialog dialog;

    public MultiQRDialog(Context context, String[] contents) {
        ViewPager view = new ViewPager(context) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

                int height = 0;
                for(int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    if(h > height) height = h;
                }

                heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };

        //ViewPager viewPager = view;
        //viewPager.setAdapter(new QRPagerAdapter(context,contents));
        view.setAdapter(new QRPagerAdapter(context,contents));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        };

        //***********************************Making The Dialog*****************************************
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(view);
        //alert.setTitle("QR Codes");
        alert.setPositiveButton("Done", listener);
        dialog = alert.create();
    }

    class QRPagerAdapter extends PagerAdapter {

        private Context context;
        private String[] contents;

        public QRPagerAdapter(Context context, String[] contents) {
            this.context = context;
            this.contents = contents;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.qr_dialog_view_template, collection, false);

            ImageView qrImageView = (ImageView) layout.findViewById(R.id.qrImageView);
            TextView qrTitle = (TextView) layout.findViewById(R.id.qrTitle);

            qrTitle.setText(getPageTitle(position));

            QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(
                    contents[position],
                    null,
                    Contents.Type.TEXT,
                    BarcodeFormat.QR_CODE.toString(),
                    500);

            try {
                Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
                qrImageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return contents.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "QR Code " + (position + 1) + "/" + getCount();
        }
    }

    public void show() {
        dialog.show();
    }
}
