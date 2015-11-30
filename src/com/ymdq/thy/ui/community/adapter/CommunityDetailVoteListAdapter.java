package com.ymdq.thy.ui.community.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ymdq.thy.R;
import com.ymdq.thy.bean.community.Vote;
import com.ymdq.thy.callback.UICallBack;
import com.ymdq.thy.constant.Constants;
import com.ymdq.thy.ui.community.CommunityTopicDetailsActivity;
import com.ymdq.thy.util.GeneralUtils;

/**
 * 
 * <社区动态适配器>
 * <功能详细描述>
 * 
 * @author  cyf
 * @version  [版本号, 2014-11-21]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@SuppressLint("NewApi")
public class CommunityDetailVoteListAdapter extends BaseAdapter
{
    
    private List<Vote> mList;
    
    private CommunityTopicDetailsActivity context;
    
    private UICallBack callBack;
    
    private int sumVote;
    
    public CommunityDetailVoteListAdapter(List<Vote> data, CommunityTopicDetailsActivity context, UICallBack callBack, int sumVote)
    {
        super();
        this.mList = data;
        this.context = context;
        this.callBack = callBack;
        this.sumVote = sumVote;
    }
    
    @Override
    public int getCount()
    {
        return mList == null ? 0 : mList.size();
    }
    
    @Override
    public Object getItem(int position)
    {
        return mList == null ? null : mList.get(position);
    }
    
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final Vote entity = mList.get(position);
        final ViewHolder viewHolder;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(this.context).inflate(R.layout.community_topic_vote_list_item, null);
            viewHolder.name = (TextView)convertView.findViewById(R.id.vote_custom_item_name);
            viewHolder.num = (TextView)convertView.findViewById(R.id.vote_custom_item_num);
            viewHolder.progress = (ProgressBar)convertView.findViewById(R.id.progress_bar);
            viewHolder.img = (ImageView)convertView.findViewById(R.id.vote_custom_item_img);
            
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.name.setText(entity.getVoteName());
        viewHolder.num.setText(entity.getVoteNum());
        viewHolder.progress.setMax(sumVote);
        viewHolder.progress.setProgress(Integer.parseInt(entity.getVoteNum()));
        if("1".equals(entity.getMyVote()))
        {
            context.voteCustomId = entity.getVoteId();
            viewHolder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.community_vote_custom_item_press));
        }
        
        viewHolder.img.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                String voteId = context.voteCustomId;
                if(GeneralUtils.isNullOrZeroLenght(voteId))
                {
                    Intent i = new Intent(Constants.COMMUNITY_VOTE_SUCCESS_BROADCAST);
                    context.sendBroadcast(i);
                    context.voteCustomId = entity.getVoteId();
                    viewHolder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.community_vote_custom_item_press));
                    int num = Integer.parseInt(entity.getVoteNum())+1;
                    sumVote += 1;
                    viewHolder.num.setText(String.valueOf(num));
                    viewHolder.progress.setMax(sumVote);
                    viewHolder.progress.setProgress(num);
                }
            }
        });
        
        return convertView;
    }
    
    class ViewHolder
    {
        private TextView name;
        
        private TextView num;
        
        private ProgressBar progress;
        
        private ImageView img;
    }
}
