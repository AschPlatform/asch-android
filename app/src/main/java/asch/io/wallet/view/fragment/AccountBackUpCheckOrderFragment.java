package asch.io.wallet.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import asch.io.base.fragment.BaseFragment;
import asch.io.wallet.R;
import asch.io.wallet.accounts.AccountsManager;
import asch.io.wallet.util.AppUtil;
import asch.io.wallet.view.widget.BackUpMenmonicAlertDialog;
import asch.io.wallet.view.widget.TagContainerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import asch.io.wallet.view.widget.TagView;


public class AccountBackUpCheckOrderFragment extends BaseFragment{

    Unbinder unbinder;
    @BindView(R.id.backup_order_next)
    Button nextBtn;
    @BindView(R.id.backup_order_mnemonic)
    TextView mnemonicTv;
    @BindView(R.id.tag_group)
    TagContainerLayout mTagGroup;
    List<String>listShow;
    List<String >listSeed;


    public static AccountBackUpCheckOrderFragment newInstance() {
        Bundle args = new Bundle();
        AccountBackUpCheckOrderFragment fragment = new AccountBackUpCheckOrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_account_backup_check_order,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        initData();

        mTagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                setData(text,position);
            }

            @Override
            public void onTagLongClick(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        return rootView;
    }

    private void initData(){
        mnemonicTv.setText("");
        String seed = getArguments().getString("seed");
        listSeed = Arrays.asList(seed.split(" "));
        List<String> listRandom = Arrays.asList(seed.split(" "));
        Collections.shuffle(listRandom);
        mTagGroup.setTags(listRandom);

        listShow = new ArrayList<>();
    }


    private void setData(String text,int index){

        TagView v = mTagGroup.getTagView(index);
        if(listShow.contains(text)){
            v.setTagBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources()
                    .getColor(R.color.tag_gray))));
            v.invalidate();
            listShow.remove(text);
        }else {
            v.setTagBackgroundColor(Color.parseColor("#"+Integer.toHexString(getResources()
                    .getColor(R.color.main_color))));
            v.invalidate();
            listShow.add(text);
        }

        String all ="";
        for (String s:listShow){
            all+=(s+" ");
        }
        mnemonicTv.setText(all);
    }

    private void check(){

        if (listSeed.equals(listShow)){
            AccountsManager.getInstance().setAccountBackup(true);
            new BackUpMenmonicAlertDialog(getActivity())
                    .setTitle(getString(R.string.check_order_success))
                    .setContent(getString(R.string.check_order_success_content))
                    .show();
        }else {
            AppUtil.toastError(getActivity(),getString(R.string.check_order_err));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
        }

    }


}
