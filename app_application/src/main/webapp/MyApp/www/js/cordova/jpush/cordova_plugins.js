cordova.define('cordova/plugin_list', function(require, exports, module) {
module.exports = [
    {
        "file": "plugins/phonegap-plugin-jpush/www/JPushPlugin.js",
        "id": "cn.jpush.phonegap.JPushPlugin",
        "pluginId": "cordova-plugin-file",
        "clobbers": [
            "window.plugins.jPushPlugin"
        ]
    },
    {
        "file": "plugins/cordova-plugin-device/www/device.js",
        "id": "org.apache.cordova.device.Device",
        "clobbers": [
            "device"
        ]
    }
    ];

module.exports.metadata = 
// TOP OF METADATA
{
	 "cordova-plugin-whitelist": "1.2.0",
};
// BOTTOM OF METADATA
});