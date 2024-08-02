package npc
import chisel3._
import chisel3.stage._
import circt.stage.ChiselStage
import chisel3.util._


object Elaborate extends App{
    var filltlflag = Array[String]()
    var firtoolOpts = Array[String]()

    filltlflag = filltlflag ++ Array(
        "--target-dir", "generated",
        "--target:verilog"
        )
    firtoolOpts = firtoolOpts ++ Array(
        "--lowering-options=" + Seq(
            // "locationInfoStyle=wrapWithLineInfo",
            // "disallowLocalVariables",
            // "disallowPackedArrays"
            // "mitigateVivadoArrayIndexConstPropBug"
        ).mkString(","),
        "--disable-all-randomization"
    )

    ChiselStage.emitSystemVerilogFile(
        new top,
        filltlflag,
        firtoolOpts
    )
}

// object Elaborate extends App {
//   val usage = ""

//   var firrtlOpts  = Array[String]()
//   var firtoolOpts = Array[String]()

//   type OptionMap = Map[Symbol, Any]
//   def parseArgs(args: Array[String]) = {
//     def nextOption(options: OptionMap, argList: List[String]): OptionMap = {
//       argList match {
//         case Nil                             => options
//         case "--target-dir" :: value :: tail => nextOption(options ++ Map { Symbol("targetDir") -> value }, tail)
//         case "--release" :: tail =>
//           options ++ Map { Symbol("release") -> true }
//           nextOption(options, tail)
//         case option :: tail =>
//           firrtlOpts :+= option
//           nextOption(options, tail)
//       }
//     }
//     nextOption(Map(), args.toList)
//   }

//   if (args.length == 0) println(usage)
//   val options = parseArgs(args)
//   def getArg(key: String) = options.getOrElse(Symbol(key), None)

//   firrtlOpts = firrtlOpts ++ Array(
//     "-td=" + getArg("targetDir"),
//     "--target:verilog",
//     // "--split-verilog",
//     "--preserve-aggregate=vec"
//   )
//   firtoolOpts = firtoolOpts ++ Array(
//     "-O=" + (if (getArg("release") == true) "release" else "debug"),
//     "--lowering-options=" + Seq(
//       "locationInfoStyle=wrapInAtSquareBracket",
//       "disallowLocalVariables",
//       "disallowPackedArrays",
//       "mitigateVivadoArrayIndexConstPropBug"
//     ).mkString(","),
//     "--disable-all-randomization"
//   )

//   ChiselStage.emitSystemVerilogFile(
//     new top,
//     firrtlOpts,
//     firtoolOpts
//   )
// }