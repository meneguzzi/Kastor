preA.
preB.

/* Including the plans from Kastor */
{include ("kastor.asl")}

/*
+!requestdoStuff [source(S)] : true
	<- .print("Request from source: ",S).
	*/

/* Sharer has one external plan, with it we must send 
   the declarative information, which is specified below*/
ep(doStuff, [preA, preB], [stuff]).
{begin ep(doStuff)}
+!doStuff : preA & preB
	<- .print("Doing stuff remotely").
{end}