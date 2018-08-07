package com.demo.multilevellist;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.HashMap;
import java.util.List;



/**
 * Created by xulc on 2018/7/27.
 */

public class ReasonAdapter extends BaseAdapter {
    private Context mcontext;
    private Activity mActivity;
    private List<TreePoint> reasonPointList;
    private String keyword = "";
    private HashMap<String, TreePoint> reasonMap = new HashMap<>();


    public ReasonAdapter(final Context mcontext, List<TreePoint> reasonPointList, HashMap<String, TreePoint> reasonMap) {
        this.mcontext = mcontext;
        this.mActivity = (Activity) mcontext;
        this.reasonPointList = reasonPointList;
        this.reasonMap = reasonMap;
    }


    public void setKeyword(String keyword) {
        this.keyword = keyword;
        //全部闭合，然后展开
        for (TreePoint reasonTreePoint : reasonPointList) {
            reasonTreePoint.setExpand(false);
        }
        if (!"".equals(keyword)) {
            for (TreePoint reasonTreePoint : reasonPointList) {
                if (reasonTreePoint.getNNAME().contains(keyword)) {
                    //含有keyword
                    if ("0".equals(reasonTreePoint.getISLEAF())) {
                        reasonTreePoint.setExpand(true);
                    }
                    //打开所有的父级元素
                    openExpand(reasonTreePoint);
                }
            }
        }
        this.notifyDataSetChanged();

    }

    private void openExpand(TreePoint reasonTreePoint) {
        if ("0".equals(reasonTreePoint.getPARENTID())) {
            reasonTreePoint.setExpand(true);
        } else {
            reasonMap.get(reasonTreePoint.getPARENTID()).setExpand(true);
            openExpand(reasonMap.get(reasonTreePoint.getPARENTID()));
        }
    }


    //第一要准确计算数量
    @Override
    public int getCount() {
        int count = 0;
        for (TreePoint tempPoint : reasonPointList) {
            if ("0".equals(tempPoint.getPARENTID())) {
                count++;
            } else {
                if (getItemIsExpand(tempPoint.getPARENTID())) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean getItemIsExpand(String ID) {
        for (TreePoint tempPoint : reasonPointList) {
            if (ID.equals(tempPoint.getID())) {
                if (tempPoint.isExpand()) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public Object getItem(int position) {
        return reasonPointList.get(convertPostion(position));
    }

    private int convertPostion(int position) {
        int count = 0;
        for (int i = 0; i < reasonPointList.size(); i++) {
            TreePoint treePoint = reasonPointList.get(i);
            if ("0".equals(treePoint.getPARENTID())) {
                count++;
            } else {
                if (getItemIsExpand(treePoint.getPARENTID())) {
                    count++;
                }
            }
            if (position == (count - 1)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.adapter_treeview, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            holder.contactitemBtn = (ImageButton) convertView.findViewById(R.id.contactitem_sendmsg);
            holder.contactitemBtn.setVisibility(View.GONE);
            convertView.setTag(holder);
            convertView.setBackgroundColor(0xffffffff);
            convertView.setPadding(DensityUtil.dip2px(mcontext, 10), DensityUtil.dip2px(mcontext, 10), DensityUtil.dip2px(mcontext, 10), DensityUtil.dip2px(mcontext, 10));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TreePoint tempPoint = (TreePoint) getItem(position);

        int level = TreeUtils.getLevel(tempPoint, reasonMap);
        holder.icon.setPadding(25 * level, holder.icon.getPaddingTop(), 0, holder.icon.getPaddingBottom());
        if ("0".equals(tempPoint.getISLEAF())) {
            if (tempPoint.isExpand() == false) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(R.drawable.outline_list_collapse);
            } else {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(R.drawable.outline_list_expand);
            }
        } else {
            holder.icon.setVisibility(View.INVISIBLE);
            holder.icon.setImageResource(R.drawable.outline_list_collapse);
        }

        if (keyword != null && !"".equals(keyword) && tempPoint.getNNAME().contains(keyword)) {
            int index = tempPoint.getNNAME().indexOf(keyword);
            int len = keyword.length();
            Spanned temp = Html.fromHtml(tempPoint.getNNAME().substring(0, index)
                    + "<font color=#FF0000>"
                    + tempPoint.getNNAME().substring(index, index + len) + "</font>"
                    + tempPoint.getNNAME().substring(index + len, tempPoint.getNNAME().length()));

            holder.text.setText(temp);
        } else {
            holder.text.setText(tempPoint.getNNAME());
        }
        holder.text.setCompoundDrawablePadding(DensityUtil.dip2px(mcontext, 10));

        holder.contactitemBtn.setSelected(false);
        if (tempPoint.isExpand())
            holder.contactitemBtn.setSelected(true);
        return convertView;
    }

    public void onItemClick(int position) {
        TreePoint reasonTreePoint = (TreePoint) getItem(position);
        if ("1".equals(reasonTreePoint.getISLEAF())) {
            //处理回填
            Toast.makeText(mcontext, getSubmitResult(reasonTreePoint), Toast.LENGTH_SHORT).show();
        } else {  //如果点击的是父类
            if (reasonTreePoint.isExpand()) {
                for (TreePoint tempPoint : reasonPointList) {
                    if (tempPoint.getPARENTID().equals(reasonTreePoint.getID())) {
                        if ("0".equals(reasonTreePoint.getISLEAF())) {
                            tempPoint.setExpand(false);
                        }
                    }
                }
                reasonTreePoint.setExpand(false);
            } else {
                reasonTreePoint.setExpand(true);
            }
        }
        this.notifyDataSetChanged();
    }




    private String getSubmitResult(TreePoint reasonTreePoint) {
        StringBuilder sb = new StringBuilder();
        addResult(reasonTreePoint, sb);
        String result = sb.toString();
        if (result.endsWith("-")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private void addResult(TreePoint reasonTreePoint, StringBuilder sb) {
        if (reasonTreePoint != null && sb != null) {
            sb.insert(0, reasonTreePoint.getNNAME() + "-");
            if (!"0".equals(reasonTreePoint.getPARENTID())) {
                addResult(reasonMap.get(reasonTreePoint.getPARENTID()), sb);
            }
        }
    }


    class ViewHolder {
        TextView text;
        ImageView icon;
        ImageButton contactitemBtn;
    }
}
