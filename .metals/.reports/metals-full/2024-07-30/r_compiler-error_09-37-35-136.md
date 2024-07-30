file://<WORKSPACE>/src/main/scala/npc/AXI.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 2708
uri: file://<WORKSPACE>/src/main/scala/npc/AXI.scala
text:
```scala
package npc

import chisel3._
import chisel3.util._

class Date_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk = Input(Clock())
    // AR
    val arvalid     = Input(Bool())
    val araddr      = Input(UInt(32.W))
    val load_unsign = Input(Bool())
    val arready     = Output(Bool())
    // R
    val rdata  = Output(UInt(32.W))
    val rresp  = Output(Bool())
    val rvalid = Output(Bool())
    val rready = Input(Bool())
    // AW    
    val awvalid = Input(Bool())
    val awaddr  = Input(UInt(32.W))
    val awready = Output(Bool())
    // W
    val wvalid = Input(Bool())
    val wdata  = Input(UInt(32.W))
    val len    = Input(UInt(32.W))
    val wready = Output(Bool())
    // B
    val bresp  = Output(Bool())
    val bvalid = Output(Bool())
    val bready = Input(Bool())
  })
  addResource("/Date_Memory.v")
}

class AXI extends Module {
  val io = IO(new Bundle {
    val axi_in  = Flipped(Decoupled(new TO_AXI))
    val axi_out = Decoupled(new FROM_AXI)
  })

  val mem = Module(new Date_Memory())
  mem.io.clk := clock

  // Initializing memory signals
  mem.io.arvalid     := false.B
  mem.io.araddr      := 0.U
  mem.io.load_unsign := false.B
  mem.io.rready      := false.B
  mem.io.awvalid     := false.B
  mem.io.awaddr      := 0.U
  mem.io.wvalid      := false.B
  mem.io.wdata       := 0.U
  mem.io.len         := 0.U
  mem.io.bready      := false.B

  // Initializing output signals
  io.axi_out.bits.arready := false.B
  io.axi_out.bits.rvalid  := false.B
  io.axi_out.bits.rdata   := 0.U
  io.axi_out.bits.rresp   := false.B
  io.axi_out.bits.awready := false.B
  io.axi_out.bits.wready  := false.B
  io.axi_out.bits.bvalid  := false.B
  io.axi_out.bits.bresp   := false.B

  val sIdle :: sRead :: sWriteAddr :: sWriteData :: sResponse :: Nil = Enum(5)
  val state = RegInit(sIdle)

  switch(state) {
    is(sIdle) {
      when(io.axi_in.bits.arvalid) {
        mem.io.arvalid := true.B
        mem.io.araddr := io.axi_in.bits.araddr
        mem.io.load_unsign := io.axi_in.bits.load_unsign
        state := sRead
      }.elsewhen(io.axi_in.bits.awvalid) {
        mem.io.awvalid := true.B
        mem.io.awaddr := io.axi_in.bits.awaddr
        state := sWriteAddr
      }
    }
    is(sRead) {
      when(mem.io.arready) {
        mem.io.arvalid := false.B
        state := sResponse
      }
    }
    is(sWriteAddr) {
      when(mem.io.awready) {
        mem.io.awvalid := false.B
        mem.io.wvalid := true.B
        mem.io.wdata := io.axi_in.bits.wdata
        mem.io.len := io.axi_in.bits.wstrb
        state := sWriteData
      }
    }
    is(sWriteData) {
      when(mem.io.wready) {
        mem.io.wvalid := false.B
        @@
        state := sResponse
      }
    }
    is(sResponse) {
      when(mem.io.rvalid || mem.io.bvalid) {
        io.axi_out.bits.rvalid := mem.io.rvalid
        io.axi_out.bits.rdata := mem.io.rdata
        io.axi_out.bits.rresp := mem.io.rresp
        io.axi_out.bits.bvalid := mem.io.bvalid
        io.axi_out.bits.bresp := mem.io.bresp
        state := sIdle
      }
    }
  }

  io.axi_out.valid := (state === sResponse)
  io.axi_in.ready := (state === sIdle)
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