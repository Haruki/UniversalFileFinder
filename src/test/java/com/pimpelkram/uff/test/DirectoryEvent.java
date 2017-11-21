package com.pimpelkram.uff.test;

/**
 * Created by Homer on 05.06.2017.
 */
public class DirectoryEvent {

    private final ChangeTyp changeTyp;
    private final String pathName;

    public DirectoryEvent(ChangeTyp changeTyp, String pathName) {
        this.changeTyp = changeTyp;
        this.pathName = pathName;
    }

    public ChangeTyp getChangeTyp() {
        return changeTyp;
    }

    public String getPathName() {
        return pathName;
    }
}
