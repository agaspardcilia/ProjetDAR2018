import User from './User.js';
import Context from './Context.js';

export default class ViewManager {
   
    static setViewConnectionStatus(isConnected) {
        if (isConnected) {
            var user = Context.currentInstance.getUserFromCookies();
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