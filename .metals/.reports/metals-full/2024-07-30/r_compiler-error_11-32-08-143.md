file://<WORKSPACE>/src/main/scala/npc/ISU.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1005
uri: file://<WORKSPACE>/src/main/scala/npc/ISU.scala
text:
```scala
package npc

import chisel3._
import chisel3.util._


class ISU extends Module {
  val io = IO(new Bundle {
    val in        = Flipped(Decoupled(new EXU_ISU))
    val out       = Decoupled(new ISU_WBU)
    val axi_out   = Decoupled(new TO_AXI)
    val axi_in    = Flipped(Decoupled(new FROM_AXI))
    val wbu_valid = Input(Bool())
  })
  
  val store_finish = RegInit(false.B)
  val load_finish  = RegInit(false.B)

    // Initializing axi_out signals
  io.axi_out.bits.arvalid := false.B
  io.axi_out.bits.rready  := true.B
  io.axi_out.bits.awvalid := false.B
  io.axi_out.bits.wvalid  := false.B
  io.axi_out.bits.bready  := true.B

  io.axi_out.bits.wstrb       := io.in.bits.len
  io.axi_out.bits.load_unsign := io.in.bits.load_unsign
  io.axi_out.bits.araddr      := io.in.bits.alu_out
  io.axi_out.bits.awaddr      := io.in.bits.alu_out
  io.axi_out.bits.wdata       := io.in.bits.data

    // State Machine for load/store operations
  when(io.in.valid){
    when(io.in.bits.is_load){
      when()@@
    }
  }

    // For WBU
  io.out.bits.dm_out       := io.axi_in.bits.rdata
  io.out.bits.alu_out      := io.in.bits.alu_out
  io.out.bits.jump_jalr    := io.in.bits.jump_jalr
  io.out.bits.jump_en      := io.in.bits.jump_en
  io.out.bits.imm          := io.in.bits.imm
  io.out.bits.is_ecall     := io.in.bits.is_ecall
  io.out.bits.is_mret      := io.in.bits.is_mret
  io.out.bits.mtvec        := io.in.bits.mtvec
  io.out.bits.epc          := io.in.bits.epc
  io.out.bits.rd1          := io.in.bits.rd1
  io.out.bits.mem_rd_en    := io.in.bits.mem_rd_en
  io.out.bits.mem_wr_en    := io.in.bits.mem_wr_en
  io.out.bits.rf_wr_en     := io.in.bits.rf_wr_en
  io.out.bits.load_finish  := load_finish
  io.out.bits.store_finish := store_finish
  io.out.bits.is_csr       := io.in.bits.is_csr
  
    // For WBU
  io.out.bits.is_cmp   := io.in.bits.is_cmp
  io.out.bits.is_load  := io.in.bits.is_load
  io.out.bits.isS_type := io.in.bits.isS_type

  io.out.bits.is_j := io.in.bits.is_j
  io.out.bits.pc   := io.in.bits.pc

  io.axi_out.valid := (state =/= sValid)
  io.axi_in.ready  := true.B
  io.in.ready      := (state === sIdle)
  io.out.valid     := (state === sValid)

// State Machine
  val sIdle :: sValid :: Nil = Enum(2)
  val state = RegInit(sIdle)    
  switch(state) {
    is(sIdle) {
      when(io.in.valid) {
        state := sValid
      }
    }
    is(sValid) {
        state := sIdle
      }
    }   
  io.in.ready := (state === sIdle)
  io.out.valid := (state === sValid)
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