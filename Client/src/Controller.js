import Authentification from "./auth/Authentication.js";

export default class Controller {
    start() {
        Authentication.bindForm();
    }
}