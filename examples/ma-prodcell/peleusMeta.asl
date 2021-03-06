//-----------------------------------------------------------------------------
//               Plan that invokes the Planner
//                          First single agent
//-----------------------------------------------------------------------------

+!goalConj(Goals) : true
	<- org.soton.peleus.act.plan(Goals,[maxSteps(10),makeGeneric(true),makeAtomic(true)]);
	   .print("Goals ",Goals," were satisfied").

+!goalConj(Goals,useRemote(true)) : true
	<- .print("Trying to cooperate to solve the problem");
	   org.soton.peleus.act.plan(Goals,[useRemote(true)]);
	   .print("Goals ",Goals," were satisfied using the team").

-!goalConj(Goals) : true
	<- .print("Failed planning for Goals ",Goals,", will try another strategy");
	   !goalConj(Goals,useRemote(true)).

-!goalConj(Goals, useRemote(true)) : true
    <- .print("Goals failed as a team").
	   