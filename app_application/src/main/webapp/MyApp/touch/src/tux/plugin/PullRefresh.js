/**
 * This plugin adds pull to refresh functionality to the List.
 */
Ext.define('Ext.tux.plugin.PullRefresh', {
    extend: 'Ext.plugin.PullRefresh',
    id:'PullRefresh',
    config: {
        width: '100%',
        /**
         * @cfg {Ext.dataview.List} list
         * The list to which this PullRefresh plugin is connected.
         * This will usually by set automatically when configuring the list with this plugin.
         * @accessor
         */
        list: null,

        /**
         * @cfg {String} pullText The text that will be shown while you are pulling down.
         * @accessor
         */
        pullText: 'Pull down to refresh...',

        /**
         * @cfg {String} releaseText The text that will be shown after you have pulled down enough to show the release message.
         * @accessor
         */
        releaseText: 'Release to refresh...',

        /**
         * @cfg {String} loadingText The text that will be shown while the list is refreshing.
         * @accessor
         */
        loadingText: 'Loading...',

        /**
         * @cfg {String} loadedText The text that will be when data has been loaded.
         * @accessor
         */
        loadedText: 'Loaded.',

        /**
         * @cfg {String} lastUpdatedText The text to be shown in front of the last updated time.
         * @accessor
         */
        lastUpdatedText: 'Last Updated:&nbsp;',

        /**
         * @cfg {Boolean} scrollerAutoRefresh Determines whether the attached scroller should automatically track size changes of its container.
         * Enabling this will have performance impacts but will be necessary if your list size changes dynamically. For example if your list contains images
         * that will be loading and have unspecified heights.
         */
        scrollerAutoRefresh: false,

        /**
         * @cfg {Boolean} autoSnapBack Determines whether the pulldown should automatically snap back after data has been loaded.
         * If false call {@link #snapBack}() to manually snap the pulldown back.
         */
        autoSnapBack: true,

        /**
         * @cfg {Number} snappingAnimationDuration The duration for snapping back animation after the data has been refreshed
         * @accessor
         */
        snappingAnimationDuration: 300,
        /**
         * @cfg {String} lastUpdatedDateFormat The format to be used on the last updated date.
         */
        lastUpdatedDateFormat: 'm/d/Y h:iA',

        /**
         * @cfg {Number} overpullSnapBackDuration The duration for snapping back when pulldown has been lowered further then its height.
         */
        overpullSnapBackDuration: 300,

        /**
         * @cfg {Ext.XTemplate/String/Array} pullTpl The template being used for the pull to refresh markup.
         * Will be passed a config object with properties state, message and updated
         *
         * @accessor
         */
        pullTpl: [
            // '<div class="x-list-pullrefresh-arrow"></div>',
            // '<div class="x-loading-spinner" style="background: #f6f6f6">',
            '<div style="background: #f6f6f6">',
                /*'<span class="x-loading-top"></span>',
             '<span class="x-loading-right"></span>',
             '<span class="x-loading-bottom"></span>',
             '<span class="x-loading-left"></span>',*/
                '<img width="24" height="24" src="resources/images/loading.gif"/>',
            '</div>',
            '<div class="x-list-pullrefresh-wrap">',
                '<h3 class="x-list-pullrefresh-message">{message}</h3>',
                '<div class="x-list-pullrefresh-updated">{updated}</div>',
            '</div>'
        ].join(''),

        translatable: true
    },

    // @private
    $state: "pull",
    // @private
    getState: function() {
        return this.$state
    },
    // @private
    setState: function(value) {
        this.$state = value;
        this.updateView();
    },
    // @private
    $isSnappingBack: false,
    // @private
    getIsSnappingBack: function() {
        return this.$isSnappingBack;
    },
    // @private
    setIsSnappingBack: function(value) {
        this.$isSnappingBack = value;
    },

    // @private
    init: function(list) {
        var me = this;

        me.setList(list);
        me.initScrollable();
    },

    getElementConfig: function() {
        return {
            reference: 'element',
            classList: ['x-unsized'],
            children: [
                {
                    reference: 'innerElement',
                    className: Ext.baseCSSPrefix + 'list-pullrefresh'
                }
            ]
        };
    },

    // @private
    initScrollable: function() {
        var me = this,
            list = me.getList(),
            scrollable = list.getScrollable(),
            scroller;

        if (!scrollable) {
            return;
        }

        scroller = scrollable.getScroller();
        scroller.setAutoRefresh(this.getScrollerAutoRefresh());

        me.lastUpdated = new Date();

        list.insert(0, me);

        scroller.on({
            scroll: me.onScrollChange,
            scope: me
        });

        this.updateView();
    },

    // @private
    applyPullTpl: function(config) {
        if (config instanceof Ext.XTemplate) {
            return config
        } else {
            return new Ext.XTemplate(config);
        }
    },

    // @private
    updateList: function(newList, oldList) {
        var me = this;

        if (newList && newList != oldList) {
            newList.on({
                order: 'after',
                scrollablechange: me.initScrollable,
                scope: me
            });
        } else if (oldList) {
            oldList.un({
                order: 'after',
                scrollablechange: me.initScrollable,
                scope: me
            });
        }
    },

    // @private
    getPullHeight: function() {
       return this.innerElement.getHeight();
    },

    /**
     * @private
     * Attempts to load the newest posts via the attached List's Store's Proxy
     */
    fetchLatest: function() {
        var store = this.getList().getStore(),
            proxy = store.getProxy(),
            operation;

        operation = Ext.create('Ext.data.Operation', {
            page: 1,
            start: 0,
            model: store.getModel(),
            limit: store.getPageSize(),
            action: 'read',
            sorters: store.getSorters(),
            filters: store.getRemoteFilter() ? store.getFilters() : []
        });

        proxy.read(operation, this.onLatestFetched, this);
    },

    /**
     * @private
     * Called after fetchLatest has finished grabbing data. Matches any returned records against what is already in the
     * Store. If there is an overlap, updates the existing records with the new data and inserts the new items at the
     * front of the Store. If there is no overlap, insert the new records anyway and record that there's a break in the
     * timeline between the new and the old records.
     */
    onLatestFetched: function(operation) {
        Ext.ComponentQuery.query('#CenterMsgView')[0].loadMsg();
        var store = this.getList().getStore(),
            oldRecords = store.getData();
        var newRecords = operation.getRecords();
        if(!newRecords[0].raw.retData){
            return;
        }
            newRecords = newRecords[0].raw.retData.pageList;
        var length = newRecords.length,
            toInsert = [],
            newRecord, oldRecord, i;
        console.log(newRecords);
        var List=Ext.ComponentQuery.query('#getMessagesListTwo')[0];
        for (i = 1; i < length; i++) {
            newRecord = newRecords[i];
            var state = newRecord.state;
            var title = newRecord.title;
            var order_state = newRecord.order_state;
            if(order_state == '5'){
                newRecord.btnText = '鉴权绑卡';
            }else if(order_state == '0'){
                newRecord.btnText = '继续申请';
            }else if(order_state == '7'){
                newRecord.btnText = '查看合同';
            }
            if(title == '绑卡成功'){
                newRecord.btnText = '确认合同';
            }
            var content=newRecord.content.indexOf('onclick');
            var noUpdate = newRecord.update_state;
            noUpdate=='0'?newRecord.noUpdate='noUpdate':newRecord.noUpdate='';
            if(content!=-1&&newRecord.update_state=='1'){
                var reg=/onclick=".*"/;
                var str=newRecord.content.replace(/onclick=".*"/ig,function(kword){
                    return 'style="color:#d5b0a0;background:#ffd2c0"';
                })
                newRecord.content=str;
            }
            state=='0'?newRecord.state='redflag':newRecord.state='';//控制已读未读状态的变化
            newRecord.alter_time=newRecord.alter_time.substring(0, 4)+'-'+newRecord.alter_time.substring(4, 6)+'-'+newRecord.alter_time.substring(6,8)+' '+newRecord.alter_time.substring(8,10)+':'+newRecord.alter_time.substring(10,12)+':'+newRecord.alter_time.substring(12,14);
            oldRecord = oldRecords.getByKey(newRecord.creat_time);

            if (oldRecord) {
                oldRecord.set(newRecord.getData());
            } else {
                toInsert.push(newRecord);
            }
            oldRecord = undefined;
        }
        Ext.getStore('Messages').setData('');
        store.insert(0, toInsert);
        this.setState("loaded");
        this.fireEvent('latestfetched', this, toInsert);
        if (this.getAutoSnapBack()) {
            this.snapBack();
        }
    },

    /**
     * Snaps the List back to the top after a pullrefresh is complete
     * @param {Boolean=} force Force the snapback to occur regardless of state {optional}
     */
    snapBack: function(force) {
        if(this.getState() !== "loaded" && force !== true) return;

        var list = this.getList(),
            scroller = list.getScrollable().getScroller();

        scroller.refresh();
        scroller.minPosition.y = 0;

        scroller.on({
            scrollend: this.onSnapBackEnd,
            single: true,
            scope: this
        });

        this.setIsSnappingBack(true);
        scroller.scrollTo(null, 0, {duration: this.getSnappingAnimationDuration()});
    },

    /**
     * @private
     * Called when PullRefresh has been snapped back to the top
     */
    onSnapBackEnd: function() {
        this.setState("pull");
        this.setIsSnappingBack(false);
    },

    /**
     * @private
     * Called when the Scroller updates from the list
     * @param scroller
     * @param x
     * @param y
     */
    onScrollChange: function(scroller, x, y) {
        if (y <= 0) {
            var pullHeight = this.getPullHeight(),
                isSnappingBack = this.getIsSnappingBack();

            if(this.getState() === "loaded" && !isSnappingBack) {
                this.snapBack();
            }

            if (this.getState() !== "loading" && this.getState() !=="loaded") {
                if (-y >= pullHeight + 10) {
                    this.setState("release");
                    scroller.getContainer().onBefore({
                        dragend: 'onScrollerDragEnd',
                        single: true,
                        scope: this
                    });
                } else if ((this.getState() === "release") && (-y < pullHeight + 10)) {
                    this.setState("pull");
                    scroller.getContainer().unBefore({
                        dragend: 'onScrollerDragEnd',
                        single: true,
                        scope: this
                    });
                }
            }
            this.getTranslatable().translate(0, -y);
        }
    },

    /**
     * @private
     * Called when the user is done dragging, this listener is only added when the user has pulled far enough for a refresh
     */
    onScrollerDragEnd: function() {
        if (this.getState() !== "loading") {
            var list = this.getList(),
                scroller = list.getScrollable().getScroller(),
                translateable = scroller.getTranslatable();

            this.setState("loading");
            translateable.setEasingY({duration: this.getOverpullSnapBackDuration()});
            scroller.minPosition.y = -this.getPullHeight();
            scroller.on({
                scrollend: 'fetchLatest',
                single: true,
                scope: this
            });
        }
    },

    /**
     * @private
     * Updates the content based on the PullRefresh Template
     */
    updateView: function() {
        var state = this.getState(),
            lastUpdatedText = this.getLastUpdatedText() + Ext.util.Format.date(this.lastUpdated, this.getLastUpdatedDateFormat()),
            templateConfig = {state: state, updated: lastUpdatedText},
            stateFn = state.charAt(0).toUpperCase() + state.slice(1).toLowerCase(),
            fn = "get" + stateFn + "Text";

        if (this[fn] && Ext.isFunction(this[fn])) {
            templateConfig.message = this[fn].call(this);
        }

        this.innerElement.removeCls(["loaded", "loading", "release", "pull"], Ext.baseCSSPrefix + "list-pullrefresh");
        this.innerElement.addCls(this.getState(), Ext.baseCSSPrefix + "list-pullrefresh");
        this.getPullTpl().overwrite(this.innerElement, templateConfig);
    }
}, function() {
    //<deprecated product=touch since=2.3>

    /**
     * Updates the PullRefreshText.
     * @method setPullRefreshText
     * @param {String} text
     * @deprecated 2.3.0 Please use {@link #setPullText} instead.
     */
    Ext.deprecateClassMethod(this, 'setPullRefreshText', 'setPullText');

    /**
     * Updates the ReleaseRefreshText.
     * @method setReleaseRefreshText
     * @param {String} text
     * @deprecated 2.3.0 Please use {@link #setReleaseText} instead.
     */
    Ext.deprecateClassMethod(this, 'setReleaseRefreshText', 'setReleaseText');

    this.override({
        constructor: function(config) {
            if (config) {
                /**
                 * @cfg {String} pullReleaseText
                 * Optional Text during the Release State.
                 * @deprecated 2.3.0 Please use {@link #releaseText} instead
                 */
                if (config.hasOwnProperty('pullReleaseText')) {
                    //<debug warn>
                    Ext.Logger.deprecate("'pullReleaseText' config is deprecated, please use 'releaseText' config instead", this);
                    //</debug>
                    config.releaseText = config.pullReleaseText;
                    delete config.pullReleaseText;
                }

                /**
                 * @cfg {String} pullRefreshText
                 * Optional Text during the Pull State.
                 * @deprecated 2.3.0 Please use {@link #pullText} instead
                 */
                if (config.hasOwnProperty('pullRefreshText')) {
                    //<debug warn>
                    Ext.Logger.deprecate("'pullRefreshText' config is deprecated, please use 'pullText' config instead", this);
                    //</debug>
                    config.pullText = config.pullRefreshText;
                    delete config.pullRefreshText;
                }
            }

            this.callParent([config]);
        }
    });
    //</deprecated>
});



/**
 * This plugin adds pull to refresh functionality to the List.
 */
/*Ext.define('Ext.tux.plugin.PullRefresh', {
    extend: 'Ext.plugin.PullRefresh',
    id:'PullRefresh',
    config: {
        width: '100%',
        /!**
         * @cfg {Ext.dataview.List} list
         * The list to which this PullRefresh plugin is connected.
         * This will usually by set automatically when configuring the list with this plugin.
         * @accessor
         *!/
        list: null,

        /!**
         * @cfg {String} pullText The text that will be shown while you are pulling down.
         * @accessor
         *!/
        pullText: 'Pull down to refresh...',

        /!**
         * @cfg {String} releaseText The text that will be shown after you have pulled down enough to show the release message.
         * @accessor
         *!/
        releaseText: 'Release to refresh...',

        /!**
         * @cfg {String} loadingText The text that will be shown while the list is refreshing.
         * @accessor
         *!/
        loadingText: 'Loading...',

        /!**
         * @cfg {String} loadedText The text that will be when data has been loaded.
         * @accessor
         *!/
        loadedText: 'Loaded.',

        /!**
         * @cfg {String} lastUpdatedText The text to be shown in front of the last updated time.
         * @accessor
         *!/
        lastUpdatedText: 'Last Updated:&nbsp;',

        /!**
         * @cfg {Boolean} scrollerAutoRefresh Determines whether the attached scroller should automatically track size changes of its container.
         * Enabling this will have performance impacts but will be necessary if your list size changes dynamically. For example if your list contains images
         * that will be loading and have unspecified heights.
         *!/
        scrollerAutoRefresh: false,

        /!**
         * @cfg {Boolean} autoSnapBack Determines whether the pulldown should automatically snap back after data has been loaded.
         * If false call {@link #snapBack}() to manually snap the pulldown back.
         *!/
        autoSnapBack: true,

        /!**
         * @cfg {Number} snappingAnimationDuration The duration for snapping back animation after the data has been refreshed
         * @accessor
         *!/
        snappingAnimationDuration: 300,
        /!**
         * @cfg {String} lastUpdatedDateFormat The format to be used on the last updated date.
         *!/
        lastUpdatedDateFormat: 'm/d/Y h:iA',

        /!**
         * @cfg {Number} overpullSnapBackDuration The duration for snapping back when pulldown has been lowered further then its height.
         *!/
        overpullSnapBackDuration: 300,

        /!**
         * @cfg {Ext.XTemplate/String/Array} pullTpl The template being used for the pull to refresh markup.
         * Will be passed a config object with properties state, message and updated
         *
         * @accessor
         *!/
        pullTpl: [
            '<div class="x-list-pullrefresh-arrow"></div>',
            '<div class="x-loading-spinner">',
            '<span class="x-loading-top"></span>',
            '<span class="x-loading-right"></span>',
            '<span class="x-loading-bottom"></span>',
            '<span class="x-loading-left"></span>',
            '</div>',
            '<div class="x-list-pullrefresh-wrap">',
            '<h3 class="x-list-pullrefresh-message">{message}</h3>',
            '<div class="x-list-pullrefresh-updated">{updated}</div>',
            '</div>'
        ].join(''),

        translatable: true
    },

    // @private
    $state: "pull",
    // @private
    getState: function() {
        return this.$state
    },
    // @private
    setState: function(value) {
        this.$state = value;
        this.updateView();
    },
    // @private
    $isSnappingBack: false,
    // @private
    getIsSnappingBack: function() {
        return this.$isSnappingBack;
    },
    // @private
    setIsSnappingBack: function(value) {
        this.$isSnappingBack = value;
    },

    // @private
    init: function(list) {
        var me = this;

        me.setList(list);
        me.initScrollable();
    },

    getElementConfig: function() {
        return {
            reference: 'element',
            classList: ['x-unsized'],
            children: [
                {
                    reference: 'innerElement',
                    className: Ext.baseCSSPrefix + 'list-pullrefresh'
                }
            ]
        };
    },

    // @private
    initScrollable: function() {
        var me = this,
            list = me.getList(),
            scrollable = list.getScrollable(),
            scroller;

        if (!scrollable) {
            return;
        }

        scroller = scrollable.getScroller();
        scroller.setAutoRefresh(this.getScrollerAutoRefresh());

        me.lastUpdated = new Date();

        list.insert(0, me);

        scroller.on({
            scroll: me.onScrollChange,
            scope: me
        });

        this.updateView();
    },

    // @private
    applyPullTpl: function(config) {
        if (config instanceof Ext.XTemplate) {
            return config
        } else {
            return new Ext.XTemplate(config);
        }
    },

    // @private
    updateList: function(newList, oldList) {
        var me = this;

        if (newList && newList != oldList) {
            newList.on({
                order: 'after',
                scrollablechange: me.initScrollable,
                scope: me
            });
        } else if (oldList) {
            oldList.un({
                order: 'after',
                scrollablechange: me.initScrollable,
                scope: me
            });
        }
    },

    // @private
    getPullHeight: function() {
        return this.innerElement.getHeight();
    },

    /!**
     * @private
     * Attempts to load the newest posts via the attached List's Store's Proxy
     *!/
    fetchLatest: function() {
        //debugger
        var store = this.getList().getStore(),
            proxy = store.getProxy(),
            operation;

        operation = Ext.create('Ext.data.Operation', {
            page: 1,
            start: 0,
            model: store.getModel(),
            limit: store.getPageSize(),
            action: 'read',
            sorters: store.getSorters(),
            filters: store.getRemoteFilter() ? store.getFilters() : []
        });

        proxy.read(operation, this.onLatestFetched, this);
    },

    /!**
     * @private
     * Called after fetchLatest has finished grabbing data. Matches any returned records against what is already in the
     * Store. If there is an overlap, updates the existing records with the new data and inserts the new items at the
     * front of the Store. If there is no overlap, insert the new records anyway and record that there's a break in the
     * timeline between the new and the old records.
     *!/
    onLatestFetched: function(operation) {
        //debugger
        var newRecordsStr = '';
        var store = this.getList().getStore();
        var oldRecords = store.getData();
        var newRecords = operation.getRecords();

        var length = newRecords.length;
        for(var i= 0;i<length;i++){
            newRecordsStr+=newRecords[i].raw;
        }
        newRecordsStr = Ext.util.JSON.decode(newRecordsStr);
        console.log(newRecordsStr);
        var   toInsert = [];
        var   newRecord, oldRecord, i;
        debugger
        for (i=0; i < newRecordsStr.length; i++) {

            newRecord = newRecordsStr[i];
            var content=newRecord.content.indexOf('onclick');
            if(content!=-1&&newRecord.update_state=='1'){
                var reg=/onclick=".*"/;
                var str=newRecord.content.replace(/onclick=".*"/ig,function(kword){
                    return 'style="color:#757575"';
                })
                newRecord.content=str;
            }
            newRecord.alter_time=newRecord.alter_time.substring(0, 4)+'-'+newRecord.alter_time.substring(4, 6)+'-'+newRecord.alter_time.substring(6,8)+' '+newRecord.alter_time.substring(8,10)+':'+newRecord.alter_time.substring(10,12)+':'+newRecord.alter_time.substring(12,14);
            oldRecord = oldRecords.getByKey(newRecords.getId());

            if (oldRecord) {
                oldRecord.set(newRecord.getData());
            } else {
                toInsert.push(newRecord);
            }

            oldRecord = undefined;
        }

        store.insert(0, toInsert);
        this.setState("loaded");
        this.fireEvent('latestfetched', this, toInsert);
        if (this.getAutoSnapBack()) {
            this.snapBack();
        }
    },

    /!**
     * Snaps the List back to the top after a pullrefresh is complete
     * @param {Boolean=} force Force the snapback to occur regardless of state {optional}
     *!/
    snapBack: function(force) {
        if(this.getState() !== "loaded" && force !== true) return;

        var list = this.getList(),
            scroller = list.getScrollable().getScroller();

        scroller.refresh();
        scroller.minPosition.y = 0;

        scroller.on({
            scrollend: this.onSnapBackEnd,
            single: true,
            scope: this
        });

        this.setIsSnappingBack(true);
        scroller.scrollTo(null, 0, {duration: this.getSnappingAnimationDuration()});
    },

    /!**
     * @private
     * Called when PullRefresh has been snapped back to the top
     *!/
    onSnapBackEnd: function() {
        this.setState("pull");
        this.setIsSnappingBack(false);
    },

    /!**
     * @private
     * Called when the Scroller updates from the list
     * @param scroller
     * @param x
     * @param y
     *!/
    onScrollChange: function(scroller, x, y) {
        if (y <= 0) {
            var pullHeight = this.getPullHeight(),
                isSnappingBack = this.getIsSnappingBack();

            if(this.getState() === "loaded" && !isSnappingBack) {
                this.snapBack();
            }

            if (this.getState() !== "loading" && this.getState() !=="loaded") {
                if (-y >= pullHeight + 10) {
                    this.setState("release");
                    scroller.getContainer().onBefore({
                        dragend: 'onScrollerDragEnd',
                        single: true,
                        scope: this
                    });
                } else if ((this.getState() === "release") && (-y < pullHeight + 10)) {
                    this.setState("pull");
                    scroller.getContainer().unBefore({
                        dragend: 'onScrollerDragEnd',
                        single: true,
                        scope: this
                    });
                }
            }
            this.getTranslatable().translate(0, -y);
        }
    },

    /!**
     * @private
     * Called when the user is done dragging, this listener is only added when the user has pulled far enough for a refresh
     *!/
    onScrollerDragEnd: function() {
        if (this.getState() !== "loading") {
            var list = this.getList(),
                scroller = list.getScrollable().getScroller(),
                translateable = scroller.getTranslatable();

            this.setState("loading");
            translateable.setEasingY({duration: this.getOverpullSnapBackDuration()});
            scroller.minPosition.y = -this.getPullHeight();
            scroller.on({
                scrollend: 'fetchLatest',
                single: true,
                scope: this
            });
        }
    },

    /!**
     * @private
     * Updates the content based on the PullRefresh Template
     *!/
    updateView: function() {
        var state = this.getState(),
            lastUpdatedText = this.getLastUpdatedText() + Ext.util.Format.date(this.lastUpdated, this.getLastUpdatedDateFormat()),
            templateConfig = {state: state, updated: lastUpdatedText},
            stateFn = state.charAt(0).toUpperCase() + state.slice(1).toLowerCase(),
            fn = "get" + stateFn + "Text";

        if (this[fn] && Ext.isFunction(this[fn])) {
            templateConfig.message = this[fn].call(this);
        }

        this.innerElement.removeCls(["loaded", "loading", "release", "pull"], Ext.baseCSSPrefix + "list-pullrefresh");
        this.innerElement.addCls(this.getState(), Ext.baseCSSPrefix + "list-pullrefresh");
        this.getPullTpl().overwrite(this.innerElement, templateConfig);
    }
}, function() {
    //<deprecated product=touch since=2.3>

    /!**
     * Updates the PullRefreshText.
     * @method setPullRefreshText
     * @param {String} text
     * @deprecated 2.3.0 Please use {@link #setPullText} instead.
     *!/
    Ext.deprecateClassMethod(this, 'setPullRefreshText', 'setPullText');

    /!**
     * Updates the ReleaseRefreshText.
     * @method setReleaseRefreshText
     * @param {String} text
     * @deprecated 2.3.0 Please use {@link #setReleaseText} instead.
     *!/
    Ext.deprecateClassMethod(this, 'setReleaseRefreshText', 'setReleaseText');

    this.override({
        constructor: function(config) {
            if (config) {
                /!**
                 * @cfg {String} pullReleaseText
                 * Optional Text during the Release State.
                 * @deprecated 2.3.0 Please use {@link #releaseText} instead
                 *!/
                if (config.hasOwnProperty('pullReleaseText')) {
                    //<debug warn>
                    Ext.Logger.deprecate("'pullReleaseText' config is deprecated, please use 'releaseText' config instead", this);
                    //</debug>
                    config.releaseText = config.pullReleaseText;
                    delete config.pullReleaseText;
                }

                /!**
                 * @cfg {String} pullRefreshText
                 * Optional Text during the Pull State.
                 * @deprecated 2.3.0 Please use {@link #pullText} instead
                 *!/
                if (config.hasOwnProperty('pullRefreshText')) {
                    //<debug warn>
                    Ext.Logger.deprecate("'pullRefreshText' config is deprecated, please use 'pullText' config instead", this);
                    //</debug>
                    config.pullText = config.pullRefreshText;
                    delete config.pullRefreshText;
                }
            }

            this.callParent([config]);
        }
    });
    //</deprecated>
});*/
