 document.addEventListener("deviceready",onDeviceReady,false);    
        // device APIs are available    //    
        function onDeviceReady() {  
            initiateUI();
        }

        var getRegistrationID = function() {
            window.plugins.jPushPlugin.getRegistrationID(onGetRegistrationID);
        };

        var onTagsWithAlias = function(event) {
            try {
                var result = "result code:" + event.resultCode + " ";
                result += "tags:" + event.tags + " ";
                result += "alias:" + event.alias + " ";
            } catch (exception) {
            }
        };

       

        var onReceiveNotification = function(event) {
            try {
                var alertContent;
                if (device.platform == "Android") {
                	//window.location = "${staticInput}/app/main/myset/mymessage.jsp";
                } else {
                    alertContent = event.aps.alert;
                }
            } catch (exception) {
            }
        };

        var onReceiveMessage = function(event) {
            try {
                var message;
                if (device.platform == "Android") {
                	//window.location = "${staticInput}/main/myset/mymessage.jsp";
                } else {
                    message = event.content;
                }
            } catch (exception) {
            }
        };

        var initiateUI = function() {
            try {
                window.plugins.jPushPlugin.init();
                var s = getRegistrationID();
                if (device.platform != "Android") {
                    window.plugins.jPushPlugin.setDebugModeFromIos();
                    window.plugins.jPushPlugin.setApplicationIconBadgeNumber(0);
                } else {
                    window.plugins.jPushPlugin.setDebugMode(true);
                    window.plugins.jPushPlugin.setStatisticsOpen(true);
                }
            } catch (exception) {
            }
            //setAlias("18895365564");
        };
		function setAlias(alias){
			 try {
                    var tags = [];
                    window.plugins.jPushPlugin.setTagsWithAlias(tags, alias);
                } catch (exception) {
                }
		}
        document.addEventListener("jpush.setTagsWithAlias", onTagsWithAlias, false);
        document.addEventListener("deviceready", onDeviceReady, false);
        document.addEventListener("jpush.openNotification", onOpenNotification, false);
        document.addEventListener("jpush.receiveNotification", onReceiveNotification, false);
        document.addEventListener("jpush.receiveMessage", onReceiveMessage, false);