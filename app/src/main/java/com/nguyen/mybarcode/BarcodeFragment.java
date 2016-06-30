package com.nguyen.mybarcode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by my.nguyen on 6/30/16.
 */
public class BarcodeFragment extends DialogFragment {
    // empty constructor
    public BarcodeFragment() {
    }

    public static BarcodeFragment newInstance(String text, BarcodeFormat format) {
        BarcodeFragment fragment = new BarcodeFragment();
        Bundle args = new Bundle();
        args.putString("text", text);
        args.putSerializable("format", format);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_barcode, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView barcodeImage = (ImageView)view.findViewById(R.id.barcode_image);
        TextView barcodeNumber = (TextView)view.findViewById(R.id.barcode_number);
        int width = 0;
        int height = 0;
        Bundle args = getArguments();
        String text = args.getString("text");
        BarcodeFormat format = (BarcodeFormat)args.getSerializable("format");
        switch (format) {
            case CODE_128:
                width = getResources().getDimensionPixelSize(R.dimen.barcode1d_width);
                height = getResources().getDimensionPixelSize(R.dimen.barcode1d_height);
                break;
            case QR_CODE:
                width = getResources().getDimensionPixelSize(R.dimen.barcode2d_width);
                height = getResources().getDimensionPixelSize(R.dimen.barcode2d_height);
                break;
        }
        // barcodeImage.requestLayout();
        barcodeImage.getLayoutParams().width = width;
        barcodeImage.getLayoutParams().height = height;
        try {
            Bitmap bitmap = encodeAsBitmap(text, format, width, height);
            barcodeImage.setImageBitmap(bitmap);
            /*
            // hide the soft keyboard
            InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            */
        } catch (WriterException e) {
            e.printStackTrace();
        }
        barcodeNumber.setText(text);
    }

    Bitmap encodeAsBitmap(String str, BarcodeFormat format, int width, int height) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 0);
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(str, format, width, height, hints);
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
