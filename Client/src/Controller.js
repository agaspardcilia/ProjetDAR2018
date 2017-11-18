import Authentication from "./auth/Authentication.js";
import Context from "./Context.js";
import ViewManager from "./ViewManager.js";
import EventsController from "./EventsController.js";

export default class Controller {
    constructor(apiPath) {
        this.apiPath = apiPath;
    }

    start() {
        // Connection status
        var c = new Context();
        // Redirect to index if connected.
        if (c.isConnected() && (c.currentPage == "login.html" || c.currentPage == "login.html") ) {
            window.location = "index.html";
            return;
        }

        var ec = new EventsController(this.apiPath);
        ec.setEvents();

        ViewManager.setViewConnectionStatus(c.isConnected());
        

        Authentication.bindForm();
    }
}