package asch.so.wallet.view.fragment;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.Arrays;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConfig;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.view.adapter.BaseRecyclerAdapter;
import asch.so.wallet.view.adapter.SmartViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.layout.simple_list_item_1;
import static android.R.layout.simple_list_item_2;

/**
 * Created by kimziv on 2017/10/27.
 */

public class LanguagesFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private int currentPostion=0;

    BaseRecyclerAdapter<Item> adapter = new BaseRecyclerAdapter<Item>(Arrays.asList(Item.values()), R.layout.item_languages, this) {
        @Override
        protected void onBindViewHolder(SmartViewHolder holder, Item model, int position) {
            holder.text(R.id.language_tv, model.title);
            ImageView checmarkIv=holder.itemView.findViewById(R.id.checkmark_iv);
            String code= AppConfig.getLanguage();
            if (code!=null)
            {
                if (model.code.equals(code)){
                    checmarkIv.setVisibility(View.VISIBLE);
                }else{
                    checmarkIv.setVisibility(View.INVISIBLE);
                }
            }else {
                if (model.code.equals("zh_cn")){
                    checmarkIv.setVisibility(View.VISIBLE);
                }else {
                    checmarkIv.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Item item = Item.values()[position];
        item.selected=true;
        currentPostion=position;
        AppConfig.putLanguage(item.code);
        switch (item){
            case Chinese:
            {

            }
            break;
//            case English:
//            {
//            }
//                break;
        }

        adapter.notifyDataSetChanged();
        getActivity().finish();

    }

    public enum Item{
        Chinese("zh_cn","中文",false);
       // English("en", "English",false);

        public String code;
        public String title;
        public boolean selected;
        Item(String code,String name, boolean selected) {
            this.code=code;
            this.title = name;
            this.selected = selected;
        }
    }

    public static LanguagesFragment newInstance() {
        
        Bundle args = new Bundle();
        
        LanguagesFragment fragment = new LanguagesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_languages,container,false);
        ButterKnife.bind(this,rootView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}
