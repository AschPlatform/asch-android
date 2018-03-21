cordova.define("cordova-plugin-cctime.cctime", function(require, exports, module) {

var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var utils = require('cordova/utils');
var exec = require('cordova/exec');
var cordova = require('cordova');

var PLUGIN_NAME="CCTimePlugin";


module.exports = {

/**
1.1 发布文章
合约编号：1000

合约参数：

title 文章标题，非空，最长256字符
url 文章url，最长256字符
text 发表内容，最长4096字符
tags 文章标签，非空，最长20字符，多个词语用逗号分隔
注意：url与text必须有一个为空
*/
postArticle : function(onSuccess, onError, dappID, fee, title, url, text, tags){
 exec(onSuccess, onError, PLUGIN_NAME, "cctime.postArticle", [dappID, fee, title, url, text, tags]);
},

/**
1.2 发表评论
合约编号：1001

合约参数：

aid 文章编号
pid 回复评论编号，可选
content 回复内容，非空，最长4096字符
*/
postComment : function(onSuccess, onError, dappID, fee, aid, pid, content){
 exec(onSuccess, onError, PLUGIN_NAME, "cctime.postComment", [dappID, fee, aid, pid, content]);
},

/**
1.3 给文章投票
合约编号：1002

合约参数：

aid 文章编号
amount 投票数额‘，大于等于100000
*/
voteArticle : function(onSuccess, onError, dappID, fee, aid, amount){
 exec(onSuccess, onError, PLUGIN_NAME, "cctime.voteArticle", [dappID, fee, aid, amount]);
},

/**
1.4 给评论打赏
合约编号：1003

合约参数：

cid 评论编号
amount 打赏数额，大于等于100000
*/
likeComment : function(onSuccess, onError, dappID, fee, cid, amount){
 exec(onSuccess, onError, PLUGIN_NAME, "cctime.likeComment", [dappID, fee, cid, amount]);
},

/**
1.5 举报文章或评论
合约编号：1004

合约参数：

topic 只能为1或2. 1表示举报文章，2表示举报评论
value 举报的文章或评论id
*/
report : function(onSuccess, onError, dappID, fee, topic, value){
 exec(onSuccess, onError, PLUGIN_NAME, "cctime.report", [dappID, fee, topic, value]);
},

/**
1.6 设置昵称
合约编号： 4

合约参数：

nickname 用户输入的昵称，最多20字节
*/
setNickname : function(onSuccess, onError, dappID, fee, nickname){
 exec(onSuccess, onError, PLUGIN_NAME, "core.setNickname", [dappID, fee, nickname]);
}
};


});