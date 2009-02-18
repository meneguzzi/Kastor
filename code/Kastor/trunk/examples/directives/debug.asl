test.

+test : org.kcl.kastor.act.agent(sharer)
	<- .print(sharer, " exists in the domain").

{begin pdb}
+a : true
	<- .print("Doing something");
	   !b.

+!b : true
	<- .print("Doing something else").
{end}