package org.kcl.kastor.patterns;

import java.util.logging.Level;
import java.util.logging.Logger;

import jason.asSemantics.Agent;
import jason.asSyntax.Pred;
import jason.asSyntax.directives.Directive;

/**
 * A CA (check agent) plan pattern, which rewrites the check plan so that it 
 * calls a plan in the plan library associated with this mechanism. Not 
 * currently implemented.
 * @author meneguzzi
 *
 */
public class CA implements Directive {
	
	static Logger logger = Logger.getLogger(CA.class.getName());

	public Agent process(Pred directive, Agent outerContent, Agent innerContent) {
		logger.log(Level.SEVERE,"Directive error. - Not implemented");
		return null;
	}

}
