file://<WORKSPACE>/src/main/scala/npc/IDU.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 2685
uri: file://<WORKSPACE>/src/main/scala/npc/IDU.scala
text:
```scala
package npc

import chisel3._
import chisel3.util.HasBlackBoxResource
import chisel3.util.Decoupled
import chisel3.util.Fill
import chisel3.util.Cat
import java.lang.ModuleLayer.Controller
import javax.sound.sampled.Control


class IDU extends Module{
    val io = IO(new Bundle{
        val in = Flipped(Decoupled(new IFU_IDU))
        val out = Decoupled(new IDU_EXU)
// for write back
        val alu_rsl = Input(UInt(32.W))   
        val dm_out = Input(UInt(32.W))
        val wbu_valid = Input(Bool())
    })

    val Contorller = Module(new CONTORLLER())
    val RegisterFile = Module(new REGISTERFILE())

//Controller
        Contorller.io.inst := io.in.bits.inst
        Contorller.io.rs1 := RegisterFile.io.rd1
        Contorller.io.rs2 := RegisterFile.io.rd2
        io.out.bits.alu_sel := Contorller.io.alu_sel
        io.out.bits.alu_a_sel := Contorller.io.alu_a_sel
        io.out.bits.alu_b_sel := Contorller.io.alu_b_sel
        io.out.bits.pc := io.in.bits.pc
        io.out.bits.len := Contorller.io.len

//RegisterFile
        RegisterFile.io.inst := io.in.bits.inst
        RegisterFile.io.wr_en := Contorller.io.rf_wr_en && io.wbu_valid
        RegisterFile.io.dm_out := io.dm_out
        RegisterFile.io.alu_out := io.alu_rsl
        RegisterFile.io.rf_wr_sel := Contorller.io.rf_wr_sel
        RegisterFile.io.is_csr := Contorller.io.is_csr
        RegisterFile.io.is_ecall := Contorller.io.is_ecall
        RegisterFile.io.pc := io.in.bits.pc
        RegisterFile.io.is_mret := Contorller.io.is_mret
        io.out.bits.rs1 := RegisterFile.io.rd1
        io.out.bits.rs2 := RegisterFile.io.rd2
        io.out.bits.imm := Contorller.io.imm

//for pc
        io.out.bits.jump_jalr := Contorller.io.jump_jalr
        io.out.bits.jump_en := Contorller.io.jump_en
        io.out.bits.imm := Contorller.io.imm
        io.out.bits.is_ecall := Contorller.io.is_ecall
        io.out.bits.is_mret := Contorller.io.is_mret
        io.out.bits.mtvec := RegisterFile.io.mtvec
        io.out.bits.epc := RegisterFile.io.epc

//for isu
        io.out.bits.mem_rd_en := Contorller.io.mem_rd_en
        io.out.bits.mem_wr_en := Contorller.io.mem_wr_en
        io.out.bits.load_unsign := Contorller.io.load_unsign
        io.out.bits.rf_wr_en := Contorller.io.rf_wr_en
        io.out.bits.is_csr := Contorller.io.is_csr

        io.out.bits.is_load := Contorller.io.is_load
        io.out.bits.isS_type := Contorller.io.isS_type
        io.out.bits.is_j := Contorller.io.is_j
//for wbu
        io.out.bits.is_cmp := Contorller.io.is_cmp

    when(io.in.valid){
        io.in.ready := true.B
    }

    when(io.in.valid && io.in.ready){
        io.out.valid := true.B
        io,.@@
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