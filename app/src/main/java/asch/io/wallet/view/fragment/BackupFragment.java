package asch.io.wallet.view.fragment;

import android.os.Bundle;

import asch.io.base.fragment.BaseFragment;

/**
 * Created by kimziv on 2017/10/25.
 */

public class BackupFragment extends BaseFragment {

    public static BackupFragment newInstance() {
        
        Bundle args = new Bundle();
        
        BackupFragment fragment = new BackupFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
