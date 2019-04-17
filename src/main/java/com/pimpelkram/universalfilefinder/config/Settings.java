package com.pimpelkram.universalfilefinder.config;

import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;

/**
 * Bean Class for storing all (persistent) settings.
 *
 * @author borsutzha
 *
 */
public class Settings {


	private ListProperty<String> rootFolderList;

	public ListProperty<String> getRootFolderList() {
		return this.rootFolderList;
	}

	public void setRootFolderList(ListProperty<String> rootFolderList) {
		this.rootFolderList = rootFolderList;
	}

}
