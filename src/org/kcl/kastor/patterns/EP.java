package org.kcl.kastor.patterns;

import jason.asSemantics.Agent;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBodyImpl;
import jason.asSyntax.Pred;
import jason.asSyntax.Term;
import jason.asSyntax.Trigger;
import jason.asSyntax.PlanBody.BodyType;
import jason.asSyntax.Trigger.TEOperator;
import jason.asSyntax.Trigger.TEType;
import jason.asSyntax.directives.Directive;

import java.util.logging.Logger;

/**
 * An external plan pattern EP generates a plan endpoind in the sharer agent 
 * to the PPX plan pattern from the requester agent.
 * @author meneguzzi
 *
 */
public class EP implements Directive {
	
	static Logger logger = Logger.getLogger(EP.class.getName());

	public Agent process(Pred directive, Agent outerContent, Agent innerContent) {
		try {
			Agent newAg = new Agent();
			//First unpack parameters from the directive
			Term goal = directive.getTerm(0);
			
			//The original inner content just goes into newAg
			newAg.getPL().addAll(innerContent.getPL());
			
			//And then create the request responding plan
			Pred trigLiteral = Pred.parsePred("request"+goal+"[source(S)]");
			Trigger trigger = new Trigger(TEOperator.add, TEType.achieve,trigLiteral);
			
			//We also need a label
			Pred label = Pred.parsePred(trigLiteral.getFunctor()+"_"+trigLiteral.getArity());
			
			//The context is simply true
			LogicalFormula context = ASSyntax.parseFormula("true");
			
			//And finally add everything else we need to the plan body
			PlanBodyImpl planBody = new PlanBodyImpl();
			planBody.add(new PlanBodyImpl(BodyType.achieve, goal));
			planBody.add(ASSyntax.parsePlanBody(".send(S,tell,done("+goal+"))"));
			
			Plan p = new Plan(label, trigger, context, planBody);
			newAg.getPL().add(p);
			
			return newAg;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
