package npc

import chisel3._
import chisel3.util.HasBlackBoxResource
import chisel3.util.Decoupled
import chisel3.util._


//BlackBox for Inst_Memory
class Inst_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk = Input(Clock())
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
        val diff_test = Output(Bool())
    })

    val arvalid = RegInit(false.B)
    val rready = RegInit(true.B)
    val awaddr = RegInit(0.U(32.W))
    val wdata = RegInit(0.U(32.W))
    val wstrb = RegInit(0.U(32.W))
    val awvalid = RegInit(false.B)
    val bready = RegInit(false.B)
    val wvalid = RegInit(false.B)
    io.diff_test := false.B
    // val can_wirte = RegInit(false.B)

    val imem = Module(new Inst_Memory)
    imem.io.clk := clock
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


    val IPC = RegInit(0.U(32.W))
    val IR = RegInit(0.U(32.W))
    rready := true.B

  val sIdle :: sFetch :: sValid :: sGetinst :: Nil = Enum(4)
  val state = RegInit(sIdle)

  switch(state) {
    is(sIdle) {
      when(io.pc =/= IPC) {
        arvalid := true.B
        io.diff_test := true.B
        state := sFetch
      }
    }
    is(sFetch) {
      when(arvalid && arready) {
        arvalid := false.B
        state := sGetinst
      }
    }
    is(sGetinst) {
      when(rready && rvalid) {
        when(rresp) {
          IR := imem.io.inst
          IPC := io.pc
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

  // out
  io.out.valid := (state === sValid)
  io.out.bits.pc := IPC
  io.out.bits.inst := IR
}
