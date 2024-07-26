file://<WORKSPACE>/src/main/scala/npc/top.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 678
uri: file://<WORKSPACE>/src/main/scala/npc/top.scala
text:
```scala
package npc

import chisel3._
import chisel3.stage._
import circt.stage.ChiselStage
import chisel3.util._

class top extends Module{
    val io = IO(new Bundle {
        val pc = Output(UInt(32.W))
        val alu_rsl = Output(UInt(32.W))
        val inst =  Output(UInt(32.W))
        val imm = Output(UInt(32.W))
        val diff_test = Output(Bool())

    })

    val ifu = Module(new IFU)
    val idu = Module(new IDU)
    val exu = Module(new EXU)
    val isu = Module(new ISU)
    val wbu = Module(new WBU)
    val pc = Module(new PC)

    StageConnect(ifu.io.out, idu.io.in)
    StageConnect(idu.io.out, exu.io.in)
    StageConnect(exu.io.out, pc.io.in)
    StageConnect(@@)

    
    ifu.io.pc := pc.io.next_pc

    idu.io.alu_rsl := exu.io.alu_rsl
    
    
    


//for sdb    
    io.pc := ifu.io.out.bits.pc
    io.alu_rsl := exu.io.alu_rsl
    io.inst := ifu.io.out.bits.inst
    io.imm := idu.io.out.bits.imm
    io.diff_test := ifu.io.diff_test
}

object StageConnect {
  def apply[T <: Data](left: DecoupledIO[T], right: DecoupledIO[T]) = {
    val arch = "multi"
    if      (arch == "single")   { right.bits := left.bits }
    else if (arch == "multi")    { right <> left }
    else if (arch == "pipeline") { right <> RegEnable(left, left.fire) }
    else if (arch == "ooo")      { right <> Queue(left, 16) }
  }
}



object top extends App{
    var filltlflag = Array[String]()
    filltlflag = filltlflag ++ Array(
        "--target-dir", "generated",
        "--target:verilog",
        // "--split-verilog",
        // "--lowering-options=" + Seq(
        //     "disallowLocalVariables",
        //     "disallowPackedArrays"
        // ).mkString(","),
        // "--disable-all-randomization"
        )

    ChiselStage.emitSystemVerilogFile(
        new top,
        filltlflag
    )
}



```



#### Error stacktrace:

```
scala.collection.LinearSeqOps.apply(LinearSeq.scala:131)
	scala.collection.LinearSeqOps.apply$(LinearSeq.scala:128)
	scala.collection.immutable.List.apply(List.scala:79)
	dotty.tools.dotc.util.Signatures$.countParams(Signatures.scala:501)
	dotty.tools.dotc.util.Signatures$.applyCallInfo(Signatures.scala:186)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:94)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:63)
	scala.meta.internal.pc.MetalsSignatures$.signatures(MetalsSignatures.scala:17)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:51)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:426)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: 0