{include("print.asl")}
{include("domain.asl")}
{include("stats.asl")}
logging(true).
//-----------------------------------------------------------------------------
//                   Plans to trigger processing of blocks
//-----------------------------------------------------------------------------

+over(Block, feedBelt2)[source(self)] : true
	<- 	.print("Processing ",Block);
		org.soton.peleus.act.time_in_millis(Time1);
		!finish(Block);
		!writeStats(Time1).

//---------------------------------------
//Cleanup of unnecessary beliefs
//---------------------------------------
+finished(Block) : object(block,Block)
	<-  -object(block,Block)[source(self)];
	   -type(Block,_)[source(self)];
	   -finished(Block)[source(self)];
	   .abolish(processed(Block,_)[source(self)]);
	   ?totalBlocks(B);
	   -+totalBlocks(B+1);
	   .print("Cleaned up beliefs about ", Block).


//-----------------------------------------------------------------------------
//                    East Controller Actions
//-----------------------------------------------------------------------------
@action1(block, procUnit)
+!processEast(Block, ProcUnit) : over(Block, ProcUnit) & east(ProcUnit)
   <- .print("Processing ",Block," in ",ProcUnit);
      //.wait(50);
      +processed(Block, ProcUnit);
      process(Block, ProcUnit).

@action2(block)
+!consume(Block) : over(Block,depositBelt)
   <- .print("Consuming ",Block);
      //.wait(50);
      -over(Block, depositBelt);
      +empty(depositBelt);
      +finished(Block);
      consume(Block).

@action3(block, device, device)
+!moveEast(Block, Device1, Device2) 
   : over(Block,Device1) & empty(Device2) & east(Device1) & east(Device2)
   <- .print("Moving ",Block," from ",Device1," to ",Device2);
      //.wait(50);
      +over(Block, Device2);
      -over(Block, Device1);
      -empty(Device2);
      +empty(Device1);
      move(Block, Device1, Device2).

//-----------------------------------------------------------------------------
//                 Plans to act along with multiple agents
//-----------------------------------------------------------------------------

-!goalConj(Goals) : not tried
	<- .print("Failed planning for Goals ",Goals,", will try another strategy");
	   +tried;
	   !gatherSharedOperators.

/*shared([action1,
         action2,
         action3]).*/

//A test of the remote action to process a block
@action4(block, procUnit)[atomic]
+!remoteProcessWest(Block, ProcUnit) : over(Block, ProcUnit) & west(ProcUnit)
   <- .print("Asking ",controllerWest," to process ",Block," in ",ProcUnit);
      .send(controllerWest,achieve,requestProcessWest(Block, ProcUnit));
      !print("Waiting for ",controllerWest," to act.");
      .wait("+done(processWest)[source(controllerWest)]");
      !print(done(processWest(Block, ProcUnit))[source(controllerWest)]);
      +processed(Block, ProcUnit);
      true.

//A test of the remote action to move a block
@action6(block, device, device)[atomic]
+!remoteMoveWest(Block, Device1, Device2)
   : over(Block,Device1) & empty(Device2) & west(Device1) & west(Device2)
   <- .print("Asking ",controllerWest, " to move ",Block," from ",Device1," to ",Device2);
      .send(controllerWest,achieve,requestMoveWest(Block, Device1, Device2));
      !print("Waiting for ",controllerWest," to act.");
      .wait("+done(moveWest)[source(controllerWest)]");
      !print(done(moveWest(Block, Device1, Device2))[source(controllerWest)]);
      +over(Block, Device2);
      -over(Block, Device1);
      -empty(Device2);
      +empty(Device1);
      true.

//A debugging plan to check if the done message was received
+done(Act) [source(S)] : true
   <- .print("Got message ", done(Act)[source(S)]);
      true.

+!gatherSharedOperators : true
   <- !print("Gathering shared operators");
      .send(controllerWest,askOne,sharedOperators(S),A);
      !print("Answer was: ",A);
      A = sharedOperators(Plans);
      !print("Adding shared operators: ",Plans);
      !addSharedOperators(Plans);
      !print("Operators added.");
      true.

+!addSharedOperators([]) : true
   <- !print("Done").

+!addSharedOperators([Plan | Plans]) : true
   <- .add_plan(Plan);
      !addSharedOperators(Plans).
//{include("controllerActions.asl")}
{include("peleus.asl")}