
export default class User {
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