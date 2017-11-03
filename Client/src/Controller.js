import Authentification from "./auth/Authentification.js";

export default class Controller {
    start() {
        Authentification.bindForm();
    }
}