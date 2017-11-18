

export default class SearchResult {
    
    constructor(jsonData) {
        this.size = jsonData['size'];
        this.page = jsonData['page'];
        this.result = jsonData['result'];
    }

}