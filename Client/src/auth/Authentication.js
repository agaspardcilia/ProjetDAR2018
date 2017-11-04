import ServerAnswer from "../network/ServerAnswer.js";
import Context from "../Context.js";

const API_PATH = "http://localhost:8080/ProjetDAR/";

const REGISTER_PATH = "user/register";
const LOGIN_PATH = "user/login";
const LOGOUT_PATH = "user/logout";
const CHALLENGE_PATH = "user/challenge";

export default class Authentication {
    
    constructor() {}
    
    // Bind functions to forms.
    static  bindForm() {
        $("#login-form").submit(function(e) {
            e.preventDefault();  
            Authentication.login(e);
        });
        
        $("#register-form").submit(function(e) {
            e.preventDefault();  
            Authentication.register(e);
        });
        
        $(".logout-button").click(function() {
            console.log("doin stuff");
            Authentication.logout();
            Context.currentInstance.setDisconnected();
            window.location = "index.html";

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
                    Context.currentInstance.setConnected(answer.payload["login-answer"]["key"], answer.payload["login-answer"]["iduser"]);
                    window.location = "index.html";
                } else {
                    // TODO show an error.
                    $("#login-result").text("Server error." + answer.getErrorCode() + " : " + answer.getErrorMessage());
                }
    
            }, 
    
            error: function (xhr, ajaxOptions, thrownError) {
                // TODO show an error.
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
                    Context.currentInstance.setConnected(answer.payload["login-answer"]["key"], answer.payload["login-answer"]["iduser"]);
                    window.location = "index.html";
                } else {
                    // TODO show an error.
                    $("#register-result").text("Server error." + answer.getErrorCode() + " : " + answer.getErrorMessage());
                }
    
            }, 
    
            error: function (xhr, ajaxOptions, thrownError) {
                // TODO show an error.
                $("#register-result").text("Failure! " + xhr);
            }
    
        });
    
        return false;
    
    }

    static logout(key) {
        // TODO
        /*$.ajax({
            url: API_PATH + LOGOUT_PATH,
            type: 'post',
            data: {"key" : key},
            dataType: "json",
            async: false,
            success: function(data) {
                window.location = "index.html";
            }, 
    
            error: function (xhr, ajaxOptions, thrownError) {
                window.location = "index.html";
            }
    
        });*/
    }




}



