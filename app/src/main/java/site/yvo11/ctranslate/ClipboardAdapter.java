package site.yvo11.ctranslate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yvo11 on 2018/2/14.
 */

public class ClipboardAdapter extends RecyclerView.Adapter<ClipboardAdapter.ViewHolder> {
    private List<String> mStringList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.r_textview);
        }
    }

    public ClipboardAdapter(List<String> stringList){
        mStringList = stringList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rec_view,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String data = mStringList.get(position);
        holder.textView.setText(data);
    }

    @Override
    public int getItemCount() {
        return mStringList.size();
    }

    public void remove(int position) {
        mStringList.remove(position);
        notifyItemRemoved(position);
    }

    public void addData(String text, int position) {
        mStringList.add(position, text);
        notifyItemInserted(position);
    }

}
