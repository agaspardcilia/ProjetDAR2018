const STATUS_ANSWER = "status";
const PAYLOAD_ANSWER = "payload";
const SUCCESS_ANSWER = "success";
const FAILURE_ANSWER = "failure";

export default class ServerAnswer {
    
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
