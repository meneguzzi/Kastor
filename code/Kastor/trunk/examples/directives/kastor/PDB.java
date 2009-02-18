package kastor;

import jason.asSemantics.Agent;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;
import jason.asSyntax.PlanBodyImpl;
import jason.asSyntax.Pred;
import jason.asSyntax.Term;
import jason.asSyntax.PlanBody.BodyType;
import jason.asSyntax.directives.Directive;
import jason.asSyntax.parser.ParseException;

public class PDB implements Directive {

	public Agent process(Pred directive, Agent outerContent, Agent innerContent) {
		Agent newAg = new Agent();
		try {
			for (Plan plan : innerContent.getPL()) {

				Literal lStart = ASSyntax.parseLiteral(".print(\"Start\")");
				Literal lEnd = ASSyntax.parseLiteral(".print(\"End\")");
				
				plan.getBody().add(0, new PlanBodyImpl(BodyType.internalAction, lStart));
				plan.getBody().add(new PlanBodyImpl(BodyType.internalAction, lEnd));
				
				newAg.getPL().add(plan);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newAg;
	}

}
