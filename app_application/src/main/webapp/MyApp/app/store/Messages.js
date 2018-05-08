/**
 * Created by Administrator on 2017/2/24 0024.
 */

Ext.define('MyApp.store.Messages',{
    extend:'Ext.data.Store',
    config:{
        model:'MyApp.model.orders.Messages',
        proxy:{
        }
    }
});

