package com.example.naviapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.naviapp.R
import com.example.naviapp.paging3.model.PointInfo

class PointPageAdapter  : PagingDataAdapter<PointInfo, PointPageAdapter.ViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<PointInfo>() {
            override fun areItemsTheSame(oldItem: PointInfo, newItem: PointInfo): Boolean {
                return oldItem.facilityId == newItem.facilityId
            }
            override fun areContentsTheSame(oldItem: PointInfo, newItem: PointInfo): Boolean {
                return oldItem == newItem
            }
        }
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name_text)
        val address: TextView = itemView.findViewById(R.id.address_text)
        val code: TextView = itemView.findViewById(R.id.code_text)
        val manufactories:TextView=itemView.findViewById(R.id.manufactories_text)
        val latlon:TextView=itemView.findViewById(R.id.latlon_text)
        val owner:TextView=itemView.findViewById(R.id.owner_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointPageAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.pagelist_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: PointPageAdapter.ViewHolder, position: Int) {
        val pointInfo=getItem(position)
        if (pointInfo!=null){
            holder.code.text=pointInfo.deviceid
            holder.name.text=pointInfo.name
            holder.address.text=pointInfo.address
            holder.manufactories.text=pointInfo.manufacturer
            holder.latlon.text=pointInfo.longitude+",\n"+pointInfo.latitude
            holder.owner.text=pointInfo.owner
        }
        //点击事件
//        holder.itemView.setOnClickListener { mOnItemClickListener?.onItemClick(holder.itemView, position) }
        holder.itemView.setOnClickListener {
            if (pointInfo != null) {
                onItemClick.invoke(it, position,pointInfo)
            }
        }
    }

//    //1.定义变量接收接口
//    private lateinit var mOnItemClickListener: OnItemClickListener
    lateinit var onItemClick: (v: View, pos: Int,pointInfo:PointInfo) -> Unit

//    //2.定义接口：点击事件
//    interface OnItemClickListener {
//        fun onItemClick(view: View?, position: Int) //单击
//
//    }
//
//    //3.设置接口接收的方法
//    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
//        mOnItemClickListener = onItemClickListener
//    }


}