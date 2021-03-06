package com.myplas.l.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huangbrayant.citypicker.utils.StatusUtils;
import com.myplas.l.main.MainActivity;
import com.myplas.l.R;
import com.myplas.l.base.BaseActivity;
import com.myplas.l.base.BasePresenter;
import com.myplas.l.common.utils.KeyboardHelper;
import com.myplas.l.common.utils.VerifyCodeUtils;
import com.myplas.l.common.widgets.MyEditText;

/**
 * @author Huangshuang  2018/3/9 0009
 */

public class RegisterActivity extends BaseActivity implements VerifyCodeUtils.CountListener
        , MyEditText.OnTextWatcher {

    private LinearLayout root;
    private Button btnContinue, btnSkip;
    private TextView tvPhoneCode, tvAgreenmnet;
    private MyEditText editPhone, editPassWord, editPhoneCode;

    private String phone, passWord, phoneCode;

    private VerifyCodeUtils utils;
    private KeyboardHelper mKeyboardHelper;

    private boolean selected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusUtils.setStatusBar(this, false, false);
        StatusUtils.setStatusTextColor(true, this);

        setContentView(R.layout.activity_register);

        titleBarBackground(R.color.defaultWhite)
                .leftImageRes(R.drawable.ic_arrow_left_black)
                .title("注 册")
                .titleColor(R.color.black);
    }

    @Override
    public void initView() {
        root = findViewById(R.id.root);
        btnSkip = findViewById(R.id.btn_skip);
        editPhone = findViewById(R.id.edit_phone);
        btnContinue = findViewById(R.id.btn_continue);
        tvPhoneCode = findViewById(R.id.tv_phone_code);
        editPassWord = findViewById(R.id.edit_password);
        editPhoneCode = findViewById(R.id.edit_phone_code);
        tvAgreenmnet = findViewById(R.id.tv_register_agreement);

        showInPutKeybord(editPhone);
        utils = new VerifyCodeUtils(this, this);

        mKeyboardHelper = new KeyboardHelper(this, root);
        mKeyboardHelper.enable();
    }

    @Override
    public void setListener() {
        super.setListener();
        btnSkip.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        tvPhoneCode.setOnClickListener(this);
        tvAgreenmnet.setOnClickListener(this);

        editPhone.addOnTextWatcher(this);
        editPassWord.addOnTextWatcher(this);
        editPhoneCode.addOnTextWatcher(this);
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_continue:
                startActivity(new Intent(this, CompletePersonInfoActivity.class));
                break;
            case R.id.btn_skip:
                com.myplas.l.base.ActivityManager.finishAllActivity();
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.tv_register_agreement:
                selected = !selected;
                tvAgreenmnet.setCompoundDrawablesWithIntrinsicBounds(selected
                        ? R.drawable.ic_register_btn_checkbox
                        : R.drawable.ic_register_btn_checkbox_hl, 0, 0, 0);
                break;
            case R.id.tv_phone_code:
                utils.startCount();
                break;
            default:
                break;
        }
    }


    @Override
    public void onTextChanged(View v, String s) {
        changeBtnColor();
    }

    private void changeBtnColor() {
        if (isEmpty()) {
            btnContinue.setBackgroundResource(R.drawable.shape_login_orange);
            btnSkip.setBackgroundResource(R.drawable.shape_login_blue);
        } else {
            btnContinue.setBackgroundResource(R.drawable.shape_login_orange_alpha);
            btnSkip.setBackgroundResource(R.drawable.shape_login_blue_alpha);
        }
    }

    private boolean isEmpty() {
        phone = editPhone.getText().toString();
        passWord = editPassWord.getText().toString();
        phoneCode = editPhoneCode.getText().toString();
        return !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(passWord) && !TextUtils.isEmpty(phoneCode);
    }

    @Override
    public void count(Activity activity, String count) {

        tvPhoneCode.setText(count + "秒后重试");
        tvPhoneCode.setClickable(false);
        if ("0".equals(count)) {
            tvPhoneCode.setText("重新发送");
            tvPhoneCode.setClickable(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        utils.setStop(true);
        mKeyboardHelper.disable();
    }
}
