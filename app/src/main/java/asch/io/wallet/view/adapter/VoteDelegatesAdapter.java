package asch.io.wallet.view.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.io.wallet.R;
import asch.io.wallet.model.entity.Delegate;
import asch.io.wallet.view.fragment.VoteDelegatesFragment.OnListFragmentInteractionListener;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Delegate} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class VoteDelegatesAdapter extends BaseQuickAdapter<Delegate, VoteDelegatesAdapter.ViewHolder> {
    private  OnSelectedDelegatesListener selectedDelegatesListener;
    private LinkedHashMap<String, Delegate> selectedDelegatesMap;
    private HashMap<Integer,Boolean> expandStatesMap;
    public VoteDelegatesAdapter(@Nullable List<Delegate> data, OnSelectedDelegatesListener listener) {
        super(R.layout.item_vote_delegates, data);
        this.selectedDelegatesListener = listener;
        this.selectedDelegatesMap=new LinkedHashMap<>();
        this.expandStatesMap=new HashMap<Integer, Boolean>();
    }

    public VoteDelegatesAdapter(OnSelectedDelegatesListener listener) {
        this(null,listener);
    }

    public VoteDelegatesAdapter() {
        this(null,null);
    }

    @Override
    protected void convert(ViewHolder viewHolder, Delegate item) {
        //viewHolder.setIsRecyclable(false);

        viewHolder.rateTv.setText(String.valueOf(item.getRate()));
        viewHolder.nameTv.setText(item.getUsername());
        viewHolder.addressTv.setText(item.getAddress());
        viewHolder.balanceTv.setText(String.valueOf(item.getBalanceShow()));
        viewHolder.publicKeyTv.setText(item.getPublicKey());
        viewHolder.productivityTv.setText(String.format("%f%%",item.getProductivity()));
        viewHolder.producedBlocksTv.setText(String.valueOf(item.getProducedblocks()));
        viewHolder.missedBlocksTv.setText(String.valueOf(item.getMissedblocks()));
        viewHolder.approvalTv.setText(String.format("%f%%",item.getApproval()));
        if (item.isVoted()){
            viewHolder.selectBtn.setBackgroundResource(R.mipmap.item_disable);
            viewHolder.selectBtn.setClickable(false);
            viewHolder.selectBtn.setEnabled(false);
        }else {
            Delegate delegate=getItem(viewHolder.getAdapterPosition());
            boolean select=selectedDelegatesMap.containsValue(delegate);
            viewHolder.selectBtn.setBackgroundResource(select?R.mipmap.item_slected:R.mipmap.item_unslected);
            viewHolder.selectBtn.setClickable(true);
            viewHolder.selectBtn.setEnabled(true);
        }

        boolean expand= isExpand(viewHolder.getAdapterPosition()) ; //(boolean) viewHolder.expandBtn.getTag();
        viewHolder.expandableLayout.setExpanded(expand);
        viewHolder.expandBtn.setBackgroundResource(expand?R.mipmap.expand_arrow_up:R.mipmap.expand_arrow_down);
        LogUtils.d(TAG,"viewHolder.getAdapterPosition:"+viewHolder.getAdapterPosition()+" expand:"+expand);

        viewHolder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delegate delegate=getItem(viewHolder.getAdapterPosition());
                boolean select=selectedDelegatesMap.containsValue(delegate);

                if (!select){
                    if (selectedDelegatesListener!=null && selectedDelegatesListener.checkDelegateCount()) {
                        selectDelegate(delegate);
                        v.setBackgroundResource(!select?R.mipmap.item_slected:R.mipmap.item_unslected);
                    }else {
                       // v.setBackgroundResource(R.mipmap.item_unslected);
                    }
                }else {
                    deselectDelegate(delegate);
                    v.setBackgroundResource(!select?R.mipmap.item_slected:R.mipmap.item_unslected);
                }
            }
        });
        viewHolder.expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isExpand=isExpand(viewHolder.getAdapterPosition());
                if (isExpand){
                    v.setBackgroundResource(R.mipmap.expand_arrow_down);
                    expandStatesMap.put(viewHolder.getAdapterPosition(),false);
                }else {
                    v.setBackgroundResource(R.mipmap.expand_arrow_up);
                    expandStatesMap.put(viewHolder.getAdapterPosition(),true);
                }
                viewHolder.expandableLayout.toggle();
            }
        });

        viewHolder.expandableLayout.setInterpolator(new OvershootInterpolator());
    }

    private boolean isExpand(int pos){
       return expandStatesMap.containsKey(pos)?expandStatesMap.get(pos):false;
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

    public void clearSelectedDelegatesMap(){
        selectedDelegatesMap.clear();
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

    public void disableVotedDelegates(){
        LinkedHashMap<String,Delegate> delegatesMap= getSelectedDelegatesMap();
        Iterator<Map.Entry<String,Delegate>> it=delegatesMap.entrySet().iterator();
        ArrayList<Delegate> delegates=new ArrayList<>(getData());
        while (it.hasNext()){
            Map.Entry<String,Delegate> entry =it.next();
            Delegate delegate=entry.getValue();
            if (delegatesMap.containsValue(delegate)){
                delegate.setVoted(true);
            }
        }
       this.notifyDataSetChanged();
    }

    public OnSelectedDelegatesListener getSelectedDelegatesListener() {
        return selectedDelegatesListener;
    }

    public void setSelectedDelegatesListener(OnSelectedDelegatesListener selectedDelegatesListener) {
        this.selectedDelegatesListener = selectedDelegatesListener;
    }

    @Override
    public void replaceData(@NonNull Collection<? extends Delegate> data) {
        expandStatesMap.clear();
        super.replaceData(data);
    }

    public static class ViewHolder extends BaseViewHolder implements ExpandableLayout.OnExpansionUpdateListener{

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
            expandableLayout.setOnExpansionUpdateListener(this);
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            Log.d("ExpandableLayout", "State: " + state);
        }
    }


    public interface OnSelectedDelegatesListener {

        boolean checkDelegateCount();

        void selectDelegate(Delegate delegate);

        void deselectDelegate(Delegate delegate);
    }
}
