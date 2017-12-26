package asch.so.wallet.view.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
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
    private HashMap<Integer,Boolean> expandStatesMap;
    public MyVoteRecordAdapter(@Nullable List<Delegate> data, OnSelectedDelegatesListener listener) {
        super(R.layout.item_vote_delegates, data);
        this.selectedDelegatesListener = listener;
        this.selectedDelegatesMap=new LinkedHashMap<>();
        this.expandStatesMap=new HashMap<Integer, Boolean>();
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
        if (getSelectedDelegatesMap().containsValue(item)){
            viewHolder.selectBtn.setBackgroundResource(R.mipmap.item_slected);
        }else {
            viewHolder.selectBtn.setBackgroundResource(R.mipmap.item_unslected);
        }

        boolean expand= (boolean) viewHolder.expandBtn.getTag();
        viewHolder.expandableLayout.setExpanded(expand);
        viewHolder.expandBtn.setBackgroundResource(expand?R.mipmap.expand_arrow_up:R.mipmap.expand_arrow_down);

        viewHolder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delegate delegate=getItem(viewHolder.getAdapterPosition());
                boolean select=selectedDelegatesMap.containsValue(delegate);
                v.setBackgroundResource(!select?R.mipmap.item_slected:R.mipmap.item_unslected);
                if (!select){
                    selectDelegate(delegate);
                }else {
                    deselectDelegate(delegate);
                }

//                boolean select=(Boolean) viewHolder.selectBtn.getTag();
//                viewHolder.selectBtn.setBackgroundResource(!select?R.mipmap.item_slected:R.mipmap.item_unslected);
//                viewHolder.selectBtn.setTag(!select);
//                Delegate delegate = getData().get(viewHolder.getAdapterPosition());
//                expandStatesMap.put(viewHolder.getAdapterPosition(),!select);
//                if (!select){
//                    selectDelegate(delegate);
//                }else {
//                    deselectDelegate(delegate);
//                }
            }
        });

        viewHolder.expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state=viewHolder.expandableLayout.getState();
                if (state == ExpandableLayout.State.EXPANDED) {
                    v.setBackgroundResource(R.mipmap.expand_arrow_down);
                }else if (state == ExpandableLayout.State.COLLAPSED){
                    v.setBackgroundResource(R.mipmap.expand_arrow_up);
                }
                boolean select=(Boolean) v.getTag();
                v.setTag(!select);
                viewHolder.expandableLayout.toggle();
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

    public static class ViewHolder extends BaseViewHolder implements  ExpandableLayout.OnExpansionUpdateListener{

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
        @BindView(R.id.expandable_layout)
        ExpandableLayout expandableLayout;
        @BindView(R.id.select_btn)
        ImageButton selectBtn;
        @BindView(R.id.expand_btn)
        ImageButton expandBtn;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
           // expandBtn.setOnClickListener(this);
            expandBtn.setTag(false);
            selectBtn.setTag(false);
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {

        }
    }

    public interface OnSelectedDelegatesListener {

        void selectDelegate(Delegate delegate);

        void deselectDelegate(Delegate delegate);
    }
}
