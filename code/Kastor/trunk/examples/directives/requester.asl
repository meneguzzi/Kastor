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

/* The following code gets created at runtime by executing
   the kastor.asl code for finding partners
*/
/*
{begin ppx(doStuff, sharer, [preA, preB], [stuff]) }

{end}
*/