package com.fast.charge;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BatterDetailsAdapter extends ArrayAdapter<String> {
    private final Activity f6254a;
    private final String[] f6255b;
    private final String[] f6256c;
    private final Integer[] f6257d;

    public BatterDetailsAdapter(Activity activity, String[] strArr, String[] strArr2, Integer[] numArr) {
        super(activity, R.layout.detail_list_row, strArr);
        this.f6254a = activity;
        this.f6255b = strArr;
        this.f6257d = numArr;
        this.f6256c = strArr2;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View inflate = this.f6254a.getLayoutInflater().inflate(R.layout.detail_list_row, null, true);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.detailImageView);
        TextView textView = (TextView) inflate.findViewById(R.id.text_detail_value);
        CardView cardView = (CardView) inflate.findViewById(R.id.about_1);
        ((TextView) inflate.findViewById(R.id.text_detail_name)).setText(this.f6255b[i]);
        textView.setText(this.f6256c[i]);
        imageView.setImageResource(this.f6257d[i].intValue());
        cardView.setCardBackgroundColor(this.f6254a.getResources().getColor(R.color.cardcolor));
        cardView.setCardElevation(0.0f);
        return inflate;
    }
}