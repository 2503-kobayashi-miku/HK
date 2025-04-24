$(function() {
    $(".created-date").each(function() {
        var datetime = $(this).text();
            var from = new Date(datetime);

            // 現在時刻との差分＝経過時間
            var diff = new Date().getTime() - from.getTime();
            // 経過時間をDateに変換
            var elapsed = new Date(diff);

            // 大きい単位から順に表示
            if (elapsed.getUTCFullYear() - 1970) {
              $(this).text(elapsed.getUTCFullYear() - 1970 + '年前');
            } else if (elapsed.getUTCMonth()) {
               $(this).text(elapsed.getUTCMonth() + 'ヶ月前');
            } else if (elapsed.getUTCDate() - 1) {
               $(this).text(elapsed.getUTCDate() - 1 + '日前');
            } else if (elapsed.getUTCHours()) {
               $(this).text(elapsed.getUTCHours() + '時間前');
            } else if (elapsed.getUTCMinutes()) {
               $(this).text(elapsed.getUTCMinutes() + '分前');
            } else {
               $(this).text('たった今');
            }
    });
});