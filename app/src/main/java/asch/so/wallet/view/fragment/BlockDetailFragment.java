package asch.so.wallet.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import asch.so.wallet.AppConstants;
import asch.so.wallet.R;
import asch.so.wallet.model.entity.Block;
import asch.so.wallet.model.entity.Transaction;
import asch.so.wallet.model.entity.UIATransferAsset;
import asch.so.wallet.util.AppUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import so.asch.sdk.AschSDK;


/**
 * Created by haiziewang on 2018/03/16.
 */
public class BlockDetailFragment extends Fragment {
    @BindView(R.id.tx_height)
    TextView height;
    @BindView(R.id.tx_date)
    TextView date;
    @BindView(R.id.tx_id)
    TextView id;
    @BindView(R.id.tx_transaction)
    TextView transaction;
    @BindView(R.id.tx_money)
    TextView money;
    @BindView(R.id.tx_cost)
    TextView cost;
    @BindView(R.id.tx_award)
    TextView award;
    @BindView(R.id.tx_address)
    TextView address;
    @BindView(R.id.tx_public_key)
    TextView public_key;
    @BindView(R.id.tx_balance)
    TextView balance;

    private Block block;

    public BlockDetailFragment() {
        // Required empty public constructor
    }

    public static BlockDetailFragment newInstance() {
        BlockDetailFragment fragment = new BlockDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        String json=bundle.getString("block");
        block = JSON.parseObject(json,Block.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block_detail, container, false);
        ButterKnife.bind(this,view);

        height.setText(block.getHeight()+"");
        Date dateT = AschSDK.Helper.dateFromAschTimestamp(block.getTimestamp());
        String dateTime = TimeUtils.date2String(dateT,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        date.setText(dateTime);
        id.setText(block.getId());
        transaction.setText(block.getNumberOfTransactions()+"");
        money.setText(AppUtil.decimalFormat(AppUtil.decimalFromBigint(block.getTotalAmount(), AppConstants.PRECISION))+" XAS");
        cost.setText(AppUtil.decimalFormat(AppUtil.decimalFromBigint(block.getTotalFee(), AppConstants.PRECISION))+" XAS");
        award.setText(AppUtil.decimalFormat(AppUtil.decimalFromBigint(block.getReward(), AppConstants.PRECISION))+" XAS");
        address.setText(block.getGeneratorId());
        public_key.setText(block.getGeneratorPublicKey());

        return view;
    }


}
