package org.kcl.kastor.act;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.Set;

/**
<p>Internal action: <b><code>org.kcl.kastor.act.concat</code></b>.

<p>Description: checks whether the argument is an agent in the same environment
				as the requesting agent. 

<p>Parameters:<ul>
<li>+ arg (any ground term): the name of the agent to be checked.<br/>
</ul>

<p>Examples:<ul>
<li> <code>org.kcl.kastor.act.concat(dude)</code>: true if an agent named <code>dude</code> is in the environment.
</ul>

*/
public class agent extends DefaultInternalAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public int getMinArgs() {return 1;}
	
	@Override
	public int getMaxArgs() {return 1;}
	

	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		checkArguments(args);
		String agentName = args[0].toString();
		//First unpack the parameter
		Set<String> agents = ts.getUserAgArch().getArchInfraTier().getRuntimeServices().getAgentsNames();
		
		for (String agent : agents) {
			if(agent.equals(agentName))
				return true;
		}
		
		return false;
	}

}
