file://<WORKSPACE>/src/main/scala/npc/IFU.scala
### java.lang.IndexOutOfBoundsException: 0

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 2017
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
    val awvalid = Input(Bool())
    val awready = Output(Bool())
    val wdata = Input(UInt(32.W))
    val wstrb = Input(UInt(32.W))
    val wvalid = Input(Bool())
    val wready = Output(Bool())
    val bresp = Output(Bool())
    val bvalid = Output(Bool())
    val bready = Input(Bool())
  })

  addResource("/Inst_Memory.v")
}


class IFU extends  Module{
    val io = IO(new Bundle {
        val pc = Input(UInt(32.W))
        val out = Decoupled(new IFU_IDU)
    })

    val arvalid = RegInit(false.B)
    val rready = RegInit(false.B)
    val awaddr = RegInit(0.U(32.W))
    val wdata = RegInit(0.U(32.W))
    val wstrb = RegInit(0.U(32.W))
    val awvalid = RegInit(false.B)
    val bready = RegInit(false.B)
    val wvalid = RegInit(false.B)


    val imem = Module(new Inst_Memory)
    imem.io.pc := io.pc
    imem.io.arvalid := arvalid
    imem.io.rready := rready
    imem.io.awaddr := awaddr
    imem.io.awvalid := awvalid
    imem.io.wdata := wdata
    imem.io.wstrb := wstrb
    imem.io.wvalid := wvalid
    imem.io.bready := bready

    val arready = imem.io.arready
    val rresp = imem.io.rresp
    val rvalid = imem.io.rvalid
    val awready = imem.io.awready
    val bresp = imem.io.bresp
    val bvalid = imem.io.bvalid
    val wready = imem.io.wready


    val IR = RegInit(0.U(32.W))
    val inst_reg = RegInit(0.U(32.W))
    val vaild_reg = RegInit(false.B)


    when(io.pc =/= IR){     //有新的pc进来了,将读地址有效拉高
        arvalid := true.B
        IR := io.pc
    }

    when(arvalid && arready){     //读地址握手成功，准备读数据,@@
        arvalid := false.B
        rready := true.B
        inst_reg := imem.io.inst
    }

    when(rready && rvalid){
        rready := false.B
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
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:435)
```
#### Short summary: 

java.lang.IndexOutOfBoundsException: 0