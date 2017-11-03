/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__Controller_js__ = __webpack_require__(1);


var c = new __WEBPACK_IMPORTED_MODULE_0__Controller_js__["a" /* default */]();

c.start();



/***/ }),
/* 1 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__auth_Authentification_js__ = __webpack_require__(2);


class Controller {
    start() {
        __WEBPACK_IMPORTED_MODULE_0__auth_Authentification_js__["a" /* default */].bindForm();
    }
}
/* harmony export (immutable) */ __webpack_exports__["a"] = Controller;


/***/ }),
/* 2 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__ServerAnswer_js__ = __webpack_require__(3);

const API_PATH = "http://localhost:8080/ProjetDAR_Server/";

const REGISTER_PATH = "user/register";
const LOGIN_PATH = "user/login";
const CHALLENGE_PATH = "user/challenge";

class Authentification {
    
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
                var answer = new __WEBPACK_IMPORTED_MODULE_0__ServerAnswer_js__["a" /* default */](data);
    
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
                var answer = new __WEBPACK_IMPORTED_MODULE_0__ServerAnswer_js__["a" /* default */](data);
    
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
/* harmony export (immutable) */ __webpack_exports__["a"] = Authentification;






/***/ }),
/* 3 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
const STATUS_ANSWER = "status";
const PAYLOAD_ANSWER = "payload";
const SUCCESS_ANSWER = "success";
const FAILURE_ANSWER = "failure";

class ServerAnswer {
    
    constructor(jsonData) {
        console.log(jsonData);

        this.successful = jsonData[STATUS_ANSWER] == SUCCESS_ANSWER;
        this.payload = jsonData[PAYLOAD_ANSWER];
    }

    isSuccessful() {
        return this.successful;
    }

    getErrorCode() {
        return this.payload["errorCode"];
    }

    getErrorMessage() {
        return this.payload["errorMessage"];
    }
}
/* harmony export (immutable) */ __webpack_exports__["a"] = ServerAnswer;



/***/ })
/******/ ]);