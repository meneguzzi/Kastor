{include("print.asl")}
{include("domain.asl")}
{include("stats.asl")}
//-----------------------------------------------------------------------------
//                   Plans to trigger processing of blocks
//-----------------------------------------------------------------------------

+over(Block, feedBelt1)[source(self)] : true
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
@action4(block, procUnit)
+!processWest(Block, ProcUnit) : over(Block, ProcUnit) & west(ProcUnit)
   <- .print("Processing ",Block," in ",ProcUnit);
      //.wait(50);
      +processed(Block, ProcUnit);
      process(Block, ProcUnit).

@action5(block)
+!consume(Block) : over(Block,depositBelt)
   <- .print("Consuming ",Block);
      //.wait(50);
      -over(Block, depositBelt);
      +empty(depositBelt);
      +finished(Block);
      consume(Block).

@action6(block, device, device)
+!moveWest(Block, Device1, Device2) 
   : over(Block,Device1) & empty(Device2) & west(Device1) & west(Device2)
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

shared([action4,
        action5,
        action6]).

+?sharedOperators(Plans) : true
   <- //.print(S," asked for my operators.");
      ?shared(Ops);
      !getSharedPlans(Ops,[],P);
      !print("Shared plans: ",Plans).

+!getSharedPlans([], Plans, Plans) : true
   <- !print("Shared plans: ",Plans);
      -+sharedPlans(Plans);
      true.

+!getSharedPlans([Op | Ops], PlansSoFar, PlansR) : true
   <- !print("Getting plan for label ",Op);
      .plan_label(Plan,Op);
      !print("Plan is ",Plan);
      !getSharedPlans(Ops, [Plan | PlansSoFar], Plans);
      PlansR = Plans;
      !print("Shared plans: ",Plans);
      true.

//{include("controllerActions.asl")}
{include("peleus.asl")}