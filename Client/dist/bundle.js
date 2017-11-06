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
/******/ 	return __webpack_require__(__webpack_require__.s = 3);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_js_cookie__ = __webpack_require__(6);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_js_cookie___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_js_cookie__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__User_js__ = __webpack_require__(2);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__network_ServerAnswer_js__ = __webpack_require__(1);




const API_PATH = "http://localhost:8080/ProjetDAR/";
const GET_USER_PATH = "user/get"

class Context {
    
    constructor() {
        var url = window.location.pathname;
        this.currentPage = url.substring(url.lastIndexOf('/')+1);

        Context.currentInstance = this;

        this.user = undefined;

    }


    setConnected(key, idUser) {
        __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.set("isConnected", true);
        __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.set("key", key);
        __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.set("idUser", idUser);
        
        var cUser = this.getUserFromCookies();

        if (cUser == undefined)
            this.fetchUser(idUser);
        else
            this.user = cUser;
    }

    setDisconnected() {
        __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.remove("isConnected");
        __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.remove("key");
        __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.remove("idUser")
        __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.remove("user");
    }

    isConnected() {
        return __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.get("isConnected");
    }    

    getKey() {
        return __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.get("key");
    }

    getUserFromCookies() {
        var raw = __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.get("user");
        if (raw != undefined) 
            return __WEBPACK_IMPORTED_MODULE_1__User_js__["a" /* default */].getUserFromCookie(JSON.parse(raw));
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
                var answer = new __WEBPACK_IMPORTED_MODULE_2__network_ServerAnswer_js__["a" /* default */](data);
    
                if (answer.isSuccessful()) {
                    var rawUser = answer.payload.user;
                    this.user = __WEBPACK_IMPORTED_MODULE_1__User_js__["a" /* default */].getUserFromJson(rawUser);
                    __WEBPACK_IMPORTED_MODULE_0_js_cookie___default.a.set("user", this.user);
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
/* harmony export (immutable) */ __webpack_exports__["a"] = Context;


/***/ }),
/* 1 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
const STATUS_ANSWER = "status";
const PAYLOAD_ANSWER = "payload";
const SUCCESS_ANSWER = "success";
const FAILURE_ANSWER = "failure";

class ServerAnswer {
    
    constructor(jsonData) {
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



/***/ }),
/* 2 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";

class User {
    constructor(id, name, email, avatar) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
    }

    static getUserFromJson(json) {
        return new User(json['id'], json['username'], json['email'], '');
    }

    static getUserFromCookie(cookie) {
        return new User(cookie['id'], cookie['name'], cookie['email'], cookie['avatar']);
    }
}
/* harmony export (immutable) */ __webpack_exports__["a"] = User;


/***/ }),
/* 3 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__Controller_js__ = __webpack_require__(4);


var c = new __WEBPACK_IMPORTED_MODULE_0__Controller_js__["a" /* default */]();

c.start();



/***/ }),
/* 4 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__auth_Authentication_js__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Context_js__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__ViewManager_js__ = __webpack_require__(7);




class Controller {
    start() {
        // Connection status
        var c = new __WEBPACK_IMPORTED_MODULE_1__Context_js__["a" /* default */]();

        // Redirect to index if connected.
        if (c.isConnected() && (c.currentPage == "login.html" || c.currentPage == "login.html") ) {
            window.location = "index.html";
            return;
        }

        __WEBPACK_IMPORTED_MODULE_2__ViewManager_js__["a" /* default */].setViewConnectionStatus(c.isConnected());
        

        __WEBPACK_IMPORTED_MODULE_0__auth_Authentication_js__["a" /* default */].bindForm();
    }
}
/* harmony export (immutable) */ __webpack_exports__["a"] = Controller;


/***/ }),
/* 5 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__network_ServerAnswer_js__ = __webpack_require__(1);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Context_js__ = __webpack_require__(0);



const API_PATH = "http://localhost:8080/ProjetDAR/";

const REGISTER_PATH = "user/register";
const LOGIN_PATH = "user/login";
const LOGOUT_PATH = "user/logout";
const CHALLENGE_PATH = "user/challenge";

class Authentication {
    
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
            __WEBPACK_IMPORTED_MODULE_1__Context_js__["a" /* default */].currentInstance.setDisconnected();
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
                var answer = new __WEBPACK_IMPORTED_MODULE_0__network_ServerAnswer_js__["a" /* default */](data);
    
                if (answer.isSuccessful()) {
                    __WEBPACK_IMPORTED_MODULE_1__Context_js__["a" /* default */].currentInstance.setConnected(answer.payload["login-answer"]["key"], answer.payload["login-answer"]["iduser"]);
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
                var answer = new __WEBPACK_IMPORTED_MODULE_0__network_ServerAnswer_js__["a" /* default */](data);
    
                if (answer.isSuccessful()) {
                    __WEBPACK_IMPORTED_MODULE_1__Context_js__["a" /* default */].currentInstance.setConnected(answer.payload["login-answer"]["key"], answer.payload["login-answer"]["iduser"]);
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
/* harmony export (immutable) */ __webpack_exports__["a"] = Authentication;






/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_FACTORY__, __WEBPACK_AMD_DEFINE_RESULT__;/*!
 * JavaScript Cookie v2.2.0
 * https://github.com/js-cookie/js-cookie
 *
 * Copyright 2006, 2015 Klaus Hartl & Fagner Brack
 * Released under the MIT license
 */
;(function (factory) {
	var registeredInModuleLoader = false;
	if (true) {
		!(__WEBPACK_AMD_DEFINE_FACTORY__ = (factory),
				__WEBPACK_AMD_DEFINE_RESULT__ = (typeof __WEBPACK_AMD_DEFINE_FACTORY__ === 'function' ?
				(__WEBPACK_AMD_DEFINE_FACTORY__.call(exports, __webpack_require__, exports, module)) :
				__WEBPACK_AMD_DEFINE_FACTORY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
		registeredInModuleLoader = true;
	}
	if (true) {
		module.exports = factory();
		registeredInModuleLoader = true;
	}
	if (!registeredInModuleLoader) {
		var OldCookies = window.Cookies;
		var api = window.Cookies = factory();
		api.noConflict = function () {
			window.Cookies = OldCookies;
			return api;
		};
	}
}(function () {
	function extend () {
		var i = 0;
		var result = {};
		for (; i < arguments.length; i++) {
			var attributes = arguments[ i ];
			for (var key in attributes) {
				result[key] = attributes[key];
			}
		}
		return result;
	}

	function init (converter) {
		function api (key, value, attributes) {
			var result;
			if (typeof document === 'undefined') {
				return;
			}

			// Write

			if (arguments.length > 1) {
				attributes = extend({
					path: '/'
				}, api.defaults, attributes);

				if (typeof attributes.expires === 'number') {
					var expires = new Date();
					expires.setMilliseconds(expires.getMilliseconds() + attributes.expires * 864e+5);
					attributes.expires = expires;
				}

				// We're using "expires" because "max-age" is not supported by IE
				attributes.expires = attributes.expires ? attributes.expires.toUTCString() : '';

				try {
					result = JSON.stringify(value);
					if (/^[\{\[]/.test(result)) {
						value = result;
					}
				} catch (e) {}

				if (!converter.write) {
					value = encodeURIComponent(String(value))
						.replace(/%(23|24|26|2B|3A|3C|3E|3D|2F|3F|40|5B|5D|5E|60|7B|7D|7C)/g, decodeURIComponent);
				} else {
					value = converter.write(value, key);
				}

				key = encodeURIComponent(String(key));
				key = key.replace(/%(23|24|26|2B|5E|60|7C)/g, decodeURIComponent);
				key = key.replace(/[\(\)]/g, escape);

				var stringifiedAttributes = '';

				for (var attributeName in attributes) {
					if (!attributes[attributeName]) {
						continue;
					}
					stringifiedAttributes += '; ' + attributeName;
					if (attributes[attributeName] === true) {
						continue;
					}
					stringifiedAttributes += '=' + attributes[attributeName];
				}
				return (document.cookie = key + '=' + value + stringifiedAttributes);
			}

			// Read

			if (!key) {
				result = {};
			}

			// To prevent the for loop in the first place assign an empty array
			// in case there are no cookies at all. Also prevents odd result when
			// calling "get()"
			var cookies = document.cookie ? document.cookie.split('; ') : [];
			var rdecode = /(%[0-9A-Z]{2})+/g;
			var i = 0;

			for (; i < cookies.length; i++) {
				var parts = cookies[i].split('=');
				var cookie = parts.slice(1).join('=');

				if (!this.json && cookie.charAt(0) === '"') {
					cookie = cookie.slice(1, -1);
				}

				try {
					var name = parts[0].replace(rdecode, decodeURIComponent);
					cookie = converter.read ?
						converter.read(cookie, name) : converter(cookie, name) ||
						cookie.replace(rdecode, decodeURIComponent);

					if (this.json) {
						try {
							cookie = JSON.parse(cookie);
						} catch (e) {}
					}

					if (key === name) {
						result = cookie;
						break;
					}

					if (!key) {
						result[name] = cookie;
					}
				} catch (e) {}
			}

			return result;
		}

		api.set = api;
		api.get = function (key) {
			return api.call(api, key);
		};
		api.getJSON = function () {
			return api.apply({
				json: true
			}, [].slice.call(arguments));
		};
		api.defaults = {};

		api.remove = function (key, attributes) {
			api(key, '', extend(attributes, {
				expires: -1
			}));
		};

		api.withConverter = init;

		return api;
	}

	return init(function () {});
}));


/***/ }),
/* 7 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__User_js__ = __webpack_require__(2);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Context_js__ = __webpack_require__(0);



class ViewManager {
   
    static setViewConnectionStatus(isConnected) {
        if (isConnected) {
            var user = __WEBPACK_IMPORTED_MODULE_1__Context_js__["a" /* default */].currentInstance.getUserFromCookies();
            if (user == undefined) { // It means that something went wrong, it is not supposed to happen.
                console.log("Error while switching to connected mode. This is not supposed to happen.");
                ViewManager.switchToNotConnected();    
            } else {
                $('.user-name').text(user.name);           
                ViewManager.switchToConnected();
            }

            
        } else {
            ViewManager.switchToNotConnected();
        }
    }
   
    static switchToConnected() {
        $('.not-connected-item').hide();
        $('.connected-item').show();
    }

    static switchToNotConnected() {
        $('.not-connected-item').show();
        $('.connected-item').hide();   
    }
}
/* harmony export (immutable) */ __webpack_exports__["a"] = ViewManager;


/***/ })
/******/ ]);