package com.iexpress.spring.domain;


public enum ProfilePlace {
	
	PROFILE_PHOTO,
	PROFILE_ALBUM,
	POST,
	COMMENT;
	
	/*
	 * private String value;
	 * 
	 * private ProfilePlace(String input) { this.value = input; }
	 */	
	public static ProfilePlace textOf(String input) {
		if(null != input) {
			for( ProfilePlace profilePlace : ProfilePlace.values()) {
				if(input.equalsIgnoreCase(profilePlace.name())) {
					return profilePlace;
				}
			}
		}
		throw new IllegalArgumentException("Illegal Argument exception");
	}
}
