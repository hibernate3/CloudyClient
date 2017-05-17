package com.example.cloudyclient.activity.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.cloudyclient.MainApplication;
import com.example.cloudyclient.R;
import com.example.cloudyclient.activity.PicWallActivity;
import com.example.cloudyclient.model.bean.PicEntity;
import com.example.cloudyclient.model.biz.PicEntityDBManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wangyuhang on 17-5-16.
 */

public class PicSearchDialog extends DialogFragment {
    @BindView(R.id.aperture_et)
    EditText apertureEt;
    @BindView(R.id.exposure_et)
    EditText exposureEt;
    @BindView(R.id.exposure_switch)
    Switch exposureSwitch;
    @BindView(R.id.iso_et)
    EditText isoEt;
    @BindView(R.id.search_btn)
    Button searchBtn;

    Unbinder unbinder;

    private static final int DECIMAL_DIGITS_1 = 1;//小数的位数
    private static final int DECIMAL_DIGITS_2 = 2;//小数的位数

    private PicWallActivity.PicWallAdapter mAdapter;
    private FloatingActionButton mFab;

    public PicSearchDialog(FloatingActionButton fab, PicWallActivity.PicWallAdapter adapter) {
        this.mFab = fab;
        this.mAdapter = adapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pic_search, null);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        apertureEt.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                       int dend) {
                // source:当前输入的字符
                // start:输入字符的开始位置
                // end:输入字符的结束位置
                // dest：当前已显示的内容
                // dstart:当前光标开始位置
                // dent:当前光标结束位置
                if (dest.length() == 0 && source.equals(".")) {
                    return "0.";
                }

                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    if (dotValue.length() == DECIMAL_DIGITS_1) {
                        return "";
                    }
                } else {
                    //确保整数部分不能超过2位
                    if (dest.length() == 2 && !dest.toString().contains(".") && !source.equals(".")) {
                        return "";
                    }
                }
                return null;
            }
        }});

        exposureEt.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                       int dend) {
                // source:当前输入的字符
                // start:输入字符的开始位置
                // end:输入字符的结束位置
                // dest：当前已显示的内容
                // dstart:当前光标开始位置
                // dent:当前光标结束位置
                if (dest.length() == 0 && source.equals(".")) {
                    return "0.";
                }

                String dValue = dest.toString();
                String[] splitArray = dValue.split("\\.");
                if (splitArray.length > 1) {
                    String dotValue = splitArray[1];
                    if (dotValue.length() == DECIMAL_DIGITS_1) {
                        return "";
                    }
                } else {
                    //确保整数部分不能超过4位
                    if (dest.length() == 4 && !dest.toString().contains(".") && !source.equals(".")) {
                        return "";
                    }
                }
                return null;
            }
        }});

        isoEt.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                       int dend) {
                // source:当前输入的字符
                // start:输入字符的开始位置
                // end:输入字符的结束位置
                // dest：当前已显示的内容
                // dstart:当前光标开始位置
                // dent:当前光标结束位置
                if (dest.length() == 0 && source.equals("0")) {
                    return "";
                } else if (dest.length() == 5) {
                    return "";
                }
                return null;
            }
        }});

        exposureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    String exposure = exposureEt.getText().toString();
                    if (exposure.contains(".")) {
                        exposureSwitch.setChecked(false);
                        Snackbar.make(exposureSwitch, "含有小数数值，无法倒置", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }
            }
        });

        exposureEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().contains(".")) {
                    exposureSwitch.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.search_btn)
    public void onViewClicked() {
        String aperture = apertureEt.getText().toString();
        String exposure = exposureEt.getText().toString();
        String iso = isoEt.getText().toString();

        if (TextUtils.isEmpty(aperture) && TextUtils.isEmpty(exposure) && TextUtils.isEmpty(iso)) {
            Snackbar.make(searchBtn, "请至少填写一项参数", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            return;
        }

        DecimalFormat df = new DecimalFormat("0.0");
        if (!TextUtils.isEmpty(aperture)) {
            aperture = df.format(Double.parseDouble(aperture));
        }

        if (!TextUtils.isEmpty(exposure)) {
            exposure = exposureSwitch.isChecked() ?
                    "1/" + df.format(Double.parseDouble(exposure)) : df.format(Double.parseDouble(exposure));
        }

        List<PicEntity> result = PicEntityDBManager.getInstance().query(null, aperture, iso, exposure, null);

        List<String> data = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            data.add(result.get(i).getFileName());
        }

        PicWallActivity.isFiltered = true;
        mFab.setImageResource(android.R.drawable.ic_dialog_dialer);
        mAdapter.setData(data);

        dismiss();

        StringBuffer sb = new StringBuffer("检索到" + data.size() + "张照片");
        sb.append(TextUtils.isEmpty(aperture) ? "" : "\r\r|\r光圈:" + aperture);
        sb.append(TextUtils.isEmpty(exposure) ? "" : "\r\r快门:" + exposure);
        sb.append(TextUtils.isEmpty(iso) ? "" : "\r\rISO:" + iso);
        Snackbar.make(mFab, sb.toString(), Snackbar.LENGTH_INDEFINITE).show();
    }
}

