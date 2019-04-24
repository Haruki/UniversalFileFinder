package com.pimpelkram.universalfilefinder.config;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Bean Class for storing all (persistent) settings.
 * @author borsutzha */
public class Settings {

    private List<String> root;

    public List<String> getRoot() {
        return this.root;
    }

    public void setRoot(List<String> rootFolderList) {
        this.root = rootFolderList;
    }

    public Settings(@JsonProperty("root") List<String> rootList) {
        this.root = rootList;
    }

}
