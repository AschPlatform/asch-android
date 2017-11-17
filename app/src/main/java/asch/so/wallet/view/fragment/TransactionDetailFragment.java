package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import asch.so.base.fragment.BaseFragment;
import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.UIATransferAsset;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.TransactionType;

/**
 * Created by kimziv on 2017/10/27.
 */

public class TransactionDetailFragment extends BaseFragment {

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



    public static TransactionDetailFragment newInstance() {
        
        Bundle args = new Bundle();
        
        TransactionDetailFragment fragment = new TransactionDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_transaction_detail,container,false);
        ButterKnife.bind(this,rootView);

        Transaction transaction = getTransaction();
        txIDTv.setText(transaction.getId());
        txTypeTv.setText(Transaction.Type.fromCode(transaction.getType()).getName());
        txDateTv.setText(String.valueOf(transaction.dateFromAschTimestamp().toString()));
        txSenderTv.setText(transaction.getSenderId());
        txReceiveTv.setText(transaction.getRecipientId());
        txAmountTv.setText(amountFroTransaction(transaction));
        txFeeTv.setText(AppUtil.decimalFromBigint(transaction.getFee(), AppConstants.PRECISION).floatValue()+" XAS");
        txConfirmationsTv.setText(transaction.getConfirmations()+"");
        txBlockIdTv.setText(transaction.getBlockId());


        return rootView;
    }

    private String amountFroTransaction(Transaction transaction){
        switch (Transaction.Type.fromCode(transaction.getType())){
            case Transfer:
                return AppUtil.decimalFromBigint(transaction.getAmount(), AppConstants.PRECISION).floatValue()+" XAS";
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
            case UIATransfer:
            {
                UIATransferAsset asset=(UIATransferAsset)transaction.getAssetInfo();
                if (asset!=null && asset.getUiaTransfer() !=null){
                    return String.format("%s %s",asset.getUiaTransfer().getAmountShow(),asset.getUiaTransfer().getCurrency());
                }
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
            case Transfer:
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
            case UIATransfer:
            {
                if (transaction.getAsset()!=null){
                    UIATransferAsset asset=JSON.parseObject(transaction.getAsset(),UIATransferAsset.class);
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

}
