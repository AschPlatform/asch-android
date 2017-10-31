package asch.so.wallet.view.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/10/17.
 */

public class SecretBackupFragment extends BaseFragment implements View.OnClickListener{


    @BindView(R.id.copy_btn)
    Button copyBtn;
    @BindView(R.id.secret_tv)
    TextView secretTv;

    public static SecretBackupFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SecretBackupFragment fragment = new SecretBackupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         View rootView =inflater.inflate(R.layout.fragment_secret_backup,container,false);
        ButterKnife.bind(this,rootView);
        copyBtn.setOnClickListener(this);
       String secret=getArguments().getString("secret");
        secretTv.setText(secret);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        if (view==copyBtn){
            copySecret(secretTv.getText().toString().trim());
        }
    }

    private void copySecret(String secret){
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(secret);
        Toast.makeText(getContext(),"复制成功",Toast.LENGTH_SHORT).show();
    }
}
