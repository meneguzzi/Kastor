//-----------------------------------------------------------------------------
//                Domain-related Instructions to Graphplan
//-----------------------------------------------------------------------------

procUnit(pu1).
procUnit(pu2).
procUnit(pu3).
procUnit(pu4).
device(pu1).
device(pu2).
device(pu3).
device(pu4).
device(depositBelt).
device(transferBelt).
device(feedBelt1).
device(feedBelt2).

west(feedBelt1).
west(pu1).
west(pu2).
west(depositBelt).
west(transferBelt).


east(feedBelt2).
east(pu3).
east(pu4).
east(depositBelt).
east(transferBelt).

//-----------------------------------------------------------------------------
//             Plans to update the Belief Base and optimise testing
//-----------------------------------------------------------------------------
+empty(Device) [source(percept)] : true
	<- +empty(Device).

+procUnit(ProcUnit) [source(percept)] : not procUnit(ProcUnit) [source(self)]
   <- +procUnit(ProcUnit).

+device(Device) [source(percept)] : not device(Device) [source(self)]
   <- +device(Device).

+block(Block) [source(percept)] : not block(Block) [source(self)]
   <- +block(Block).

+type(Block, Type) [source(percept)] : not type(Block, Type) [source(self)]
	<- +type(Block, Type).

+over(Object, Device) [source(percept)] : not over(Object, Device)[source(self)]
	<-  //.print("Acknowledging ",over(Object, Device));
		+over(Object, Device).

//---------------------------------------
//Cleanup of unnecessary beliefs
//---------------------------------------
+finished(Block) : block(Block)
   <- -block(Block)[source(self)];
      -type(Block,_)[source(self)];
      -finished(Block)[source(self)];
      .abolish(processed(Block,_)[source(self)]);
      ?totalBlocks(B);
      -+totalBlocks(B+1);
      .print("Cleaned up beliefs about ", Block).


//-----------------------------------------------------------------------------
//               Instructions on how to finish processing blocks
//-----------------------------------------------------------------------------

+!finish(Block) : type(Block, type1)
	<- !goalConj([processed(Block, pu1), 
	         //processed(Block, pu2), 
	         //processed(Block, pu3), 
	         finished(Block)]).

+!finish(Block) : type(Block, type2)
	<- !goalConj([processed(Block, pu3), 
	         //processed(Block, pu4), 
	         finished(Block)]).

+!finish(Block) : type(Block, type3)
	<- !goalConj([processed(Block, pu1), 
	         processed(Block, pu3), 
	         finished(Block)]).