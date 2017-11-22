package com.meiyou.androidskin.databinding;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.meiyou.androidskin.R;
import com.meiyou.androidskin.databinding.ActivityDatabindingBinding;

/**
 * Author: ice
 * Date: 17/11/21 14:31.
 */

public class DatabindingActivity extends Activity {

    private ActivityDatabindingBinding mActivityDatabindingBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mActivityDatabindingBinding = DataBindingUtil.setContentView(
                this, R.layout.activity_databinding);

        User user = new User("lin", "暂时不支持Databinding换肤哦");
        user.setCheck(true);
        mActivityDatabindingBinding.setUser(user);
    }

    public void onTextViewChange(View view){
        mActivityDatabindingBinding.getUser().setCheck(!mActivityDatabindingBinding.getUser().isCheck());
        mActivityDatabindingBinding.setUser(mActivityDatabindingBinding.getUser());
    }
}
