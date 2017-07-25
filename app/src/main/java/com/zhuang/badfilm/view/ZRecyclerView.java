package com.zhuang.badfilm.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhuang.badfilm.R;

/**
 * Created by zhuang on 2016/9/14.
 */

public class ZRecyclerView extends RecyclerView {

    private static final int TYPE_LOAD_MORE = 1000;
    private static final int TYPE_HEADER = 1001;
    private static final int TYPE_EMPTY = 1002;
    private static final int Line = 0;//线条
    private static final int Interval = 1;//间隔
    private boolean loadComplete = true;//是否加载完
    private View mHeaderView;
    private View footView;
    private String emptyText;
    private WrapAdapter mWrapAdapter;
    private DataObserver mDataObserver;
    private boolean hasDiver = true;
    private int diverType = 1;

    private OnItemClickListener onItemClickListener;
    private OnLongItemClickListener onLongItemClickListener;
    private OnLoadListener onLoadListener;

    public ZRecyclerView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ZRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ZRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ZRecyclerView, defStyle, 0);
        hasDiver = a.getBoolean(R.styleable.ZRecyclerView_hasDiver, hasDiver);
        diverType = a.getInt(R.styleable.ZRecyclerView_diverType, diverType);
        if (hasDiver) {
            if (diverType == Line) {
                addItemDecoration(new Divider2(getContext()));//线条
            } else if (diverType == Interval) {
                addItemDecoration(new Divider(getContext()));//间隔
            }
        }
        a.recycle();

        footView = LayoutInflater.from(context).inflate(R.layout.zrecycleview_loadmore, null);
    }

    public void addHeaderView(View view) {
        mHeaderView = view;
    }

    public void loadComplete() {
        this.loadComplete = true;
        mWrapAdapter.notifyDataSetChanged();
    }

    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        mDataObserver = new DataObserver();
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        //mDataObserver.onChanged();
    }

    private class WrapAdapter extends Adapter<ViewHolder> {

        private Adapter adapter;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        private class HeaderViewHolder extends ViewHolder {
            public HeaderViewHolder(View itemView) {
                super(itemView);
            }
        }

        private class EmptyViewHolder extends ViewHolder {
            TextView textView;

            public EmptyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.emptyText);
            }
        }

        private class FootViewHolder extends ViewHolder {
            public FootViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_LOAD_MORE) {
                return new FootViewHolder(footView);
            } else if (viewType == TYPE_HEADER) {
                return new HeaderViewHolder(mHeaderView);
            } else if (viewType == TYPE_EMPTY) {
                View emptyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_comment, parent,false);
                return new EmptyViewHolder(emptyView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_HEADER
                    || getItemViewType(position) == TYPE_LOAD_MORE
                    ) return;

            if (getItemViewType(position) == TYPE_EMPTY) {
                ((EmptyViewHolder) holder).textView.setText(emptyText);
                return;
            }

            adapter.onBindViewHolder(holder, hasHeader() ? position - 1 : position);

            //item点击事件处理
            final int fPosition = position;
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(fPosition);
                    }
                });
            }
            if (onLongItemClickListener != null) {
                holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onLongItemClickListener.OnLongItemClick(fPosition);
                        return true;
                    }
                });
            }

            //加载更多
            if (!loadComplete && position + 1 == adapter.getItemCount() && onLoadListener != null) {
                onLoadListener.onLoad();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return TYPE_HEADER;
            }
            if (isEmptyView(position)) {
                return TYPE_EMPTY;
            }
            if (isFooter(position)) {
                return TYPE_LOAD_MORE;
            }
            return adapter.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            int count = 0;
            if (hasHeader()) count++;
            if (emptyText != null && adapter.getItemCount() == 0) count++;
            if (adapter.getItemCount() == 0) return count;
            if (!loadComplete) {
                return adapter.getItemCount() + count + 1;//1指的是加载更多的那个footview
            } else {
                return adapter.getItemCount() + count;//移除加载更多的那个footview
            }
        }

        private boolean isFooter(int position) {
            if (loadComplete) return false;
            return position == getItemCount() - 1;
        }

        private boolean hasHeader() {
            return mHeaderView != null;
        }

        private boolean isHeader(int position) {
            return hasHeader() && position == 0;
        }

        private boolean isEmptyView(int position) {
            if (emptyText != null && adapter.getItemCount() == 0) {
                return true;
            }
            return false;
        }
    }

    private class DataObserver extends AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnLoadListener {
        void onLoad();
    }

    public interface OnLongItemClickListener {
        void OnLongItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        loadComplete = false;
        this.onLoadListener = onLoadListener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        this.onLongItemClickListener = onLongItemClickListener;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }
}
