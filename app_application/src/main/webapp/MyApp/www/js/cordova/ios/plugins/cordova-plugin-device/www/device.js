cordova.define("cordova-plugin-device.device", function(require, exports, module) {
/*
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
Device.prototype.getXiHaInfo = function(successCallback, errorCallback) {
        argscheck.checkArgs('fF', 'Device.getXiHaInfo', arguments);
        exec(successCallback, errorCallback, "Device", "getXiHaDeviceInfo");
};
Device.prototype.getLocation = function(successCallback, errorCallback) {
       argscheck.checkArgs('fF', 'Device.getLocation', arguments);
       exec(successCallback, errorCallback, "Device", "getLocationInfo");
};
//人脸识别
Device.prototype.getMegLive = function(successCallback, errorCallback, option) {
    argscheck.checkArgs('fF', 'Device.getMegLive', arguments);
    exec(successCallback, errorCallback, "Device", "getMegLiveInfo",option);
};
//身份证识别
Device.prototype.getMegIDCardQuality = function(successCallback, errorCallback,option) {
    argscheck.checkArgs('fF', 'Device.getMegIDCardQuality', arguments);
    exec(successCallback, errorCallback, "Device", "getMegIDCardQualityInfo",option);
};
//调用魔蝎授权页面
Device.prototype.getMoxieAuth = function(successCallback, errorCallback,option) {
    argscheck.checkArgs('fF', 'Device.getMoxieAuth', arguments);
    exec(successCallback, errorCallback, "Device", "getMoxieAuthPage",option);
};
//银行卡识别
Device.prototype.getMegBankCard = function(successCallback, errorCallback,option) {
    argscheck.checkArgs('fF', 'Device.getMegBankCard', arguments);
    exec(successCallback, errorCallback, "Device", "getMegBankCardInfo",option);
};
//分享
Device.prototype.getUShare = function(successCallback, errorCallback,option) {
        argscheck.checkArgs('fF', 'Device.getUShare', arguments);
        exec(successCallback, errorCallback, "Device", "getUShareInfo",option);
};

//获取同盾指纹数据
Device.prototype.getFMDeviceManager = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Device.getFMDeviceManager', arguments);
    exec(successCallback, errorCallback, "Device", "getFMDeviceManagerInfo",[]);
};

//手签方法
Device.prototype.getWritingSignature = function(successCallback, errorCallback,option) {
    argscheck.checkArgs('fF', 'Device.getWritingSignature', arguments);
    exec(successCallback, errorCallback, "Device", "getWritingSignature",option);
};


module.exports = new Device();

});
