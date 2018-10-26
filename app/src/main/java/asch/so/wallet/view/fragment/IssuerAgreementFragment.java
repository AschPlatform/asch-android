package asch.so.wallet.view.fragment;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.webkit.WebView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import asch.so.base.activity.BaseActivity;
import asch.so.wallet.P;
import asch.so.wallet.R;
import asch.so.wallet.activity.RegisterIssuerActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IssuerAgreementFragment extends Fragment  {

    @BindView(R.id.issuer_agreement_wv)
    WebView issuerAgreementWv;
    @BindView(R.id.agreement_line)
    View agreementLine;
    @BindView(R.id.agreement_agree)
    TextView agreeTv;
    @BindView(R.id.agreement_deny)
    TextView denyTv;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.fragment_issuer_agreement,container,false);
        unbinder= ButterKnife.bind(this,rootView);
        issuerAgreementWv.loadUrl(getString(R.string.agreement_url));
        denyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        agreeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = getArguments().getString("title");
                if (TextUtils.isEmpty(from)){
                    return;
                }
                if (from.equals(getString(R.string.register_issuer))){
                    BaseActivity.start(getActivity(),RegisterIssuerActivity.class,new Bundle());
                }
//                else if(from.equals())

            }
        });
        return rootView;
    }

    public static IssuerAgreementFragment newInstance() {

        Bundle args = new Bundle();
        IssuerAgreementFragment fragment = new IssuerAgreementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }

}
