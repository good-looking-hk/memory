$(function () {
    if (window.top !== window.self) {
        window.top.location = window.location;
    }
});

$(function () {
    $('input').iCheck({
        checkboxClass: 'icheckbox_square-red',
    });
    $('.checkbox').removeClass('hidden');
    $("#register-form").bootstrapValidator({
        fields: {
            username: {
                validators: {
                    notEmpty: {
                        message: '用户名不能为空'
                    }
                }
            },
            email: {
                validators: {
                    notEmpty: {
                        message: '邮箱名不能为空'
                    }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '密码不能为空'
                    }
                }
            },
            repeat: {
                validators: {
                    notEmpty: {
                        message: '需要确认密码'
                    },
                    identical: {//相同
                        field: 'password',
                        message: '两次密码不一致'
                    },
                }
            }
        },
        submitHandler: function (validator, form, submitButton) {
            var flag = $("input[name='yes']").is(":checked");
            if (!flag) {
                layer.msg("竟然不同意我很帅，哼");
                setTimeout("location.href = '/register'", 1500);
                return;
            }
            var username = $("input[name='username']").val();
            var email = $("input[name='email']").val();
            var password = $("input[name='password']").val();
            $.post("/register", {username: username, email: email, password: password}, function (res) {
                if (typeof res.code !== "undefined" && res.code !== 200) {
                    layer.msg(res.msg, {icon: 5, time: 3000});
                } else if (res.code === 200) {
                    location.href = "/login";
                }
            }, 'json');
        }
    });
});
