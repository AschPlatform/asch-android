package so.asch.sdk.transaction.asset;

import java.nio.ByteBuffer;

import so.asch.sdk.codec.Encoding;

public class DappAssetInfo extends AssetInfo{

    /*
      category: options.category,
	  name: options.name,
	  description: options.description,
	  tags: options.tags,
	  type: options.type,
	  link: options.link,
	  icon: options.icon,
	  delegates: options.delegates,
	  unlockDelegates: options.unlockDelegates
     */
    public  static  class  DappInfo{
        private String category;
        private String name;
        private String description;
        private String tags;
        private String type;
        private String link;
        private String icon;
        private String delegates;
        private String unlockDelegates;



    }



    private DappInfo dappInfo;

//    public DappAssetInfo(String dappId, String currency, long amount) {
//        this.dappInfo = new DappInfo(dappId);
//    }
//
//    public InTransfer getInTransfer() {
//        return inTransfer;
//    }

    @Override
    public void addBytes(ByteBuffer buffer) {
//        buffer.put(Encoding.getUTF8Bytes(inTransfer.getDappId()));
//        buffer.put(Encoding.getUTF8Bytes(inTransfer.getCurrency()));
//        if (!inTransfer.getCurrency().equals("XAS")){
//            buffer.put(Encoding.getUTF8Bytes(inTransfer.getAmount()));
//        }
    }
}
