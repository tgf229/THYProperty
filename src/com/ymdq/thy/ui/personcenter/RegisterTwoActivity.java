package com.ymdq.thy.ui.personcenter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.personcenter.BindingHouseResponse;
import com.ymdq.thy.bean.personcenter.HouseCodeResponse;
import com.ymdq.thy.bean.personcenter.HouseDList;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.constant.Global;
import com.ymdq.thy.constant.URLUtil;
import com.ymdq.thy.network.ConnectService;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.util.GeneralUtils;
import com.ymdq.thy.util.NetLoadingDailog;
import com.ymdq.thy.util.ToastUtil;

/**
 * 注册第二步,绑定房屋
 * <功能详细描述>
 * 
 * @author  user
 * @version  [版本号, 2014-11-20]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RegisterTwoActivity extends BaseActivity
{
    /**
     * 头部
     */
    private LinearLayout llBack;
    
    private TextView tvTitle;
    
    /**
     * 房屋信息
     */
    private HouseDList houseDList;
    
    private RadioGroup owerRG;
    
    /**
     * 身份证信息
     */
    private TextView tvIdCardLeft, tvIdCardOne, tvIdCardTwo, tvIdCardThree, tvIdCardFour, tvIdCardFive, tvIdCardSix;
    
    /**
     * 手机号
     */
    private TextView tvPhone;
    
    private String phoneNum;
    
    /**
     * 验证码
     */
    private EditText etCode;
    
    private Button btCode;
    
    private Button btNext;
    
    private View phoneView, idCardView;
    
    /**
     * 记录身份信息,默认住户
     */
    private String role = Constants.ROLE_TENEMENT;
    
    private KeyboardView keyboardView;
    
    /**
     * 填写的身份证号
     */
    private String cardNum = "";
    
    /**
     * 网络请求框
     */
    private NetLoadingDailog dailog;
    
    /**
     * 倒计时
     */
    private MyTime myTime;
    
    private String bindHouseCenter;
    
    private String register_cid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_two);
        initTitle();
        setKeyboard();
        init();
        addListener();
    }
    
    /**
     * 初始化头部
     */
    private void initTitle()
    {
        bindHouseCenter = getIntent().getStringExtra("center_binding_house");
        llBack = (LinearLayout)findViewById(R.id.title_back_layout);
        tvTitle = (TextView)findViewById(R.id.title_name);
        tvTitle.setTypeface(GeneralUtils.getFontFace(this));
        
        if ("center_binding_house".equals(bindHouseCenter))
        {
            tvTitle.setText("绑定房屋");
        }
        else
        {
            register_cid = getIntent().getStringExtra("register_cid");
            tvTitle.setText("注册");
        }
        
        llBack.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                RegisterTwoActivity.this.finish();
            }
        });
    }
    
    private void init()
    {
        
        houseDList = (HouseDList)getIntent().getSerializableExtra("register_two_building_info");
        owerRG = (RadioGroup)findViewById(R.id.register_two_radiogroup);
        tvIdCardLeft = (TextView)findViewById(R.id.register_two_idcard_left);
        tvIdCardLeft.setText(houseDList.getIdCard());
        tvIdCardOne = (TextView)findViewById(R.id.register_two_idcard_right_one);
        tvIdCardTwo = (TextView)findViewById(R.id.register_two_idcard_right_two);
        tvIdCardThree = (TextView)findViewById(R.id.register_two_idcard_right_three);
        tvIdCardFour = (TextView)findViewById(R.id.register_two_idcard_right_four);
        tvIdCardFive = (TextView)findViewById(R.id.register_two_idcard_right_five);
        tvIdCardSix = (TextView)findViewById(R.id.register_two_idcard_right_six);
        tvPhone = (TextView)findViewById(R.id.register_two_phone_tv);
        
        phoneNum = houseDList.getPhone();
        if (GeneralUtils.isNotNullOrZeroLenght(phoneNum) && phoneNum.length() == 11)
        {
            tvPhone.setText(phoneNum.substring(0, 4) + "*****"
                + phoneNum.substring(phoneNum.length() - 2, phoneNum.length()));
        }
        else
        {
            tvPhone.setText("");
        }
        
        etCode = (EditText)findViewById(R.id.register_two_code_left_et);
        btCode = (Button)findViewById(R.id.register_two_code_right_bt);
        btNext = (Button)findViewById(R.id.register_two_sumbit_bt);
        
        etCode.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                hideKeyboard();
            }
        });
        
        etCode.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    hideKeyboard();
                }
            }
        });
        
        if ("center_binding_house".equals(bindHouseCenter))
        {
            btNext.setText("完成");
        }
        
        phoneView = findViewById(R.id.register_two_phone_num);
        idCardView = findViewById(R.id.register_two_idcard_view);
        
        dailog = new NetLoadingDailog(this);
        
        int width = getResources().getDrawable(R.drawable.register_two_idcard_right).getIntrinsicWidth() / 6;
        int height = getResources().getDrawable(R.drawable.register_two_idcard_right).getIntrinsicHeight();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.width = width;
        layoutParams.height = height;
        tvIdCardOne.setLayoutParams(layoutParams);
        tvIdCardTwo.setLayoutParams(layoutParams);
        tvIdCardThree.setLayoutParams(layoutParams);
        tvIdCardFour.setLayoutParams(layoutParams);
        tvIdCardFive.setLayoutParams(layoutParams);
        tvIdCardSix.setLayoutParams(layoutParams);
    }
    
    private void addListener()
    {
        btNext.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                //检测身份证号
                if (GeneralUtils.isNotNullOrZeroLenght(cardNum) && cardNum.length() == 6)
                {
                    if (role.equals(Constants.ROLE_OWNER))
                    {
                        //判断是否输入验证码
                        String code = etCode.getText().toString().trim();
                        if (GeneralUtils.isNotNullOrZeroLenght(code))
                        {
                            bindingHouse();
                        }
                        else
                        {
                            ToastUtil.makeText(RegisterTwoActivity.this, "请输入验证码");
                        }
                    }
                    else
                    {
                        bindingHouse();
                    }
                    
                }
                else
                {
                    ToastUtil.makeText(RegisterTwoActivity.this, "请输入业主身份证号码后六位");
                }
            }
        });
        
        btCode.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                getCode(phoneNum);
            }
        });
        
        owerRG.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.register_two_radio_tenement:
                        role = Constants.ROLE_TENEMENT;
                        phoneView.setVisibility(View.GONE);
                        break;
                    
                    case R.id.register_two_radio_owner:
                        role = Constants.ROLE_OWNER;
                        phoneView.setVisibility(View.VISIBLE);
                        break;
                    
                    default:
                        break;
                }
            }
        });
        
        /**
         * 弹出软键盘
         */
        idCardView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                
                InputMethodManager inputMethodManager =
                    (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etCode.getWindowToken(), 0);
                
                showKeyboard();
                
            }
        });
    }
    
    /**
     * 
     * <获取验证码>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void getCode(String phone)
    {
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("phone", phone);
        param.put("type", Constants.CODE_GET_HOUSE);
        ConnectService.instance().connectServiceReturnResponse(RegisterTwoActivity.this,
            param,
            RegisterTwoActivity.this,
            HouseCodeResponse.class,
            URLUtil.HOUSE_CODE,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 
     * <绑定房屋>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void bindingHouse()
    {
        dailog.loading();
        Map<String, String> param = new HashMap<String, String>();
        param.put("uId", Global.getUserId());
        param.put("hId", houseDList.gethId());
        if (role.equals(Constants.ROLE_OWNER))
        {
            String code = etCode.getText().toString().trim();
            param.put("type", "1");
            param.put("phone", houseDList.getPhone());
            param.put("msgCode", code);
        }
        else
        {
            param.put("type", "2");
        }
        param.put("idCard", cardNum);
        
        ConnectService.instance().connectServiceReturnResponse(RegisterTwoActivity.this,
            param,
            RegisterTwoActivity.this,
            BindingHouseResponse.class,
            URLUtil.BINDING_HOUSE,
            Constants.ENCRYPT_NONE);
    }
    
    /**
     * 初始化软键盘
     * <一句话功能简述>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void setKeyboard()
    {
        keyboardView = (KeyboardView)findViewById(R.id.register_two_keyboard_view);
        keyboardView.setKeyboard(new Keyboard(this, R.layout.qwerty));
        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(new OnKeyboardActionListener()
        {
            @Override
            public void onKey(int primaryCode, int[] keyCodes)
            {
                switch (primaryCode)
                {
                    case 49:
                        addNum("1");
                        break;
                    case 50:
                        addNum("2");
                        break;
                    case 51:
                        addNum("3");
                        break;
                    case 52:
                        addNum("4");
                        break;
                    case 53:
                        addNum("5");
                        break;
                    case 54:
                        addNum("6");
                        break;
                    case 55:
                        addNum("7");
                        break;
                    case 56:
                        addNum("8");
                        break;
                    case 57:
                        addNum("9");
                        break;
                    case 48:
                        addNum("0");
                        break;
                    case 46:
                        addNum("X");
                        break;
                    case 45://关键盘
                        hideKeyboard();
                        break;
                    case 44://删除
                        cutNum();
                        break;
                    case 43://清空
                        addNum("");
                        break;
                    
                    default:
                        break;
                }
                
            }
            
            @Override
            public void onPress(int primaryCode)
            {
            }
            
            @Override
            public void onRelease(int primaryCode)
            {
            }
            
            @Override
            public void onText(CharSequence text)
            {
            }
            
            @Override
            public void swipeDown()
            {
            }
            
            @Override
            public void swipeLeft()
            {
            }
            
            @Override
            public void swipeRight()
            {
            }
            
            @Override
            public void swipeUp()
            {
            }
            
        });
    }
    
    private void addNum(String num)
    {
        if (GeneralUtils.isNotNullOrZeroLenght(num) && cardNum.length() < 6)
        {
            cardNum = cardNum + num;
            showNum();
        }
        
        if (GeneralUtils.isNullOrZeroLenght(num))
        {
            cardNum = "";
            tvIdCardOne.setText(" ");
            tvIdCardTwo.setText(" ");
            tvIdCardThree.setText(" ");
            tvIdCardFour.setText(" ");
            tvIdCardFive.setText(" ");
            tvIdCardSix.setText(" ");
        }
        
    }
    
    private void cutNum()
    {
        if (cardNum == "")
        {
            return;
        }
        if (cardNum.length() == 1)
        {
            cardNum = "";
        }
        else
        {
            cardNum = cardNum.substring(0, cardNum.length() - 1);
        }
        
        switch (cardNum.length())
        {
            case 0:
                tvIdCardOne.setText(" ");
                tvIdCardTwo.setText(" ");
                tvIdCardThree.setText(" ");
                tvIdCardFour.setText(" ");
                tvIdCardFive.setText(" ");
                tvIdCardSix.setText(" ");
                break;
            case 1:
                tvIdCardTwo.setText(" ");
                tvIdCardThree.setText(" ");
                tvIdCardFour.setText(" ");
                tvIdCardFive.setText(" ");
                tvIdCardSix.setText(" ");
                break;
            case 2:
                tvIdCardThree.setText(" ");
                tvIdCardFour.setText(" ");
                tvIdCardFive.setText(" ");
                tvIdCardSix.setText(" ");
                break;
            case 3:
                tvIdCardFour.setText(" ");
                tvIdCardFive.setText(" ");
                tvIdCardSix.setText(" ");
                break;
            case 4:
                tvIdCardFive.setText(" ");
                tvIdCardSix.setText(" ");
                break;
            case 5:
                tvIdCardSix.setText(" ");
                break;
            
            default:
                break;
        }
    }
    
    private void showNum()
    {
        for (int i = 0; i < cardNum.length(); i++)
        {
            String num = cardNum.substring(i, i + 1);
            switch (i)
            {
                case 0:
                    tvIdCardOne.setText(num);
                    break;
                case 1:
                    tvIdCardTwo.setText(num);
                    break;
                case 2:
                    tvIdCardThree.setText(num);
                    break;
                case 3:
                    tvIdCardFour.setText(num);
                    break;
                case 4:
                    tvIdCardFive.setText(num);
                    break;
                case 5:
                    tvIdCardSix.setText(num);
                    break;
                
                default:
                    break;
            }
        }
    }
    
    private void showKeyboard()
    {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE)
        {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }
    
    private void hideKeyboard()
    {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE)
        {
            keyboardView.setVisibility(View.GONE);
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            int visibility = keyboardView.getVisibility();
            if (visibility == View.VISIBLE)
            {
                keyboardView.setVisibility(View.GONE);
            }
            else
            {
                RegisterTwoActivity.this.finish();
                return true;
            }
        }
        
        return false;
    }
    
    //倒计时
    private class MyTime extends CountDownTimer
    {
        
        public MyTime(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }
        
        @Override
        public void onFinish()
        {
            btCode.setText("获取验证码");
            btCode.setTextColor(getResources().getColor(R.color.white_color));
            btCode.setClickable(true);
            btCode.setBackgroundResource(R.drawable.register_two_code_sumbit_bg_selector);
        }
        
        @Override
        public void onTick(long millisUntilFinished)
        {
            btCode.setBackgroundResource(R.drawable.register_two_code_grey_bg);
            btCode.setTextColor(getResources().getColor(R.color.person_register_choise_title));
            btCode.setText("重新获取" + millisUntilFinished / 1000);
        }
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case Constants.Register_three_code:
                setResult(Constants.Register_two_code, data);
                RegisterTwoActivity.this.finish();
                break;
            
            default:
                break;
        }
    }
    
    @Override
    public void netBack(Object ob)
    {
        if (dailog != null)
        {
            dailog.dismissDialog();
        }
        
        if (ob instanceof HouseCodeResponse)
        {
            HouseCodeResponse codeResponse = (HouseCodeResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(codeResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(codeResponse.getRetcode()))
                {
                    myTime = new MyTime(Constants.Countdown_start, Constants.Countdown_end);
                    myTime.start();
                    btCode.setClickable(false);
                    ToastUtil.makeText(RegisterTwoActivity.this, codeResponse.getRetinfo());
                }
                else
                {
                    ToastUtil.makeText(RegisterTwoActivity.this, codeResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(RegisterTwoActivity.this);
            }
        }
        if (ob instanceof BindingHouseResponse)
        {
            BindingHouseResponse houseResponse = (BindingHouseResponse)ob;
            if (GeneralUtils.isNotNullOrZeroLenght(houseResponse.getRetcode()))
            {
                if (Constants.SUCESS_CODE.equals(houseResponse.getRetcode()))
                {
                    if ("center_binding_house".equals(bindHouseCenter))
                    {
                        setResult(Constants.Register_binding_to_one);
                        RegisterTwoActivity.this.finish();
                    }
                    else
                    {
                        Intent intent = new Intent(RegisterTwoActivity.this, RegisterThreeActivity.class);
                        if (role.equals(Constants.ROLE_OWNER))
                        {
                            intent.putExtra("register_owner_phone", phoneNum);
                        }
                        intent.putExtra("register_cid", register_cid);
                        intent.putExtra("register_owner_hid", houseDList.gethId());
                        RegisterTwoActivity.this.startActivityForResult(intent, 4);
                    }
                }
                else
                {
                    ToastUtil.makeText(RegisterTwoActivity.this, houseResponse.getRetinfo());
                }
            }
            else
            {
                ToastUtil.showError(RegisterTwoActivity.this);
            }
        }
    }
}
