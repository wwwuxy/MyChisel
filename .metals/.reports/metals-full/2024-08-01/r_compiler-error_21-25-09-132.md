file://<WORKSPACE>/src/main/scala/npc/Uart.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 979
uri: file://<WORKSPACE>/src/main/scala/npc/Uart.scala
text:
```scala
package npc

import chisel3._
import chisel3.util._

class AxiLiteSlave extends Module {
  val io = IO(new Bundle {
    in  = Flipped(Decoupled(TO_AXI))
    out = Decoupled(FROM_AXI)
    })
  
// Device register to store data
    val deviceReg = RegInit(0.U(32.W))
  
// Default values for outputs
    io.out.bits.arready = true.B

    val rresp   = RegInit(false.B)
    val rvalid  = RegInit(false.B)
    val awready = RegInit(false.B)
    val wready  = RegInit(false.B)
    val bresp   = RegInit(false.B)
    val bvalid  = RegInit(false.B)
  
    io.out.bits.rresp   := rresp
    io.out.bits.rvalid  := rvalid
    io.out.bits.bresp   := bresp
    io.out.bits.bvalid  := bvalid
    io.out.bits.wready  := wready
    io.out.bits.awready := awready
  
// Read data handshake
  when(io.out.bits.arready && io.in.bits.arvalid) {
    io.axi.rdata := deviceReg
    io.axi.rvalid := true.B
    when(io.in.bits.araddr < "h8000_0000".U || io.in.bits.araddr > "h87ff_ffff".U){
        io,@@
    }
    io.axi.rresp := 1.U // 'OKAY' response
  }

// Write address handshake
  when(io.in.bits.awvalid && !io.axi.awready) {
    io.axi.awready := true.B
  }
  
  // Write data handshake
  when(io.axi.wvalid && !io.axi.wready) {
    io.axi.wready := true.B
  }
  
  // Write response handshake
  when(io.axi.awready && io.axi.wready && io.axi.awvalid && io.axi.wvalid) {
    // Write data to the device register
    deviceReg := io.axi.wdata
    
    // Output the low 8 bits of the written data as a character
    printf(p"Written character: %c\n", deviceReg(7, 0))

    io.axi.bvalid := true.B
    io.axi.bresp := 0.U // 'OKAY' response
  }
    
  // Reset write handshake signals after response is accepted
  when(io.axi.bvalid && io.axi.bready) {
    io.axi.bvalid := false.B
  }
  
  // Reset read handshake signals after data is accepted
  when(io.axi.rvalid && io.axi.rready) {
    io.axi.rvalid := false.B
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