package org.kcl.kastor.patterns;

import jason.asSemantics.Agent;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
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

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A proxy plan pattern PPX for an external plan <g, a, P, E>, where 
 * P = {p0 , . . . , pn}, and E = {b0 , . . . , bm} (and bi are 
 * belief additions or deletions)
 * 
 * <table border="1">
 *  <tr> <td>+!g : p0 & . . . &pn <-b0 ; . . . ; bm.</td> </tr>
 *  <tr> <td>+!remoteG : p0 & . . . &pn &ready(a, g) <br/>
 *           <- .send(a, achieve, requestG); <br/>
 *              .wait(done(g));  <br/>
 *              b0 ; . . . ; bm. <br/>
 *              +!check(a, g) : true <br/>
 *           <- +ready(a, g).
 *    </td> </tr>
 * </table>
 * 
 * @author meneguzzi
 *
 */
public class PPX implements Directive {
	
	static Logger logger = Logger.getLogger(PPX.class.getName());

	public Agent process(Pred directive, Agent outerContent, Agent innerContent) {
		try {
			Agent newAg = new Agent();
			
			//Unpack the directive's parameters
			Term remoteGoal  = directive.getTerm(0);
			Term targetAgent = directive.getTerm(1);
			ListTerm preconds= (ListTerm) directive.getTerm(2);
			ListTerm effects = (ListTerm) directive.getTerm(3);
			
			//Then create the trigger for the new plan appending "remote" to the original goal name
			Literal trigLiteral = ASSyntax.parseLiteral("remote"+remoteGoal.toString());
			Trigger trig = new Trigger(TEOperator.add, TEType.achieve, trigLiteral);
			
			//The label is the original goal plus it's arity
			Pred label = Pred.parsePred(trigLiteral.getFunctor()+"_"+trigLiteral.getArity());
			
			//The context is the conjunction of all preconditions
			StringBuilder builder = new StringBuilder();
			for (Iterator<Term> i=preconds.iterator(); i.hasNext(); ) {
				Term term = i.next();
				builder.append(term);
				if(i.hasNext()) {
					builder.append(" & ");
				}
			}
			
			LogicalFormula context = ASSyntax.parseFormula(builder.toString());
			
			PlanBodyImpl planBody = new PlanBodyImpl();
			
			planBody.add(new PlanBodyImpl(BodyType.action, ASSyntax.parseTerm(".send("+targetAgent+","+"achieve,"+"request"+remoteGoal+")")));
			planBody.add(new PlanBodyImpl(BodyType.action, ASSyntax.parseTerm(".wait(\"+done("+remoteGoal+")[source("+targetAgent+")]\")")));
			
			for (Term term : effects) {
				planBody.add(new PlanBodyImpl(BodyType.addBel, term));
			}
			
			Plan p = new Plan(label,trig,context,planBody);
			
			newAg.getPL().add(p);
			
			return newAg;
		} catch(ClassCastException cce) {
			logger.log(Level.SEVERE,"Directive error.", cce);
		} catch(Exception e) {
			logger.log(Level.SEVERE,"Directive error.", e);
		}
		return null;
	}

}
