preA.
preB.

/*
+!requestdoStuff [source(S)] : true
	<- .print("Request from source: ",S).
	*/

{begin ep(doStuff)}
+!doStuff : preA & preB
	<- .print("Doing stuff remotely").
{end}