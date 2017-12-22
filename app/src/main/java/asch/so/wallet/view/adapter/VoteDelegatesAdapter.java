package asch.so.wallet.view.adapter;

import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.zagum.switchicon.SwitchIconView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import asch.so.wallet.R;
import asch.so.wallet.model.entity.Delegate;
import asch.so.wallet.view.fragment.VoteDelegatesFragment.OnListFragmentInteractionListener;
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
    private static final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;
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
        viewHolder.balanceTv.setText(String.valueOf(item.getBalance()));
        viewHolder.publicKeyTv.setText(item.getPublicKey());
        viewHolder.productivityTv.setText(String.valueOf(item.getProductivity()));
        viewHolder.producedBlocksTv.setText(String.valueOf(item.getProducedblocks()));
        viewHolder.missedBlocksTv.setText(String.valueOf(item.getMissedblocks()));
        viewHolder.approvalTv.setText(String.valueOf(item.getApproval()));
        if (item.isVoted()){
            viewHolder.selectBtn.setBackgroundResource(R.mipmap.item_slected);
            viewHolder.selectBtn.setClickable(false);
            viewHolder.selectBtn.setEnabled(false);
        }else {
            viewHolder.selectBtn.setBackgroundResource(R.mipmap.item_unslected);
            viewHolder.selectBtn.setClickable(true);
            viewHolder.selectBtn.setEnabled(true);
        }

       boolean expand=  expandStatesMap.getOrDefault(viewHolder.getAdapterPosition(),false);
        viewHolder.expandableLayout.setExpanded(expand);

        viewHolder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean select=(Boolean) viewHolder.selectBtn.getTag();
                viewHolder.selectBtn.setBackgroundResource(!select?R.mipmap.item_slected:R.mipmap.item_unslected);
                viewHolder.selectBtn.setTag(!select);
                Delegate delegate = getData().get(viewHolder.getAdapterPosition());
                expandStatesMap.put(viewHolder.getAdapterPosition(),!select);
                if (!select){
                    selectDelegate(delegate);
                }else {
                    deselectDelegate(delegate);
                }
            }
        });

        viewHolder.expandableLayout.setInterpolator(new OvershootInterpolator());

       // viewHolder.expandableLayout.setOnExpansionUpdateListener(this);

//        viewHolder.expandableLayout.setInRecyclerView(true);
//        viewHolder.expandableLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//        //viewHolder.expandableLayout.setInterpolator(item.interpolator);
//        viewHolder.expandableLayout.setExpanded(viewHolder.getAdapterPosition()/2==0);
////        viewHolder.expandableLayout.setExpanded( expandStatesMap.getOrDefault(item.getPublicKey(),false));
//        viewHolder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
//            @Override
//            public void onPreOpen() {
//                expandStatesMap.put(item.getPublicKey(),true);
//                //createRotateAnimator(holder.buttonLayout, 0f, 180f).start();
//
//                //expandState.put(viewHolder.getAdapterPosition(), true);
//            }
//
//            @Override
//            public void onPreClose() {
//                //createRotateAnimator(holder.buttonLayout, 180f, 0f).start();
//                expandStatesMap.put(item.getPublicKey(),false);
//            }
//        });
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

    public static class ViewHolder extends BaseViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener{

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
            expandBtn.setOnClickListener(this);
            selectBtn.setTag(false);
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            Log.d("ExpandableLayout", "State: " + state);
            if (state != ExpandableLayout.State.COLLAPSED) {
               // .smoothScrollToPosition(getAdapterPosition());
            }
        }

        @Override
        public void onClick(View v) {
            expandableLayout.toggle();
        }
    }

//    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
//        animator.setDuration(300);
//        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
//        return animator;
//    }

    public interface OnSelectedDelegatesListener {

        void selectDelegate(Delegate delegate);

        void deselectDelegate(Delegate delegate);
    }
}
