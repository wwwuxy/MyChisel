file://<WORKSPACE>/src/main/scala/npc/IFU.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 2448
uri: file://<WORKSPACE>/src/main/scala/npc/IFU.scala
text:
```scala
package npc

import chisel3._
import chisel3.util.HasBlackBoxResource
import chisel3.util.Decoupled

//BlackBox for Inst_Memory
class Inst_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
//imem
    val pc = Input(UInt(32.W))
    val inst = Output(UInt(32.W))
    val arvalid = Input(Bool())
    val arready = Output(Bool())
    val rresp = Output(Bool())
    val rvalid = Output(Bool())
    val rready = Input(Bool())
    val awaddr = Input(UInt(32.W))
    val awvalid = Output(Bool())
    val awready = Output(Bool())
    val wdata = Input(UInt(32.W))
    val wstrb = Input(UInt(32.W))
    val wvalid = Input(Bool())
    val wready = Input(Bool())
    val bresp = Output(Bool())
    val bvalid = Output(Bool())
    val bready = Input(Bool())
  })

  addResource("/Inst_Memory.v")
}


class IFU extends  Module{
    val io = IO(new Bundle {
        val pc = Input(UInt(32.W))
        val arvalid = Output(Bool())
        val arready = Input(Bool())
        val rvalid = Input(Bool())
        val rready = Output(Bool())
        val rresp = Input(UInt(2.W))
        val rvalid = Input(Bool())
        val rready = Output(Bool())
        val awaddr = Output(UInt(32.W))
        val awvalid = Input(Bool())
        val awready = Input(Bool())
        val wdata = Output(UInt(32.W))
        val wstrb = Output(UInt(32.W))
        val wvalid = Output(Bool())
        val wready = Output(Bool())
        val bresp = Input(Bool())
        val bvalid = Input(Bool())
        val bready = Output(Bool())
        val out = Decoupled(new IFU_IDU)
    })

    io.arready := 0
    io.ready := 0
    io.awaddr := 0
    io.wdata := 0
    io.wstrb := 0
    io.wvalid := 0
    io.wready := 0
    io.bread := 0


    val imem = Module(new Inst_Memory)
    imem.io.pc := io.pc
    imem.io.inst := io.inst
    imem.io.arvalid := io.arvalid
    imem.io.rready := io.rready
    imem.io.awaddr := io.awaddr
    imem.io.awvalid := io.awvalid
    imem.io.wdata := io.wdata
    imem.io.wstrb := io.wstrb
    imem.io.wvalid := io.wvalid
    imem.io.wready := io.wready
    imem.io.bready := io.bready

    io.arready := imem.io.arready
    io.rresp := imem.io.rresp
    io.rvalid := imem.io.rvalid
    io.awready := imem.io.awready
    io.bresp := imem.io.bresp
    io.bvalid := imem.io.bvalid



    val IR = RegInit(0.U(32.W))
    val inst_reg = RegInit(0.U(32.W))
    val vaild_reg = RegInit(false.B)


    when(io.pc =/= IR){     //有新的pc进来了,@@
        io.arvalid := 1
        IR := io.pc
    }

    when(io.arvalid && io.arready){
        io.rready := 1
        inst_reg := imem.io.inst
    }

//out
    io.out.bits.pc := IR
    io.out.bits.inst := inst_reg
    io.out.valid := true.B
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