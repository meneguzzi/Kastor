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
import jason.asSyntax.directives.Directive;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A CA (check agent) plan pattern, which rewrites the check plan so that it 
 * calls a plan in the plan library associated with this mechanism. Not 
 * currently implementing any verification methods.
 * @author meneguzzi
 *
 */
public class CA implements Directive {
	
	static Logger logger = Logger.getLogger(CA.class.getName());

	public Agent process(Pred directive, Agent outerContent, Agent innerContent) {
		try {
			Agent newAg = new Agent();
			
			if(directive.getArity() != 2) {
				throw new Exception("CA directive expects two parameters.");
			}
			
			if(innerContent.getPL().size() != 1) {
				throw new Exception("CA directive expects only one inner plan.");
			}
			
			Term agent = directive.getTerm(0);
			Term goal  = directive.getTerm(1);
			
			Plan checkPlan = innerContent.getPL().getPlans().get(0);
			
			if(checkPlan.getTrigger().getLiteral().getFunctor() != "check") {
				throw new Exception("CA directive expects one check inner plan.");
			}
			
			
			// Removed these calls to conform to current version of ASSyntax
//			Pred label 			   = Pred.parsePred("check_2");
//			
//			Trigger trig 		   = ASSyntax.parseTrigger("+?check(A,G)");
//			LogicalFormula context = ASSyntax.parseFormula("true");
//			PlanBody 	  planBody = new PlanBodyImpl();
//			planBody.add(ASSyntax.parsePlanBody(".print(\"Checking agent \",A,\" for goal \", G)"));
//			
//			Plan p = new Plan(label, trig, context, planBody);
			
			Plan p = ASSyntax.parsePlan("@check_2 " +
					                    "+?check(A,G) : true" +
					                    "<- .print(\"Checking agent \",A,\" for goal \", G).");
			newAg.getPL().add(p);
			return newAg;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Directive error.",e);
		}
		return null;
	}

}
