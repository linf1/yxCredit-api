cordova.define("cordova-plugin-FaceScan.faceRecognition", function(require, exports, module) {
var exec = require('cordova/exec');

function faceRecognition() {};

faceRecognition.prototype.faceScan = function(success, fail, option) {
    exec(success, fail, "ocFaceModel", "faceScan", option);
};

module.exports = new faceRecognition();
});