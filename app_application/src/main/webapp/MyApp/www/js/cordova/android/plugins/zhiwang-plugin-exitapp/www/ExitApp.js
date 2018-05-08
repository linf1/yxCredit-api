cordova.define("zhiwang-plugin-exitapp.ExitApp",function(require, exports, module){
var exec = require('cordova/exec');

module.exports = {
  /**
   * Exits the PhoneGap application with no questions asked.
   */
  exitApp: function(exitId) {
    exec(null, null, 'ExitApp', 'exitApp', [exitId]);
  }
};
});
