import ServerAnswer from "./ServerAnswer.js";
const API_PATH = "http://localhost:8080/ProjetDAR_Server/";

const REGISTER_PATH = "user/register";
const LOGIN_PATH = "user/login";
const CHALLENGE_PATH = "user/challenge";

export default class Authentication {
    
    constructor() {}
    
    // Bind functions to forms.
    static  bindForm() {
        $("#login-form").submit(function(e) {
            e.preventDefault();  
            Authentification.login(e);
        });
        
        $("#register-form").submit(function(e) {
            e.preventDefault();  
            Authentification.register(e);
        });
        
    }


    static login(submit) {
        var form = submit.target;
        
        var username = form.username.value;
        var password = form.password.value;
    
        $.ajax({
            url: API_PATH + LOGIN_PATH,
            type: 'post',
            data: {"username" : username, "password" : password},
            dataType: "json",
            async: false,
            success: function(data) {
                var answer = new ServerAnswer(data);
    
                if (answer.isSuccessful()) {
                    $("#login-result").text("Success! key : " + answer.payload["login-answer"]["key"]);
                } else {
                    $("#login-result").text("Server error." + answer.getErrorCode() + " : " + answer.getErrorMessage());
                }
    
            }, 
    
            error: function (xhr, ajaxOptions, thrownError) {
                $("#register-result").text("Failure! " + xhr);
            }
    
        });
    
        return false;
    }
   
    static  register(submit) {

        var form = submit.target;

        var username = form.username.value;
        var password = form.password.value;
        var confirmPassword= form.confirm_password.value;
        var email = form.email.value;

        if(password!==confirmPassword){
            $("#simple-message").text("les mots de passe ne se pas identiques !");  
            return false;          
        }
        $.ajax({
            url: API_PATH + REGISTER_PATH,
            type: 'post',
            data: {"username" : username, "password" : password, "email" : email},
            dataType: "json",
            async: false,
            success: function(data) {
                var answer = new ServerAnswer(data);
    
                if (answer.isSuccessful()) {
                    $("#register-result").text("Success!");
                } else {
                    $("#register-result").text("Server error." + answer.getErrorCode() + " : " + answer.getErrorMessage());
                }
    
            }, 
    
            error: function (xhr, ajaxOptions, thrownError) {
                $("#register-result").text("Failure! " + xhr);
            }
    
        });
    
        return false;
    
}






}



