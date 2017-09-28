package asch.so.wallet.model.entity;

/**
 * Created by kimziv on 2017/9/27.
 */

/*
        "name": "zhenxi.UIA",
		"desc": "注册资产-测试",
		"maximum": "10000000",
		"precision": 3,
		"strategy": "",
		"quantity": "1000000",
		"height": 301,
		"issuerId": "AKKHPvQb2A119LNicCQWLZQDFxhGVEY57a",
		"acl": 0,
		"writeoff": 1
 */
public class BaseAsset {

    private String name;
    private String desc;
    private String maximum;
    private int precision;
    private String strategy;
    private String quantity;
    private long height;
    private String issuerId;
    private int acl;
    private int writeoff;
}
