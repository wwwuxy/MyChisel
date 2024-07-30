file://<WORKSPACE>/src/main/scala/npc/ISU.scala
### dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition sIdle is defined in
  <WORKSPACE>/src/main/scala/npc/IFU.scala
and also in
  <WORKSPACE>/src/main/scala/npc/ISU.scala
One of these files should be removed from the classpath.

occurred in the presentation compiler.

presentation compiler configuration:
Scala version: 3.3.3
Classpath:
<HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala3-library_3/3.3.3/scala3-library_3-3.3.3.jar [exists ], <HOME>/.cache/coursier/v1/https/maven.aliyun.com/repository/central/org/scala-lang/scala-library/2.13.12/scala-library-2.13.12.jar [exists ]
Options:



action parameters:
offset: 1280
uri: file://<WORKSPACE>/src/main/scala/npc/ISU.scala
text:
```scala
package npc

import chisel3._
import chisel3.util._


class ISU extends Module{
    val io = IO(new Bundle{
        val in          = Flipped(Decoupled(new EXU_ISU))
        val out         = Decoupled(new ISU_WBU)
        val is_busy     = Input(Bool())
        val arvalid     = Output(Bool())
        val arready     = Input(Bool())
        val load_unsign = Output(Bool())
        val raddr       = Output(UInt(32.W))
        val rvalid      = Input(Bool())
        val rready      = Output(Bool())
        val rdata       = Input(UInt(32.W))
        val rresp       = Input(Bool())
        val awvalid     = Output(Bool())
        val awready     = Input(Bool())
        val awaddr      = Output(UInt(32.W))
        val wvalid      = Output(Bool())
        val wready      = Input(Bool())
        val wdata       = Output(UInt(32.W))
        val wstrb       = Output(UInt(32.W))
        val bvalid      = Input(Bool())
        val bready      = Output(Bool())
        val bresp       = Input(Bool())
        val wbu_valid   = Input(Bool())
    })

    val Dmem = Module(new Date_Memory())

    Dmem.io.clk := clock
    
    arvalid      = false.B
    load_finish  = false.B
    rready       = true.B
    awvalid      = false.B
    store_finish = false.B
    wvalid       = fa@@lse.B
    bready       = true.B

    when(io.in.bits.is_load){
        when(io.in.valid){
            arvalid := true.B
        }
    }
    
    when(arvalid && Dmem.io.arready){
        arvalid := false.B
    }

    when(Dmem.io.rvalid && rready){
        load_finish := true.B
    }.elsewhen(load_finish){
        load_finish := false.B
    }

    when(io.in.bits.mem_wr_en){
        when(io.in.valid){
            awvalid := true.B
        }
    }

    when(awvalid && Dmem.io.awready){   //写地址握手成功
        awvalid := false.B
        wvalid  := true.B
    }

    when(wvalid && Dmem.io.wready){     //写数据握手成功
        wvalid := false.B
    }

    when(Dmem.io.bresp){
        store_finish := true.B
    }.elsewhen(store_finish){
        store_finish := false.B
    }

//Dmem
    Dmem.io.len := io.in.bits.len
    Dmem.io.load_unsign := io.in.bits.load_unsign
    Dmem.io.araddr := io.in.bits.alu_out
    Dmem.io.awaddr := io.in.bits.alu_out
    Dmem.io.wdata := io.in.bits.data
    
    Dmem.io.awvalid := awvalid
    Dmem.io.arvalid := arvalid
    Dmem.io.wvalid := wvalid
    Dmem.io.rready := rready
    Dmem.io.bready := bready

//for wbu
    io.out.bits.dm_out       := Dmem.io.rdata
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
    
//for wbu
    io.out.bits.is_cmp   := io.in.bits.is_cmp
    io.out.bits.is_load  := io.in.bits.is_load
    io.out.bits.isS_type := io.in.bits.isS_type

    io.out.bits.is_j := io.in.bits.is_j
    io.out.bits.pc   := io.in.bits.pc

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

```
#### Short summary: 

dotty.tools.dotc.core.TypeError$$anon$1: Toplevel definition sIdle is defined in
  <WORKSPACE>/src/main/scala/npc/IFU.scala
and also in
  <WORKSPACE>/src/main/scala/npc/ISU.scala
One of these files should be removed from the classpath.