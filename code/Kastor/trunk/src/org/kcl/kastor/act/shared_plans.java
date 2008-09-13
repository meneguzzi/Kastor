package org.kcl.kastor.act;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jason.JasonException;
import jason.asSemantics.InternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Plan;
import jason.asSyntax.StringTerm;
import jason.asSyntax.StringTermImpl;
import jason.asSyntax.Term;

/**
 * <p>Internal action: <b><code>.shared_plans(<i>P</i>)</code></b>.
 * @author meneguzz
 *
 */
public class shared_plans implements InternalAction {
	
	protected final Logger logger = Logger.getLogger(InternalAction.class.getName());

	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		
		if(args.length != 1) {
			throw new JasonException("The internal action 'shared_plans' has not received one argument.");
		}
		
		List<Plan> plans = ts.getAg().getPL().getPlans();
		List<Plan> sharedPlans = new ArrayList<Plan>();
		
		for(Plan plan : plans) {
			if(plan.getLabel().toString().startsWith("action")) {
				sharedPlans.add(plan);
			}
		}
		
		ListTerm planList = new ListTermImpl();
		for(Plan plan : sharedPlans) {
			StringTerm planSrc = StringTermImpl.parseString(plan.toASString());
			planList.add(planSrc);
		}
		
		Unifier unifier = new Unifier();
		
		if(unifier.unifies(args[0], planList)) {
			args[0].apply(unifier);
			return true;
		} else {
			return false;
		}
	}

	public boolean suspendIntention() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean canBeUsedInContext() {
		return true;
	}

}
