package org.kcl.kastor.act;

import jason.JasonException;
import jason.asSemantics.InternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * <p>Internal action: <b><code>.add_external_plan(<i>P</i>,<i>A</i>)</code></b>.
 * 
 * @author meneguzz
 *
 */
public class add_external_plan implements InternalAction {

	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		if(args.length != 2) {
			throw new JasonException("The internal action 'add_external_plan' has not received two arguments.");
		}
		
		return null;
	}

	public boolean suspendIntention() {
		// TODO Auto-generated method stub
		return false;
	}

}
