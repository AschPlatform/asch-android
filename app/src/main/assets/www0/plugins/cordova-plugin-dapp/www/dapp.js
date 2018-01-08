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
    deposit : function(onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "deposit", []);
},

//提现
withdraw:function(currency, amount, fee, message, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "withdraw", [currency, amount, fee, message]);
},
//内部转账
innerTransfer : function(currency, amount, fee, message, targetAddress, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "innerTransfer", [currency, amount, fee, message]);
},

//设置昵称
setNickname : function(nickname, fee, message, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "setNickname", [currency, amount, fee, message]);
},

// dapp自定义合约，发布文章
postArticle : function(title, url, text, tags, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "postArticle", [currency, amount, fee, message]);
},

// dapp自定义合约，发布评论
postComment : function(aid, pid, content, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "postArticle", [currency, amount, fee, message]);
},

// dapp自定义合约，给文章进行投票
voteArticle : function(aid, amount, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "postArticle", [currency, amount, fee, message]);
},

// dapp自定义合约，对评论进行打赏
likeComment : function(cid, amount, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "postArticle", [currency, amount, fee, message]);
},

// dapp自定义合约，举报文章
report : function(ttopic, value, onSuccess, onError){
 exec(onSuccess, onError, PLUGIN_NAME, "postArticle", [currency, amount, fee, message]);
}

};


});