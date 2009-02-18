package org.kcl.kastor.patterns;

import static org.junit.Assert.*;
import jason.asSemantics.Agent;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;
import jason.asSyntax.Pred;
import jason.asSyntax.Trigger;
import jason.asSyntax.PlanBody.BodyType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PPXTest {
	
	protected PPX ppx;

	@Before
	public void setUp() throws Exception {
		ppx = new PPX();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testProcess() {
		Pred directive = Pred.parsePred("ppx(doStuff, sharer, [preA, preB], [stuff])");
		Agent outerContent = new Agent();
		Agent innerContent = new Agent();
		
		Agent newAg = ppx.process(directive, outerContent, innerContent);
		assertEquals("PPX should generate only 1 plan", 1, newAg.getPL().size());
		
		Plan p = newAg.getPL().getPlans().get(0);
		
		Trigger trig = p.getTrigger();
		assertEquals("+!remotedoStuff", trig.toString());
		
		LogicalFormula context = p.getContext();
		assertEquals("(preA & preB)", context.toString());
		
		PlanBody planBody = p.getBody();
		assertEquals(".send(sharer,achieve,requestdoStuff)",planBody.getBodyTerm().toString());
		
		planBody = planBody.getBodyNext();
		assertEquals(".wait(\"+done(doStuff)[source(sharer)]\")", planBody.getBodyTerm().toString());
		
		planBody = planBody.getBodyNext();
		assertEquals("stuff",planBody.getBodyTerm().toString());
		assertEquals(BodyType.addBel,planBody.getBodyType());
	}

}
