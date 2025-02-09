package codegen

import analyser.SymTree._
import analyser.SymTree
import utils.Context
import netlogo.Breed
import netlogo.{LinkedFunction, Function, BaseFunction}
import netlogo.Variable
import netlogo.Types._
import netlogo.Type
import utils.Reporter

object ToolGen{
    def generateSwitchInstruction(breed: Breed, indexValue: String, functions: Iterable[LinkedFunction], flag: Flag, ending: Instruction = EmptyInstruction)(implicit context: Context):Instruction = {
        if (functions.isEmpty){
            EmptyInstruction
        }
        else {
            val lst = functions.map(f => 
                InstructionList(
                    InstructionGen(f"//${f.name}"),
                    InstructionCompose(f"if ($indexValue == ${f.lambdaIndex})", 
                        InstructionBlock(
                            ContentGen.generate(f.symTree)(f, breed, context, flag),
                            ending
                        )
                    )
                )
            )
            InstructionList(lst.toList)
        }
    }
}