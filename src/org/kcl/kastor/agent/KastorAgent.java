package org.kcl.kastor.agent;

import jason.JasonException;
import jason.asSemantics.TransitionSystem;

import org.kcl.nestor.agent.ModularAgent;

public class KastorAgent extends ModularAgent {
	
	@Override
	public TransitionSystem initAg(String asSrc) throws JasonException {
		TransitionSystem ts = super.initAg(asSrc);
		
		return ts;
	}
}
