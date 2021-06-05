package cn.sun45.warbanner.ui.views.recordlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import cn.sun45.warbanner.R;

/**
 * Created by Sun45 on 2021/6/4
 * 纪录列表Adapter
 */
public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.Holder> {
    private static final String TAG = "RecordListAdapter";

    private Context context;

    private RecordListListener listener;

    private List<RecordListModel> list;

    public RecordListAdapter(Context context) {
        this.context = context;
    }

    public void setListener(RecordListListener listener) {
        this.listener = listener;
    }

    public void setList(List<RecordListModel> list) {
        this.list = list;
    }

    @NotNull
    @Override
    public Holder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recordlist_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecordListAdapter.Holder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView mText;

        private RecordListModel model;

        public Holder(@NonNull @NotNull View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.click(model);
                    }
                }
            });
        }

        public void setData(RecordListModel recordListModel) {
            model = recordListModel;
            mText.setText(recordListModel.getName());
        }
    }
}
