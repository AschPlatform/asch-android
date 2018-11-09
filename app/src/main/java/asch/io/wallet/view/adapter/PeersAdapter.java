package asch.io.wallet.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import asch.io.wallet.R;
import asch.io.wallet.model.entity.PeerNode;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kimziv on 2017/11/24.
 */

public class PeersAdapter extends BaseQuickAdapter<PeerNode, PeersAdapter.ViewHolder> {


    private Context context;

    public PeersAdapter(Context ctx) {
        super(R.layout.item_peer);
        this.context=ctx;
    }

    @Override
    protected void convert(ViewHolder viewHolder, PeerNode peerNode) {
        viewHolder.ipTv.setText(peerNode.getStaredIp());
        viewHolder.versionTv.setText(peerNode.getVersion());
        viewHolder.osTv.setText(peerNode.getOs());
    }

    public static class ViewHolder extends BaseViewHolder{
        @BindView(R.id.ip_tv)
        TextView ipTv;
        @BindView(R.id.version_tv)
        TextView versionTv;
        @BindView(R.id.os_tv)
        TextView osTv;
        @BindView(R.id.status_tv)
        TextView statusTv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
