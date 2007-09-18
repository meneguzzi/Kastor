{include("print.asl")}
{include("domain.asl")}
{include("stats.asl")}

logging(false).
//logging(true).

//-----------------------------------------------------------------------------
//                   Plans to trigger processing of blocks
//-----------------------------------------------------------------------------

+over(Block, feedBelt1)[source(self)] : true
	<- 	.print("Processing ",Block);
		org.soton.peleus.act.time_in_millis(Time1);
		!finish(Block);
		!writeStats(Time1).

//-----------------------------------------------------------------------------
//                    West Controller Actions
//-----------------------------------------------------------------------------
@action1(block, procUnit)
+!processWest(Block, ProcUnit)
   : over(Block, ProcUnit) & west(ProcUnit)
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

//--------------------------
// Exported plans
//--------------------------
+!requestProcessWest(Block, ProcUnit) [source(S)] : true
   <- .print(S," asked me to process ",Block," in ",ProcUnit);
      !processWest(Block, ProcUnit);
      !print("Informing ",S," that I acted.");
      .send(S,tell,done(processWest));
      !print("Message sent: ",done(processWest));
      true.

+!requestMoveWest(Block, Device1, Device2) [source(S)] : true
   <- .print(S," asked me to move ",Block," from ",Device1," to ",Device2);
      !moveWest(Block, Device1, Device2);
      !print("Informing ",S," that I acted.");
      .send(S,tell,done(moveWest));
      !print("Message sent: ",done(moveWest));
      true.

//--------------------------
// Imported plans
//--------------------------

//A debugging plan to check if the done message was received
+done(Act) [source(S)] : true
   <- !print("Got message ", done(Act)[source(S)]);
      -done(Act) [source(S)];
      true.

//A test of the remote action to process a block
@action4(block, procUnit)[atomic,remote]
+!remoteProcessEast(Block, ProcUnit) : over(Block, ProcUnit) & east(ProcUnit)
   <- .print("Asking ",controllerWest," to process ",Block," in ",ProcUnit);
      .send(controllerEast,achieve,requestProcessEast(Block, ProcUnit));
      !print("Waiting for ",controllerEast," to act.");
      .wait("+done(processEast)[source(controllerEast)]");
      !print(done(processEast(Block, ProcUnit))[source(controllerEast)]);
      +processed(Block, ProcUnit);
      true.

//A test of the remote action to move a block
@action6(block, device, device)[atomic,remote]
+!remoteMoveEast(Block, Device1, Device2)
   : over(Block,Device1) & empty(Device2) & east(Device1) & east(Device2)
   <- .print("Asking ",controllerEast, " to move ",Block," from ",Device1," to ",Device2);
      .send(controllerEast,achieve,requestMoveEast(Block, Device1, Device2));
      !print("Waiting for ",controllerEast," to act.");
      .wait("+done(moveEast)[source(controllerEast)]");
      !print(done(moveEast(Block, Device1, Device2))[source(controllerEast)]);
      +over(Block, Device2);
      -over(Block, Device1);
      -empty(Device2);
      +empty(Device1);
      true.

//-----------------------------------------------------------------------------

+!gatherSharedOperators : true
   <- !print("Gathering shared operators");
      .send(controllerWest,askOne,sharedOperators(S),A);
      !print("Answer was: ",A);
      A = sharedOperators(Plans);
      !print("Adding shared operators: ",Plans);
      !addSharedOperators(Plans);
      !print("Operators added.");
      true.

/*shared([action4,
        action5,
        action6]).*/

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