package ExerMate.ExerMate.Base.Enum;

import ExerMate.ExerMate.Biz.Controller.UserController;


public enum UserType {
    NORMAL("유저"),
    ADMIN("관리자")
    ;

    UserType(String name){
        this.name = name;
    }

    String name;

    public String getName() {
        return name;
    }
}
