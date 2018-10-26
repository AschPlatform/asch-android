package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.TimeUtils;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.UIATransferAsset;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by kimziv on 2017/10/27.
 */

public class TransactionDetailFragment extends BaseFragment implements View.OnClickListener{

    @BindView(R.id.tx_id_tv)
    TextView txIDTv;
    @BindView(R.id.tx_type_tv)
    TextView txTypeTv;
    @BindView(R.id.tx_date_tv)
    TextView txDateTv;
    @BindView(R.id.tx_sender_tv)
    TextView txSenderTv;
    @BindView(R.id.tx_receive_tv)
    TextView txReceiveTv;
    @BindView(R.id.tx_amount_tv)
    TextView txAmountTv;
    @BindView(R.id.tx_fee_tv)
    TextView txFeeTv;
    @BindView(R.id.tx_confirmations_tv)
    TextView txConfirmationsTv;
    @BindView(R.id.block_id_tv)
    TextView txBlockIdTv;
    @BindView(R.id.memo_ll)
    LinearLayout memoLl;
    @BindView(R.id.memo_tv)
    TextView memoTv;
    @BindView(R.id.icon_transaction)
    ImageView iconIv;
    @BindView(R.id.ammount_tv)
    TextView amountTv;
    @BindView(R.id.asset_tv)
    TextView assetTv;
    Unbinder unbinder;


    public static TransactionDetailFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TransactionDetailFragment fragment = new TransactionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_transaction_detail,container,false);
        unbinder = ButterKnife.bind(this, rootView);

        Transaction transaction = getTransaction();
        txIDTv.setText(transaction.getId());
        int resId=AppUtil.getResIdFromCode(Transaction.Type.fromCode(transaction.getType()));
        String transactionType=getContext().getResources().getString(resId);
        txTypeTv.setText(transactionType);
        String dateTime = TimeUtils.date2String(transaction.dateFromAschTimestamp());
       // CharSequence ago= AppUtil.getRelativeTimeSpanString(getContext(),transaction.dateFromAschTimestamp().getTime());
        txDateTv.setText(dateTime);
        txSenderTv.setText(transaction.getSenderId());
        txReceiveTv.setText(transaction.getRecipientId());
        txAmountTv.setText(amountFroTransaction(transaction));
        if (TextUtils.isEmpty(transaction.getMessage())){
            memoLl.setVisibility(View.GONE);
        }else {
            memoLl.setVisibility(View.VISIBLE);
            memoTv.setText(transaction.getMessage());
        }
        if (transaction.getTransaction()!=null){
            txFeeTv.setText(AppUtil.decimalFormat(AppUtil.decimalFromBigint(transaction.getTransaction().getFee(), AppConstants.PRECISION))+" XAS");
        }else {
            txFeeTv.setText(0);
        }
        iconIv.setImageResource(AppUtil.getIconIdByName(transaction.getCurrency()));
        amountTv.setText(amountFroTransaction(transaction));
        assetTv.setText("");
            // txConfirmationsTv.setText(String.valueOf(transaction.getConfirmations()));
       // txBlockIdTv.setText(transaction.getBlockId());

        txSenderTv.setOnClickListener(this);
        txReceiveTv.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        if (txSenderTv==v){
            AppUtil.copyText(getContext(),txSenderTv.getText().toString().trim());
        }else if (txReceiveTv==v){
            AppUtil.copyText(getContext(),txReceiveTv.getText().toString().trim());
        }
    }

    private String amountFroTransaction(Transaction transaction){
        switch (Transaction.Type.fromCode(transaction.getType())){
            case TransferV2:
                return AppUtil.decimalFormat(AppUtil.decimalFromBigint(transaction.getAmount(), AppConstants.PRECISION))+" XAS";
            case Signature:
                break;
            case Delegate:
                break;
            case Vote:
                break;
            case MultiSignature:
                break;
            case Dapp:
                break;
            case InTransfer:
                break;
            case OutTransfer:
                break;
            case Store:
                break;
            case UIAIssuer:
                break;
            case UIAAsset:
                break;
            case UIAFlags:
                break;
            case UIA_ACL:
                break;
            case UIAIssue:
                break;
            case UIATransferV2:
            {
                Transaction.AssetInfo asset=(Transaction.AssetInfo)transaction.getAssetInfo();
                if (asset!=null){
                    String name = asset.getName()==null?asset.getSymbol():asset.getName();
                    return AppUtil.decimalFormat(AppUtil.decimalFromBigint(transaction.getAmount(), asset.getPrecision()))+" "+name;
                }else {
                    return "0";
                }
                //return asset.getQuantity();
//                if (asset!=null && asset.getUiaTransfer() !=null){
//                    return String.format("%s %s",asset.getUiaTransfer().getAmountShow(),asset.getUiaTransfer().getCurrency());
//                }
            }

            case Lock:
                break;
            default:
                return "0";
        }
        return "0";
    }

    private Transaction getTransaction(){
        Bundle bundle=getArguments();
        String json=bundle.getString("transaction");
        Transaction transaction = JSON.parseObject(json,Transaction.class);
        switch (Transaction.Type.fromCode(transaction.getType())){
            case TransferV2:
                break;
            case Signature:
                break;
            case Delegate:
                break;
            case Vote:
                break;
            case MultiSignature:
                break;
            case Dapp:
                break;
            case InTransfer:
                break;
            case OutTransfer:
                break;
            case Store:
                break;
            case UIAIssuer:
                break;
            case UIAAsset:
                break;
            case UIAFlags:
                break;
            case UIA_ACL:
                break;
            case UIAIssue:
                break;
            case UIATransferV2:
            {
                if (transaction.getAsset()!=null){
                    Transaction.AssetInfo asset=JSON.parseObject(transaction.getAsset(),Transaction.AssetInfo.class);
                    transaction.setAssetInfo(asset);
                }
            }
                break;
            case Lock:
                break;
            default:
        }

        return transaction;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null)
            unbinder.unbind();
    }
}
