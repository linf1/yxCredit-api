cordova.define("cordova-plugin-device.Device", function(require, exports, module) {/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

var argscheck = require('cordova/argscheck'),
    channel = require('cordova/channel'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec'),
    cordova = require('cordova');

channel.createSticky('onCordovaInfoReady');
// Tell cordova channel to wait on the CordovaInfoReady event
channel.waitForInitialization('onCordovaInfoReady');

/**
 * This represents the mobile device, and provides properties for inspecting the model, version, UUID of the
 * phone, etc.
 * @constructor
 */
function Device() {
    this.available = false;
    this.platform = null;
    this.version = null;
    this.uuid = null;
    this.cordova = null;
    this.model = null;
    this.manufacturer = null;
    this.isVirtual = null;
    this.serial = null;

    var me = this;

    channel.onCordovaReady.subscribe(function() {
        me.getInfo(function(info) {
            //ignoring info.cordova returning from native, we should use value from cordova.version defined in cordova.js
            //TODO: CB-5105 native implementations should not return info.cordova
            var buildLabel = cordova.version;
            me.available = true;
            me.platform = info.platform;
            me.version = info.version;
            me.uuid = info.uuid;
            me.cordova = buildLabel;
            me.model = info.model;
            me.isVirtual = info.isVirtual;
            me.manufacturer = info.manufacturer || 'unknown';
            me.serial = info.serial || 'unknown';
            channel.onCordovaInfoReady.fire();
        },function(e) {
            me.available = false;
            utils.alert("[ERROR] Error initializing Cordova: " + e);
        });
    });
}

/**
 * Get device info
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
Device.prototype.getInfo = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Device.getInfo', arguments);
    exec(successCallback, errorCallback, "Device", "getDeviceInfo", []);
};
/**获取手机设备信息**/
Device.prototype.getXiHaInfo = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Device.getXiHaInfo', arguments);
    exec(successCallback, errorCallback, "Equipment", "equipment", []);
};
/**获取手机短信信息**/
Device.prototype.getSMSRecord = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Device.getSMSRecord', arguments);
    exec(successCallback, errorCallback, "SMSRecord", "smsrecord", []);
};
/**获取手机通话记录信息**/
Device.prototype.getTelephoneRecord = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Device.getTelephoneRecord', arguments);
    exec(successCallback, errorCallback, "TelephoneRecord", "telephonerecord", []);
};
/**获取手机app信息**/
Device.prototype.getAppList = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Device.getAppList', arguments);
    exec(successCallback, errorCallback, "AppList", "applist", []);
};
/**获取手机定位信息**/
Device.prototype.getLocation = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Device.getLocation', arguments);
    exec(successCallback, errorCallback, "Location", "location", []);
};
/**获取身份证信息**/
Device.prototype.idCardAuth = function(successCallback, errorCallback, option) {
    argscheck.checkArgs('fF', 'Device.idCardAuth', arguments);
    exec(successCallback, errorCallback, "IDCard", "idcard", option);
};
/***嘻哈人脸识别*/
Device.prototype.megLive = function(successCallback, errorCallback, option) {
    argscheck.checkArgs('fF', 'Device.megLive', arguments);
    exec(successCallback, errorCallback, "MegLive", "meglive", option);
};
/**魔蝎***/
Device.prototype.moxieAuth = function(successCallback, errorCallback, option) {
    argscheck.checkArgs('fF', 'Device.moxieAuth', arguments);
    exec(successCallback, errorCallback, "MoXieAuth", "moxie", option);
};
/**
 * Face++ 银行卡扫描识别
 */
Device.prototype.bankcard = function(successCallback, errorCallback, option) {
    argscheck.checkArgs('fF', 'Device.bankcard', arguments);
    exec(successCallback, errorCallback, "BankCard", "bankcard", option);
};
/**
 * 友盟分享
 */
Device.prototype.sharePages = function(successCallback, errorCallback, option) {
    argscheck.checkArgs('fF', 'Device.sharePages', arguments);
    exec(successCallback, errorCallback, "Share", "share", option);
};
    /**
     * 同盾
     */
Device.prototype.tongdun = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Device.tongdun', arguments);
    exec(successCallback, errorCallback, "Tongdun", "tongdun", []);
};
/**
 * 客户手签
 * */
Device.prototype.signature = function(successCallback, errorCallback, option) {
    argscheck.checkArgs('fF', 'Device.signature', arguments);
    exec(successCallback, errorCallback, "GestureSignature", "signature", option);
};
/**
 * 图片选择器
 */
Device.prototype.imagePicker = function(successCallback, errorCallback, option) {
    argscheck.checkArgs('fF', 'Device.imagePicker', arguments);
    exec(successCallback, errorCallback, "ImagePicker", "imagePicker", option);
};

module.exports = new Device();
});