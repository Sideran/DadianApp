package com.example.naviapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.naviapp.R;
import com.example.naviapp.entity.PointEntity;
import com.example.naviapp.utils.Util;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private Context context;
    private List<PointEntity> list;
    private View inflater;
    //构造方法，传入数据,即把展示的数据源传进来，并且复制给一个全局变量，以后的操作都在该数据源上进行
    public RecyclerAdapter(Context context, List<PointEntity> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater= LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(inflater);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
//        holder.pointCode.setText(list.get(position).getPointCode());
//        holder.address.setText(list.get(position).getAddress());
//        holder.owner.setText(list.get(position).getOwner());
        //点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) mOnItemClickListener.onItemClick(v, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pointCode,address,owner;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pointCode=(TextView) itemView.findViewById(R.id.pointCode);
            address=(TextView) itemView.findViewById(R.id.address);
            owner=(TextView) itemView.findViewById(R.id.owner);
        }
    }
    //1.定义变量接收接口
    private OnItemClickListener mOnItemClickListener;
    //2.定义接口：点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position);//单击
//        void onItemLongClick(View view, int position);//长按
    }
    //3.设置接口接收的方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
