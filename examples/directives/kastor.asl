
//Default is to cooperate (I could have a list of agents that I cooperate in)
+?cooperate(S) : true <- .print("I'm cooperating with ",S).

+?cooperate(S) : false.

/* Algorithm to find cooperation partners  */
/* This implementation simply broadcasts a */
/* request for plans to all agents         */
+!findCooperativePlans : true
	<- .print("Requesting plans from all agents");
	   .broadcast(achieve, sendPlans);
	   .print("Awaiting replies").

/* Plan to receive the replies from sharing agents.    */
/* Like the previous plan, we want to make sure we are */
/* cooperating with whoever is sending us the plans.   */
+!receivePlans(PlanInfo)[source(S)] : true
	<- ?cooperate(S);
	   .print("Received from ",S," the following plans: ",PlanInfo," adding plans to agent.");
	   for(.member(X,PlanInfo)) {
	   	!processPlanPattern(X)
	   };
	   .print("Added all external plans").

+!processPlanPattern(PlanInfo) : true
	<- act.plan_puts("{begin #{PlanInfo} } {end}",PlanPattern);
	   org.kcl.kastor.act.add_plan_pattern(PlanPattern);
	   .print("Added plan pattern ",PlanInfo).

/* A plan to respond to requests for plans, the first step */
/* checks whether we are cooperating with the requester    */
+!sendPlans[source(S)] : .my_name(Name)
	<- ?cooperate(S);
	   //We want to find all 
	   .findall(ppx(Goal,Name,Pre,Eff),
	            ep(Goal,Pre,Eff),
	            Plans);
	   .print("Sending cooperative plans to ",S);
	   .send(S,achieve,receivePlans(Plans)).