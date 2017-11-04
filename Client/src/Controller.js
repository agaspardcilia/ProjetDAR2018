import Authentication from "./auth/Authentication.js";
import Context from "./Context.js";
import ViewManager from "./ViewManager.js";

export default class Controller {
    start() {
        // Connection status
        var c = new Context();

        // Redirect to index if connected.
        if (c.isConnected() && (c.currentPage == "login.html" || c.currentPage == "login.html") ) {
            window.location = "index.html";
            return;
        }

        ViewManager.setViewConnectionStatus(c.isConnected());
        

        Authentication.bindForm();
    }
}