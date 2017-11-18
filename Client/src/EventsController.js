import ServerAnswer from "./network/ServerAnswer.js";
import SearchResult from "./network/SearchResult.js";

const GET_CITIES_PATH = "event/cities";
const GET_EVENTS_PATH = "event/last";

export default class EventsController {
    constructor(apiPath) {
        this.apiPath = apiPath;
        EventsController.currentInstance = this;
    }
    
    setEvents() {
        this.getCities();
    }

    getCities() {
        console.log(this.apiPath + GET_CITIES_PATH);
        $.ajax({
            url: this.apiPath + GET_CITIES_PATH,
            type: 'post',
            data: {},
            dataType: "json",
            async: true,
            success: function(data) {
                var answer = new ServerAnswer(data);
    
                if (answer.isSuccessful()) {
                    var sr = new SearchResult(answer.payload["cities"]);
                    
                    EventsController.currentInstance.cities = sr.result;
                    EventsController.currentInstance.getEvents();

                } else {
                    // TODO show an error.
                    console.log("Error while getting city list. (else)");
                    console.log(answer);
                }
            }, 
            error: function (xhr, ajaxOptions, thrownError) {
                // TODO show an error.
                console.log("Error while getting city list.(real error)");
                console.log("Thrown error:");
                console.log(thrownError);
                console.log("Xhr : ");
                console.log(xhr);
            }
    
        });
    }

    getEvents() {

        $.ajax({
            url: this.apiPath + GET_EVENTS_PATH,
            type: 'post',
            data: {"idcity" : EventsController.currentInstance.cities[0].id},
            dataType: "json",
            async: true,
            success: function(data) {
                var answer = new ServerAnswer(data);
    
                if (answer.isSuccessful()) {
                    var sr = new SearchResult(answer.payload["eventResult"]);
                    
                    EventsController.currentInstance.fillEventContainers(sr.result);

                } else {
                    // TODO show an error.
                    console.log("Error while getting city list.");
                    console.log(answer);
                }
            }, 
            error: function (xhr, ajaxOptions, thrownError) {
                // TODO show an error.
                console.log("Error while getting city list.");
                console.log("Failure! " + xhr);
            }
    
        });
    }

    fillEventContainers(events) {
        console.log("Filling stuff...");
        var template = $('#event-template');

        console.log("each n stuff");
        events.forEach(e => {
            var newChild = $(template).clone();
            newChild.removeAttr('id');
            
            
            newChild.find('.event-type').text(e.eventType.name);
            newChild.find('.event-city').text(e.city.name);
            newChild.find('.event-date').text(' - ' + moment(e.date).locale('fr').calendar());
            
            $('.event-container').append(newChild);
            newChild.show();
        });

    }
}