package org.kcl.kastor.patterns;

import jason.asSemantics.Agent;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;
import jason.asSyntax.PlanBodyImpl;
import jason.asSyntax.Pred;
import jason.asSyntax.Term;
import jason.asSyntax.Trigger;
import jason.asSyntax.Trigger.TEOperator;
import jason.asSyntax.directives.Directive;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FHP implements Directive {
	
	static Logger logger = Logger.getLogger(FHP.class.getName());

	public Agent process(Pred directive, Agent outerContent, Agent innerContent) {
		//logger.log(Level.SEVERE,"Directive error. - Not implemented");
		try {
			Agent newAg = new Agent();
			
			//First unpack the parameters
			Term remoteAgent = directive.getTerm(0);
			int i=0;
			//Then add removal plans for every one of them
			for (Plan plan : innerContent.getPL()) {
				Trigger trig = plan.getTrigger();
				Pred label   = plan.getLabel();
				//If the label is null, we need to create one to be able to delete this plan
				if(label == null) {
					label = Pred.parsePred(trig.toString()+"_"+(i++));
					plan.setLabel(label);
				}
				
				Pred   newLabel        = Pred.parsePred("del_"+label);
				Trigger newTrig        = new Trigger(TEOperator.del, trig.getType(), trig.getLiteral().copy());
				//TODO change the context to a test of readiness of the new agent
				LogicalFormula context = ASSyntax.parseFormula("org.kcl.kastor.act.agent("+remoteAgent+")");
				PlanBody planBody      = new PlanBodyImpl();
				planBody.add(ASSyntax.parsePlanBody(".remove_plan("+label+")"));
				planBody.add(ASSyntax.parsePlanBody(".remove_plan("+newLabel+")"));
				
				
				Plan fhp = new Plan(newLabel, newTrig, context, planBody);
				newAg.getPL().add(plan);
				newAg.getPL().add(fhp);
			}
			
			//TODO Add a plan to check if the agent is still available
			
			return newAg;
		} catch (Exception e) {
			logger.log(Level.SEVERE,"Directive error.", e);
		}
		return null;
	}

}
