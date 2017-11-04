import Cookies from 'js-cookie';
import User from './User.js';
import ServerAnswer from "./network/ServerAnswer.js";

const API_PATH = "http://localhost:8080/ProjetDAR/";
const GET_USER_PATH = "user/get"

export default class Context {
    
    constructor() {
        var url = window.location.pathname;
        this.currentPage = url.substring(url.lastIndexOf('/')+1);

        Context.currentInstance = this;

        this.user = undefined;

    }


    setConnected(key, idUser) {
        Cookies.set("isConnected", true);
        Cookies.set("key", key);
        Cookies.set("idUser", idUser);
        
        var cUser = this.getUserFromCookies();

        if (cUser == undefined)
            this.fetchUser(idUser);
        else
            this.user = cUser;
    }

    setDisconnected() {
        Cookies.remove("isConnected");
        Cookies.remove("key");
        Cookies.remove("idUser")
        Cookies.remove("user");
    }

    isConnected() {
        return Cookies.get("isConnected");
    }    

    getKey() {
        return Cookies.get("key");
    }

    getUserFromCookies() {
        var raw = Cookies.get("user");
        if (raw != undefined) 
            return User.getUserFromCookie(JSON.parse(raw));
        else
            return undefined;
    }
    

    fetchUser(idUser) {
        $.ajax({
            url: API_PATH + GET_USER_PATH,
            type: 'post',
            data: {"iduser" : idUser},
            dataType: "json",
            async: false,
            success: function(data) {
                var answer = new ServerAnswer(data);
    
                if (answer.isSuccessful()) {
                    var rawUser = answer.payload.user;
                    this.user = User.getUserFromJson(rawUser);
                    Cookies.set("user", this.user);
                } else {
                   // TODO show an error.
                }
    
            }, 
    
            error: function (xhr, ajaxOptions, thrownError) {
                $("#register-result").text("Failure! " + xhr);
            }
    
        });
    }

}