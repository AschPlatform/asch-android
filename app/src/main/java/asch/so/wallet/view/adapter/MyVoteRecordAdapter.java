package asch.so.wallet.view.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.so.wallet.R;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.view.fragment.MyVoteRecordFragment;
import asch.so.wallet.view.fragment.MyVoteRecordFragment.OnListFragmentInteractionListener;
import asch.so.wallet.view.fragment.dummy.DummyContent.DummyItem;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyVoteRecordAdapter extends BaseQuickAdapter<Delegate, MyVoteRecordAdapter.ViewHolder> {

    private OnSelectedDelegatesListener selectedDelegatesListener;
    private LinkedHashMap<String, Delegate> selectedDelegatesMap;

    public MyVoteRecordAdapter(@Nullable List<Delegate> data, OnSelectedDelegatesListener listener) {
        super(R.layout.item_vote_delegates, data);
        this.selectedDelegatesListener = listener;
        this.selectedDelegatesMap=new LinkedHashMap<>();
    }
    public MyVoteRecordAdapter(OnSelectedDelegatesListener listener) {
        this(null,listener);
    }
    public MyVoteRecordAdapter() {
        this(null,null);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Delegate item) {
        viewHolder.rateTv.setText(String.valueOf(item.getRate()));
        viewHolder.nameTv.setText(item.getUsername());
        viewHolder.addressTv.setText(item.getAddress());
        viewHolder.balanceTv.setText(String.valueOf(item.getBalance()));
        viewHolder.publicKeyTv.setText(item.getPublicKey());
        viewHolder.productivityTv.setText(String.valueOf(item.getProductivity()));
        viewHolder.producedBlocksTv.setText(String.valueOf(item.getProducedblocks()));
        viewHolder.missedBlocksTv.setText(String.valueOf(item.getMissedblocks()));
        viewHolder.approvalTv.setText(String.valueOf(item.getApproval()));
//        if (!item.isVoted()){
//            viewHolder.checkBox.setChecked(true);
//            viewHolder.checkBox.setClickable(false);
//            viewHolder.checkBox.setEnabled(false);
//        }else {
            viewHolder.checkBox.setChecked(getSelectedDelegatesMap().containsKey(item.getPublicKey()));
           // viewHolder.checkBox.setClickable(true);
           // viewHolder.checkBox.setEnabled(true);
       // }
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectDelegate(item);
                }else {
                    deselectDelegate(item);
                }
            }
        });
    }


    private void selectDelegate(Delegate delegate) {
        selectedDelegatesMap.put(delegate.getPublicKey(),delegate);
        if (selectedDelegatesListener!=null){
            selectedDelegatesListener.selectDelegate(delegate);
        }
    }

    private void deselectDelegate(Delegate delegate) {
        selectedDelegatesMap.remove(delegate.getPublicKey());
        if (selectedDelegatesListener!=null){
            selectedDelegatesListener.deselectDelegate(delegate);
        }
    }


    public LinkedHashMap<String, Delegate> getSelectedDelegatesMap() {
        return selectedDelegatesMap;
    }

    public List<Delegate> getSelectedDelegates(){
        LinkedHashMap<String,Delegate> delegatesMap= getSelectedDelegatesMap();
        Iterator<Map.Entry<String,Delegate>> it=delegatesMap.entrySet().iterator();
        ArrayList<Delegate> delegates=new ArrayList<>();
        while (it.hasNext()){
            Map.Entry<String,Delegate> entry =it.next();
            delegates.add(entry.getValue());
        }
        return delegates;
    }

    public void removeDownVotedDelegates(){
        LinkedHashMap<String,Delegate> delegatesMap= getSelectedDelegatesMap();
        Iterator<Map.Entry<String,Delegate>> it=delegatesMap.entrySet().iterator();
        ArrayList<Delegate> delegates=new ArrayList<>(getData());
        while (it.hasNext()){
            Map.Entry<String,Delegate> entry =it.next();
            if (delegatesMap.containsValue(entry.getValue())){
                delegates.remove(entry.getValue());
            }
        }
        this.replaceData(delegates);
    }

    public static class ViewHolder extends BaseViewHolder {

        @BindView(R.id.rate_tv)
        TextView rateTv;
        @BindView(R.id.name_tv)
        TextView nameTv;
        @BindView(R.id.address_tv)
        TextView addressTv;
        @BindView(R.id.balance_tv)
        TextView balanceTv;
        @BindView(R.id.publicKey_tv)
        TextView publicKeyTv;
        @BindView(R.id.productivity_tv)
        TextView productivityTv;
        @BindView(R.id.producedblocks_tv)
        TextView producedBlocksTv;
        @BindView(R.id.missedblocks_tv)
        TextView missedBlocksTv;
        @BindView(R.id.approval_tv)
        TextView approvalTv;
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

    }

    public interface OnSelectedDelegatesListener {

        void selectDelegate(Delegate delegate);

        void deselectDelegate(Delegate delegate);
    }
}
