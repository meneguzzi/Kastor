preA.
preB.

/* Including the plans from Kastor */
{include ("kastor.asl")}

/*Requesting plans from the sharer*/
+timeToRequest : true
	<- !findCooperativePlans.

+startPlan : true
	<- !goal_conj([stuff]).

+!goal_conj([stuff]) : true
	<- !remotedoStuff.

{begin ppx(doStuff, sharer, [preA, preB], [stuff]) }

{end}