file://<WORKSPACE>/src/main/scala/npc/IFU.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1081
uri: file://<WORKSPACE>/src/main/scala/npc/IFU.scala
text:
```scala
package npc

import chisel3._
import chisel3.util._


class IFU extends  Module{
    val io = IO(new Bundle {
        val pc        = Input(UInt(32.W))
        val out       = Decoupled(new IFU_IDU)
        val axi_out   = Decoupled(new TO_AXI)
        val axi_in    = Flipped(Decoupled(new FROM_AXI))
        val diff_test = Output(Bool())
    })
    io.diff_test := false.B

//initial
    io.axi_out.bits.load_unsign := false.B
    io.axi_out.bits.arvalid     := false.B
    io.axi_out.bits.rready      := true.B
    io.axi_out.bits.awaddr      := 0.U
    io.axi_out.bits.wdata       := 0.U
    io.axi_out.bits.wstrb       := 0.U
    io.axi_out.bits.awvalid     := false.B
    io.axi_out.bits.bready      := false.B
    io.axi_out.bits.wvalid      := false.B
    io.axi_out.bits.araddr      := io.pc

    val IPC = RegInit(0.U(32.W))
    val IR  = RegInit(0.U(32.W))

    val sIdle :: sFetch :: sValid :: sGetinst :: Nil = Enum(4)
    val state = RegInit(sIdle)

    switch(state) {
      is(sIdle) {
        when(io.pc =/= IPC) {
            when(io.axi_in.ready){  //AXI ready,@@
                io.axi_out.bits.arvalid := true.B
                io.diff_test            := true.B
                state                   := sFetch
            }
        }
      }
      is(sFetch) {
        when(io.axi_out.bits.arvalid && io.axi_in.bits.arready) {
          io.axi_out.bits.arvalid := false.B
          state                   := sGetinst
        }
      }
      is(sGetinst) {
        when(io.axi_out.bits.rready && io.axi_in.bits.rvalid) {
          when(io.axi_in.bits.rresp) {
            IR    := io.axi_in.bits.rdata
            IPC   := io.pc
            state := sValid
          }
        }
      }
      is(sValid){
          when(io.out.ready){
              state := sIdle
          }
      }
    }

    io.axi_out.valid := (state === sFetch)
    io.axi_in.ready  := true.B
    
    io.out.valid     := (state === sValid)
    io.out.bits.pc   := IPC
    io.out.bits.inst := IR
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