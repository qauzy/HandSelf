package cn.iyunbei.handself.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
import cn.iyunbei.handself.contract.LoginContract;
import cn.iyunbei.handself.presenter.LoginPresenter;
import jt.kundream.base.BaseActivity;
import jt.kundream.utils.ActivityUtil;
import jt.kundream.utils.AndroidBug5497Workaround;
import jt.kundream.utils.CommonUtil;

/**
 * 版权所有，违法必究！！！
 *
 * @Company: NanYangYunBeiTeac
 * @Copyright： ©2017-2018
 * @author: YangTiankun created on 2018/3/19/019 10
 * @e-mail: 245086168@qq.com
 * @desc:登陆界面
 **/
public class LoginActivity extends BaseActivity<LoginContract.View, LoginPresenter> implements LoginContract.View, TextView.OnEditorActionListener {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.et_pswd)
    EditText etPswd;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        etPhone.setOnEditorActionListener(this);
        etPswd.setOnEditorActionListener(this);
        presenter.checkToken(this);
    }

    @Override
    public LoginPresenter initPresenter() {
        return new LoginPresenter();
    }

    @OnClick({R.id.iv_back, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.btn_login:
                presenter.login(etPhone.getText().toString(), etPswd.getText().toString());
                break;
        }
    }

    @Override
    public void loginSucc(String token) {
        //将token保存在本地
        CommonUtil.put(this, "token", token);
        ActivityUtil.startActivity(this, MainActivity.class, true);
    }

    @Override
    public void skipLogin() {
        ActivityUtil.startActivity(this, MainActivity.class, true);
    }


    /**
     * 弹出的软键盘的右下角的点击监听
     */
    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        doWhichOperation(actionId);

        return true;
    }

    private void doWhichOperation(int actionId) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_DONE:
                Log.e("BALLACK", "IME_ACTION_DONE");
                break;
            case EditorInfo.IME_ACTION_GO:
                Log.e("BALLACK", "IME_ACTION_GO");
                break;
            case EditorInfo.IME_ACTION_NEXT:
                //这个是我所需要的
                Log.e("BALLACK", "IME_ACTION_NEXT");
                break;
            case EditorInfo.IME_ACTION_NONE:
                Log.e("BALLACK", "IME_ACTION_NONE");
                break;
            case EditorInfo.IME_ACTION_PREVIOUS:
                Log.e("BALLACK", "IME_ACTION_PREVIOUS");
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                Log.e("BALLACK", "IME_ACTION_SEARCH");
                break;
            case EditorInfo.IME_ACTION_SEND:
                Log.e("BALLACK", "IME_ACTION_SEND");
                break;
            case EditorInfo.IME_ACTION_UNSPECIFIED:
                Log.e("BALLACK", "IME_ACTION_UNSPECIFIED");
                break;
            default:
                break;
        }
    }
}
