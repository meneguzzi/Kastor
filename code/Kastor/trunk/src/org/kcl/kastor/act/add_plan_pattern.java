package org.kcl.kastor.act;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;
import jason.asSyntax.parser.as2j;

import java.io.StringReader;

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
		if(planPattern.startsWith("\"")) {
			planPattern = planPattern.substring(1, planPattern.length()-1);
		}
		
		boolean b = (new as2j(new StringReader(planPattern)).directive(ts.getAg()));
		
		return true;
	}
}
