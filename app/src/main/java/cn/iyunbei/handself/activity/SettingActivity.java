package cn.iyunbei.handself.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yarolegovich.mp.MaterialPreferenceScreen;

import java.util.Arrays;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.iyunbei.handself.R;
public class SettingActivity extends AppCompatActivity {
    @Bind(R.id.iv_left)
    ImageView ivLeft;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.iv_right)
    ImageView ivRight;
    @Bind(R.id.tv_right)
    TextView tvRight;

    ListPreference listPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference_settings);
        ButterKnife.bind(this);
        MaterialPreferenceScreen screen = (MaterialPreferenceScreen) findViewById(R.id.preference_screen);
        screen.setVisibilityController(R.id.pref_time_notice, Arrays.asList(R.id.pref_internal_item), false);


        tvLeft.setVisibility(View.GONE);
        tvTitle.setText("设置");
        tvRight.setVisibility(View.GONE);
        ivRight.setVisibility(View.GONE);

    }

    @OnClick({R.id.iv_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            default:
                break;

        }
    }


}