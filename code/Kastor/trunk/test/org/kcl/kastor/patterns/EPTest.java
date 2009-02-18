package org.kcl.kastor.patterns;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import jason.asSemantics.Agent;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;
import jason.asSyntax.Pred;
import jason.asSyntax.Trigger;
import jason.asSyntax.PlanBody.BodyType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EPTest {
	
	protected EP ep;

	@Before
	public void setUp() throws Exception {
		ep = new EP();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testProcess() {
		Pred directive = Pred.parsePred("ep(doStuff)");
		
		Agent outerContent = new Agent();
		Agent innerContent = new Agent();
		
		Plan p = null;
		try {
			p = ASSyntax.parsePlan("+!doStuff : preA & preB <- .print(\"Doing stuff remotely\").");
			innerContent.getPL().add(p);
		} catch (Exception e) {
			assertTrue(e.toString(),false);
		}
		Agent newAg = ep.process(directive, outerContent, innerContent);
		assertEquals("EP pattern should yield one plan",2, newAg.getPL().size());
		
		//We want to get the newly added plan
		p = newAg.getPL().get("requestdoStuff_0");
		
		Trigger trig = p.getTrigger();
		//Test trigger
		assertEquals("+!requestdoStuff[source(S)]", trig.toString());
		
		LogicalFormula context = p.getContext();
		//Right now we don't have a context, so it must be null
		assertEquals(null,context);
		
		//Now test each step of the new plan
		PlanBody planBody = p.getBody();
		assertEquals("doStuff", planBody.getBodyTerm().toString());
		assertEquals(BodyType.achieve, planBody.getBodyType());
		
		planBody = planBody.getBodyNext();
		//Test planBody
		assertEquals(".send(S,tell,done(doStuff))", planBody.getBodyTerm().toString());
		assertEquals(BodyType.internalAction, planBody.getBodyType());
	}
}
