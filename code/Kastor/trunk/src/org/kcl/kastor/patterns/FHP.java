package org.kcl.kastor.patterns;

import java.util.logging.Level;
import java.util.logging.Logger;

import jason.asSemantics.Agent;
import jason.asSyntax.Pred;
import jason.asSyntax.directives.Directive;

public class FHP implements Directive {
	
	static Logger logger = Logger.getLogger(FHP.class.getName());

	public Agent process(Pred directive, Agent outerContent, Agent innerContent) {
		logger.log(Level.SEVERE,"Directive error. - Not implemented");
		return null;
	}

}
