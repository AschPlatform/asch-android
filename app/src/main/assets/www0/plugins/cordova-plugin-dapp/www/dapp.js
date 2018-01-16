cordova.define("cordova-plugin-dapp.dapp", function(require, exports, module) {

var argscheck = require('cordova/argscheck');
var channel = require('cordova/channel');
var utils = require('cordova/utils');
var exec = require('cordova/exec');
var cordova = require('cordova');

var PLUGIN_NAME="DAppPlugin";


module.exports = {
//授权
    authorize:function(onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "authorize", []);
},

//充值
    deposit : function(onSuccess, onError, dappID, currency, amount, message){
 exec(onSuccess, onError, PLUGIN_NAME, "deposit", [ dappID, currency, amount, message]);
},

//提现
withdraw:function(onSuccess, onError, dappID, currency, amount, message, fee){
 exec(onSuccess, onError, PLUGIN_NAME, "withdraw", [dappID, currency, amount, message, fee]);
},
//内部转账
innerTransfer : function(onSuccess, onError, dappID, currency, targetAddress, amount, fee, message){
 exec(onSuccess, onError, PLUGIN_NAME, "innerTransfer", [dappID, currency, targetAddress, amount, message, fee]);
},

//设置昵称
setNickname : function(onSuccess, onError, dappID, nickname, fee){
 exec(onSuccess, onError, PLUGIN_NAME, "setNickname", [dappID, nickname, fee]);
},

// dapp自定义合约，发布文章
postArticle : function(title, url, text, tags, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "postArticle", [currency, amount, fee, message]);
},

// dapp自定义合约，发布评论
postComment : function(aid, pid, content, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "postComment", [currency, amount, fee, message]);
},

// dapp自定义合约，给文章进行投票
voteArticle : function(aid, amount, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "voteArticle", [currency, amount, fee, message]);
},

// dapp自定义合约，对评论进行打赏
likeComment : function(cid, amount, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "likeComment", [currency, amount, fee, message]);
},

// dapp自定义合约，举报文章
report : function(ttopic, value, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "report", [currency, amount, fee, message]);
}

};


});