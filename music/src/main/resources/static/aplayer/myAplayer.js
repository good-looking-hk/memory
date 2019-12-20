var list = [];
// $.get("/blog/music/july", function (res) {
//     console.log(res);
//     for (var i in res) {
//         var o = new Object();
//         o.name = res[i];
//         o.artist = 'July';
//         o.url = '/music/july/' + res[i];
//         o.cover = '/img/July' + (i % 2 === 0 ? 1 : 2) + '.jpg';
//         list.push(o);
//     }
    var ap = new APlayer({
        container: document.getElementById('aplayer'),
        fixed: false,// 迷你模式
        autoplay: true,// false
        // theme: '#FADFA3',
        theme: '#00FF00',
        loop: 'one', //'all', 'one', 'none'
        order: 'list',// list/random
        preload: 'auto',//auto/none
        volume: 0.4,
        mutex: true,
        listFolded: false,
        listMaxHeight: 100,
        lrcType: 3,
        // audio: list
        audio:
            [
            {
                name: '当爱来临的时候',
                artist: '吴莫愁',
                url: '/music/3.mp3',
                cover: '/images/photo/1a7fe40de159c3f4301208183d54a0f6_wmk.jpeg'
            },
            {
                name: '错错错',
                artist: '六哲、陈娟儿',
                url: '/music/1.mp3',
                cover: 'https://ws3.sinaimg.cn/large/006zweohly1g0bgjxxankj3069069dfo.jpg',
                lrc: '/lrc/1.lrc'
            },
            {
                name: '小幸运',
                artist: '赵丽颖',
                url: '/music/2.mp3',
                cover: 'https://ws2.sinaimg.cn/large/006zweohly1g0bgnwc0udj307w04xdfr.jpg'
            }
        ]
    });
// });

