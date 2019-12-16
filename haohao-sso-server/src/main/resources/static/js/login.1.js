$(function(){
	// input iCheck
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '10%' // optional
    });
});
/*

function checkForm() {
    console.log("------"+CryptoJS)

    var username = $.common.trim($("input[name='username']").val());
    var input_password = $.common.trim($("input[id='password_input']").val());
    var rememberMe = $("input[name='rememberme']").is(':checked');

    alert(username)
    alert(input_password)
    alert(CryptoJS.MD5(input_password));
    alert(rememberMe)

    var password_input = document.getElementById('password_input');
    var password = document.getElementById('password');
    password.value = CryptoJS.MD5(password_input.value);
    return true;
}
*/
