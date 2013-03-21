package com.letsdoitworld.wastemapper.controllers;

import com.letsdoitworld.wastemapper.model.Parser;

public abstract class AbstractController {
	
	public static Parser parser = new Parser();
	
	public boolean isSuccess() {
		return parser.getConnection().isSuccess();
	}
	
}
