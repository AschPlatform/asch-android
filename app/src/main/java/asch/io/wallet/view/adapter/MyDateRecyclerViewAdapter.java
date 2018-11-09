package asch.io.wallet.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Calendar;
import asch.io.wallet.R;
import asch.io.wallet.util.StrUtil;

/**
 */
public class MyDateRecyclerViewAdapter extends RecyclerView.Adapter<MyDateRecyclerViewAdapter.ViewHolder> {

    public static final int YEAR = 100;
    public static final int MONTH = 200;
    public static final int DAY = 300;
    private final OnDateSelectListener mListener;
    private String[] mValues;
    private int type;
    private Context context;

    public MyDateRecyclerViewAdapter(Context context,int type, OnDateSelectListener listener) {
        this.type = type;
        this.context = context;
        switch (type){
            case YEAR:
                String yearSymble = context.getString(R.string.year);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                mValues = new String[]{year+yearSymble,year+1+yearSymble,year+2+yearSymble};
                break;
            case MONTH:
                String monthSymble = context.getString(R.string.month);
                mValues = new String[12];
                for (int i=0;i<12;i++){
                    mValues[i] = i + 1+monthSymble;
                }
                break;
            case DAY:
//                mValues = new String[]{"01日","02日","03日"};
                break;
        }
        mListener = listener;
    }

    public void onYearOrMonthChange(String yearStr,String monthStr){
        if(type==DAY){
            int day = getMaxDay(yearStr,monthStr);
            String daySymble = context.getString(R.string.day);
            mValues = new String[day];
            for (int i = 0; i <day ; i++) {
                mValues[i] = (i+1) + daySymble;
            }
            notifyDataSetChanged();
        }
    }

    public int getMaxDay(String yearStr,String monthStr){
        int year = StrUtil.getNumbers(yearStr);
        int month = StrUtil.getNumbers(monthStr);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_pop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mIdView.setText(mValues[position]);
        holder.mContentView.setText(mValues[position]);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onDateSelect(type,holder.mContentView.getText().toString());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.tv_item);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface OnDateSelectListener {
        void onDateSelect(int type,String str);
    }
}
