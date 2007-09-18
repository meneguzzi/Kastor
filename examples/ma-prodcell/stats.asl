//-----------------------------------------------------------------------------
//                        Statistics Gathering Fluents 
//-----------------------------------------------------------------------------
totalTime(0).
totalBlocks(0).

+!writeStats(Time1) : true
   <- org.soton.peleus.act.time_in_millis(Time2);
      PlanningTime = Time2 - Time1;
      .print("Planning took ",PlanningTime," miliseconds");
      ?totalTime(T);
      TotalTime = T + PlanningTime;
      -+totalTime(TotalTime).

//-----------------------------------------------------------------------------
//Plans to wrap up the simulation
//-----------------------------------------------------------------------------

/** To stop the simulation and kill the agent*/
+endSimulation : true
	<- !endSimulation.

+!endSimulation : .my_name(controllerWest)
	<- .print("Simulation is over, writing stats.");
	   ?totalBlocks(B);
	   .print("Processed ",B," blocks");
	   ?totalTime(T);
	   .print("Total time planning was ",T," milliseconds");
	   stats.recordStats("statsWest",B,T);
	   .wait(1000);
	   .send(controllerEast,tell,endSimulation(west));
	   true.

+!endSimulation : true
	<- .print("Simulation is over, stopping MAS.");
	   ?totalBlocks(B);
	   .print("Processed ",B," blocks");
	   ?totalTime(T);
	   .print("Total time planning was ",T," milliseconds");
	   stats.recordStats("statsEast",B,T);
	   .wait("+endSimulation(west)[source(controllerWest)]");
	   .stopMAS;
	   true.
