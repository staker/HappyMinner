package com.ailink.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;

/**
 * 这个按钮主要的功能是，自动检测绑定的EditText控件，当绑定进来的控件没有输入值时，那么控件
 * 为不可用状态，只有当绑定进来的所有EditText控件都有输入字符串时，这个SubmitButton的状态才会是enable为true
 */
public class SubmitButton extends Button {

    private MyTextWatcher mMyTextWatcher;
    private EditText[] editText;

    public SubmitButton(Context context) {
        super(context);
    }

    public SubmitButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubmitButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMyTextWatcher = new MyTextWatcher();
    }

    public void setEditTextViews(EditText... editText){
        if(mMyTextWatcher == null) mMyTextWatcher = new MyTextWatcher();
        this.editText = editText;
        for (EditText v:editText){
            v.addTextChangedListener(mMyTextWatcher);
        }
    }

    private class MyTextWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            for (EditText v:editText){
                String str = v.getText().toString();
                if(TextUtils.isEmpty(str)){
                    setEnabled(false);
                    return;
                }
            }
            setEnabled(true);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
