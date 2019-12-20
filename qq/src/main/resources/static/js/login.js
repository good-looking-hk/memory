$(function () {
    if (window.top !== window.self) {
        window.top.location = window.location;
    }
});

$(function () {
    $('#login-form input').iCheck({
        checkboxClass: 'icheckbox_square-blue'
    });
    $('#register-form input').iCheck({
        checkboxClass: 'icheckbox_square-red'
    });
    $('.checkbox').removeClass('hidden');
});

$(document).on('keydown', function (event) {
    if (event.keyCode === 13) {
        $("#submitBtn").click();
    }
});

$("#submitBtn").click(function () {
    var username = $(".username1").val();
    var password = $(".password1").val();
    if (username === "" || password === "") {
        return false;
    }
    var flag = $("input[name='remember']").is(":checked") === true ? 'on' : 'off';
    $.post("/login", {loginName: username, password: password, remember: flag}, function (res) {
        if (typeof res.code !== "undefined" && res.code !== 200) {
            layer.msg(res.msg, {icon: 5, time: 3000});
        } else if (res.code === 200) {
            location.href = "/";
        }
    }, 'json');
});