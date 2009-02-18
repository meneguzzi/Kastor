package org.kcl.kastor.act;

import java.io.StringReader;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.asSyntax.parser.as2j;

public class add_plan_pattern extends DefaultInternalAction {
	@Override
	public int getMinArgs() {return 1;}
	@Override
	public int getMaxArgs() {return 1;}
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		checkArguments(args);
		
		String planPattern = args[0].toString();
		
		boolean b = (new as2j(new StringReader(planPattern)).directive(ts.getAg()));
		
		return b;
	}
}
