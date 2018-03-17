package site.yvo11.ctranslate;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yvo11 on 2018/2/14.
 */

public class ClipboardAdapter extends RecyclerView.Adapter<ClipboardAdapter.ViewHolder> {
    private List<Result> mResultList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView1;
        TextView textView2;
        public ViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.r_textview1);
            textView2 = view.findViewById(R.id.r_textview2);
            textView1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }

    public ClipboardAdapter(List<Result> mResultList){
        this.mResultList = mResultList;
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
        String src = mResultList.get(position).getSrc();
        String dst = mResultList.get(position).getDst();
        holder.textView1.setText(src);
        holder.textView2.setText(dst);

    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public void remove(int position) {
        mResultList.remove(position);
        notifyItemRemoved(position);
    }

    public void addData(Result result, int position) {
        mResultList.add(position, result);
        notifyItemInserted(position);
    }

}
