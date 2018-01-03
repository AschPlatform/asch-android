package asch.so.wallet.view.fragment;

import android.content.Intent;
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

import com.blankj.utilcode.util.AppUtils;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;

import java.util.Arrays;
import java.util.Locale;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConfig;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.activity.MainTabActivity;
import asch.so.wallet.util.AppUtil;
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
            if (position==0){
                holder.text(R.id.language_tv,getString(R.string.language_default));
            }else {
                holder.text(R.id.language_tv, model.title);
            }
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
                if (model.code.equals("default")){
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
            case Default:
            {
                LocaleChanger.setLocale(AppConstants.SUPPORTED_LOCALES.get(0));
            }
            break;
            case Chinese:
            {
                LocaleChanger.setLocale(AppConstants.SUPPORTED_LOCALES.get(1));

            }
            break;
            case English:
            {
                LocaleChanger.setLocale(AppConstants.SUPPORTED_LOCALES.get(2));
            }
                break;
            default:
                break;
        }


        ActivityRecreationHelper.recreate(getActivity(), false);

        adapter.notifyDataSetChanged();
        //getActivity().finish();
        AppUtil.restartApp(getContext());
    }

    public enum Item{
        Default("default","默认",false),
        Chinese("zh_cn","中文",false),
        English("en", "English",false);

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
