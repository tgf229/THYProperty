package com.ymdq.thy.ui.community;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Member;
import com.ymdq.thy.ui.BaseActivity;
import com.ymdq.thy.ui.community.adapter.CommunityMemberAdapter;

public class CommunityMemberActivity extends BaseActivity implements OnClickListener
{
    /**
     * 返回按钮
     */
    private LinearLayout back;
    
    /**
     * 标题
     */
    private TextView title;
    
    /**
     * listview
     */
    private ListView listView;
    
    /**
     * 成员集合
     */
    private ArrayList<Member> members;
    
    /**
     * 适配器
     */
    private CommunityMemberAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_member);
        
        init();
        initData();
    }
    
    /**
     * 
     * <初始化布局组件>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void init()
    {
        back = (LinearLayout)findViewById(R.id.title_back_layout);
        title = (TextView)findViewById(R.id.title_name);
        listView = (ListView)findViewById(R.id.list_view);
        
        back.setOnClickListener(this);
        
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(CommunityMemberActivity.this, CommunityPersonDetailActivity.class);
                intent.putExtra("queryUId", members.get(position).getuId());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    
    /**
     * 
     * <初始化数据>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    private void initData()
    {
        title.setText("圈子成员");
        members = (ArrayList<Member>)getIntent().getSerializableExtra("members");
        adapter = new CommunityMemberAdapter(members, this);
        listView.setAdapter(adapter);
    }
    
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
        /**
         * 响应返回按钮
         */
            case R.id.title_back_layout:
                finish();
                break;
            
            default:
                break;
        }
    }
    
}
