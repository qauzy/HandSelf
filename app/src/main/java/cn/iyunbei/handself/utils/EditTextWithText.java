package cn.iyunbei.handself.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatEditText;


public class EditTextWithText extends AppCompatEditText {
    private String leadText = null;
    public EditTextWithText(Context context) {
        super(context);
        onlyNum();
    }

    public EditTextWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        onlyNum();
    }

    public EditTextWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onlyNum();
    }
    private  void onlyNum(){
        //允许输入小数
        this.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //限制输入的位数
        this.addTextChangedListener(new OnlyOneEditText(this).setInputNum(2));
    }

    public float sp2px(Context context, float spValue) {
        //fontScale （DisplayMetrics类中属性scaledDensity）
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (spValue * fontScale + 0.5f);
    }


    public class OnlyOneEditText implements TextWatcher {

        private AppCompatEditText editText;
        private int inputNum = 2;

        public OnlyOneEditText(AppCompatEditText et) {
            editText = et;
        }
        public OnlyOneEditText setInputNum(int d) {
            inputNum = d;
            return this;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().contains(".")) {
                //举例3.67  4-1-1=2>1  s=3.67
                if (s.length() - 1 - s.toString().indexOf(".") > inputNum) {
                    //subSequence(0,3)   包括第0位不包括第3位  所以就是3.6
                    s = s.toString().subSequence(0,
                            s.toString().indexOf(".") + inputNum +1);
                    editText.setText(s);
                    editText.setSelection(s.length()); //光标移到最后
                }
            }
            //如果"."在起始位置,则起始位置自动补0
            if (s.toString().trim().equals(".")) {
                s = "0" + s;
                editText.setText(s);
                editText.setSelection(2);
            }

            //如果起始位置为0,且第二位跟的不是".",则无法后续输入
            if (s.toString().startsWith("0")
                    && s.toString().trim().length() > 1) {
                if (!s.toString().substring(1, 2).equals(".")) {
                    editText.setText(s.subSequence(0, 1));
                    editText.setSelection(1);
                    return;
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(sp2px(this.getContext(),14));//自定义字大小
        paint.setColor(Color.RED);//自定义字颜色
        canvas.drawText(getLeadText()+" ",2,getHeight()/2+10,paint);
        int paddingLeft = (int) paint.measureText(getLeadText());
        //设置距离光标距离左侧的距离
        setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        super.onDraw(canvas);
    }
    public void setLeadText(String s){
        leadText = s;
    }
    private String getLeadText(){
        return leadText;
    }
}


