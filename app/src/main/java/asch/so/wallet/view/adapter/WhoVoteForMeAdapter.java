package asch.so.wallet.view.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import asch.so.wallet.R;
import asch.so.wallet.model.entity.Voter;
import asch.so.wallet.view.fragment.WhoVoteForMeFragment.OnListFragmentInteractionListener;
import asch.so.wallet.view.fragment.dummy.DummyContent.DummyItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class WhoVoteForMeAdapter extends BaseQuickAdapter<Voter, WhoVoteForMeAdapter.ViewHolder> {

   // private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public WhoVoteForMeAdapter(@Nullable List<Voter> data, OnListFragmentInteractionListener mListener) {
        super(R.layout.item_whovoteforme, data);
        this.mListener = mListener;
    }

    public WhoVoteForMeAdapter() {
        this(null,null);
    }


    @Override
    protected void convert(ViewHolder viewHolder, Voter item) {
        viewHolder.addressTv.setText(item.getAddress());
        String name=item.getUsername().trim();
        viewHolder.nameTv.setText(name);
        viewHolder.weightTv.setText(String.valueOf(item.getWeight()));
        viewHolder.publicKeyTv.setText(item.getPublicKey());
        viewHolder.balanceTv.setText(String.valueOf(item.getBalanceShow()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null){
                    mListener.onListFragmentInteraction(item);
                }
            }
        });
    }


    public class ViewHolder  extends BaseViewHolder {
        @BindView(R.id.address_tv)
        TextView addressTv;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.weight_tv)
        TextView weightTv;
        @BindView(R.id.publicKey_tv)
        TextView publicKeyTv;
        @BindView(R.id.balance_tv)
        TextView balanceTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            balanceTv.setVisibility(View.GONE);
        }
    }
}
