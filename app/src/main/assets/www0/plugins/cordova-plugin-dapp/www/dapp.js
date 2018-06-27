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
 exec(onSuccess, onError, PLUGIN_NAME, "core.deposit", [ dappID, currency, amount, message]);
},

//提现
withdraw:function(onSuccess, onError, dappID, currency, amount, message, fee){
 exec(onSuccess, onError, PLUGIN_NAME, "core.withdrawal", [dappID, currency, amount, message, fee]);
},
//内部转账
innerTransfer : function(onSuccess, onError, dappID, currency, targetAddress, amount, message, fee){
 exec(onSuccess, onError, PLUGIN_NAME, "core.transfer", [dappID, currency, targetAddress, amount, message, fee]);
},

//设置昵称
setNickname : function(onSuccess, onError, dappID, nickname, fee){
 exec(onSuccess, onError, PLUGIN_NAME, "core.setNickname", [dappID, nickname, fee]);
}

};


});