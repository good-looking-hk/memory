layui.config({
    base: 'layui/lay/'
}).extend({
    socket: 'socket',
});
layui.use(['layim', 'jquery', 'socket'], function (layim, $, socket) {
    socket.config({
        layim: layim,
    });

    layim.config({
        init: {
            url: '/user/info', data: {}
        },
        //获取群成员
        members: {
            url: 'class/doAction.php?action=groupMembers', data: {}
        }
        //上传图片接口
        , uploadImage: {
            url: 'class/doAction.php?action=uploadImage' //（返回的数据格式见下文）
            , type: 'post' //默认post
        }
        //上传文件接口
        , uploadFile: {
            url: 'class/doAction.php?action=uploadFile' //
            , type: 'post' //默认post
        }
        //自定义皮肤
        , uploadSkin: {
            url: 'class/doAction.php?action=uploadSkin'
            , type: 'post' //默认post
        }
        //选择系统皮肤
        , systemSkin: {
            url: 'class/doAction.php?action=systemSkin'
            , type: 'post' //默认post
        }
        //获取推荐好友
        , getRecommend: {
            url: 'class/doAction.php?action=getRecommend'
            , type: 'get' //默认
        }
        //查找好友总数
        , findFriendTotal: {
            url: 'class/doAction.php?action=findFriendTotal'
            , type: 'get' //默认
        }
        //查找好友
        , findFriend: {
            url: 'class/doAction.php?action=findFriend'
            , type: 'get' //默认
        }
        //获取好友资料
        , getInformation: {
            url: '/user/getUserInfo'
            , type: 'get' //默认
        }
        //保存我的资料
        , saveMyInformation: {
            url: '/user/saveInfo'
            , type: 'post' //默认
        }
        //提交建群信息
        , commitGroupInfo: {
            url: 'class/doAction.php?action=commitGroupInfo'
            , type: 'get' //默认
        }
        //获取系统消息
        , getMsgBox: {
            url: '/user/msgBox'
            , type: 'get' //默认post
        }
        //获取总的记录数
        , getChatLogTotal: {
            url: 'class/doAction.php?action=getChatLogTotal'
            , type: 'get' //默认post
        }
        //获取历史记录
        , getChatLog: {
            url: 'class/doAction.php?action=getChatLog'
            , type: 'get' //默认post
        }
        , isAudio: false //开启聊天工具栏音频
        , isVideo: false //开启聊天工具栏视频
        , groupMembers: true
        //扩展工具栏
        // , tool: [{
        //         alias: 'code'
        //         , title: '代码'
        //         , icon: '&#xe64e;'
        //     }]
        , title: 'layim'
        , copyright: true
        , initSkin: '1.jpg' //1-5 设置初始背景
        , notice: true //是否开启桌面消息提醒，默认false
        , systemNotice: false //是否开启系统消息提醒，默认false
        , msgbox: layui.cache.dir + 'css/modules/layim/html/msgbox.html' //消息盒子页面地址，若不开启，剔除该项即可
        , find: layui.cache.dir + 'css/modules/layim/html/find.html' //发现页面地址，若不开启，剔除该项即可
        , chatLog: layui.cache.dir + 'css/modules/layim/html/chatlog.html' //聊天记录页面地址，若不开启，剔除该项即可
        , createGroup: layui.cache.dir + 'css/modules/layim/html/createGroup.html' //创建群页面地址，若不开启，剔除该项即可
        , information: layui.cache.dir + 'css/modules/layim/html/getInformation.html' //好友群资料页面
    });
});




