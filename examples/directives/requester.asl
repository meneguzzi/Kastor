preA.
preB.

{begin pdb}
+startPlan : true
	<- !goal_conj([stuff]).
{end}

+!goal_conj([stuff]) : true
	<- !remotedoStuff.

{begin ppx(doStuff, sharer, [preA, preB], [stuff]) }

{end}