file://<WORKSPACE>/src/main/scala/npc/WBU.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1144
uri: file://<WORKSPACE>/src/main/scala/npc/WBU.scala
text:
```scala
package npc

import chisel3._
import chisel3.util._

class WBU extends Module{
    val io = IO(new Bundle{
        val in = Flipped(Decoupled(new ISU_WBU))
        val out = Decoupled(new WBU_PC)
        val dm_out = Output(UInt(32.W))
        val alu_out = Output(UInt(32.W))
        val wbu_valid = Output(Bool())
    })

//for regfiles
    io.dm_out := io.in.bits.dm_out
    io.alu_out := io.in.bits.alu_out
    val old_pc = RegInit(0.U(32.W))
    io.wbu_valid := false.B

    
    when(io.in.bits.is_load){
        io.wbu_valid := io.in.bits.finish_load
    }.elsewhen(io.in.bits.isS_type){
        io.wbu_valid := io.in.bits.can_wirte
    }.otherwise{
        io.wbu_valid := io.in.bits.is_cmp || io.in.bits.rf_wr_en || io.in.bits.is_j
    }    



//for pc
    io.out.bits.imm := io.in.bits.imm
    io.out.bits.jump_en := io.in.bits.jump_en
    io.out.bits.jump_jalr := io.in.bits.jump_jalr
    io.out.bits.is_ecall := io.in.bits.is_ecall
    io.out.bits.is_mret := io.in.bits.is_mret
    io.out.bits.mtvec := io.in.bits.mtvec
    io.out.bits.epc := io.in.bits.epc
    io.out.bits.rd1 := io.in.bits.rd1

    when(io.in.valid){
        io,@@
    }
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
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:435)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: 0