cordova.define("zhiwang-plugin-face.Face",function(require, exports, module){
var exec = require('cordova/exec');

module.exports = {
  /**
   * 活体认证
   */
  authFace: function(success,error,options) {
    exec(success, error, 'Face', 'face', options);
  }
};
});
