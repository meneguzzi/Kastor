package org.kcl.kastor.agent;

import jason.JasonException;
import jason.architecture.AgArch;
import jason.asSemantics.TransitionSystem;
import jason.bb.BeliefBase;
import jason.runtime.Settings;

import org.kcl.nestor.agent.ModularAgent;

public class KastorAgent extends ModularAgent {
	@Override
	public TransitionSystem initAg(AgArch arch, BeliefBase bb, String asSrc,
			Settings stts) throws JasonException {
		TransitionSystem ts = super.initAg(arch, bb, asSrc, stts);
		
		
		
		return ts;
	}
}
