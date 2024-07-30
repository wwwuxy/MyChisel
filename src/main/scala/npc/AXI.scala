package  npc

import chisel3._
import chisel3.util._
import javax.swing.Box

class Date_Memory extends BlackBox with HasBlackBoxResource {
  val io = IO(new Bundle {
    val clk = Input(Clock())
  //AR
    val arvalid     = Input(Bool())
    val araddr      = Input(UInt(32.W))
    val load_unsign = Input(Bool())
    val arready     = Output(Bool())
  //R
    val rdata  = Output(UInt(32.W))
    val rresp  = Output(Bool())
    val rvalid = Output(Bool())
    val rready = Input(Bool())
  //AW    
    val awvalid = Input(Bool())
    val awaddr  = Input(UInt(32.W))
    val awready = Output(Bool())
  //W
    val wvalid = Input(Bool())
    val wdata  = Input(UInt(32.W))
    val len    = Input(UInt(32.W))
    val wready = Output(Bool())
  //B
    val bresp  = Output(Bool())
    val bvalid = Output(Bool())
    val bready = Input(Bool())
    
    
  })
  addResource("/Date_Memory.v")
}

class AXI extends Module{
    val io = IO(new Bundle{
        val ifu_axi_in  = Flipped(Decoupled(new TO_AXI))
        val ifu_axi_out = Decoupled(new FROM_AXI)
        val isu_axi_in = Flipped(Decoupled(new TO_AXI))
        val isu_axi_out = Decoupled(new FROM_AXI)
        val ifu_valid = Input(Bool())
        val isu_valid = Input(Bool())
    })

    val Mem = Module(new Date_Memory())
    Mem.io.clk := clock

    val sIdle :: sIFU :: sISU :: sHandshake :: sBusy :: Nil = Enum(5)
    val state = RegInit(sIdle)

    switch(state) {
      is(sIdle) {
        when(io.ifu_valid) {
          state := sIFU
        }.elsewhen(io.isu_valid) {
          state := sISU
        }
      }
      is(sIFU) {
        when(io.ifu_axi_in.valid && io.ifu_axi_in.bits.arvalid) {
          state := sHandshake
        }
      }
      is(sISU) {
        when(io.isu_axi_in.valid && (io.isu_axi_in.bits.arvalid || io.isu_axi_in.bits.awvalid)) {
          state := sHandshake
        }
      }
      is(sHandshake) {
        when(io.ifu_axi_out.ready || io.isu_axi_out.ready) {
          state := sBusy
        }
      }
      is(sBusy) {
        when(Mem.io.rresp || Mem.io.bresp) {
          state := sIdle
        }
      }
    }

  val ifu_selected = (state === sIFU) || (state === sBusy && io.ifu_valid)
  val isu_selected = (state === sISU) || (state === sBusy && io.isu_valid)

  Mem.io.arvalid := Mux(ifu_selected, io.ifu_axi_in.bits.arvalid, io.isu_axi_in.bits.arvalid)
  io.ifu_axi_out.bits.arready := Mux(ifu_selected, Mem.io.arready, false.B) && (state === sBusy)
  io.isu_axi_out.bits.arready := Mux(isu_selected, Mem.io.arready, false.B) && (state === sBusy)
  Mem.io.araddr := Mux(ifu_selected, io.ifu_axi_in.bits.araddr, io.isu_axi_in.bits.araddr)
  Mem.io.load_unsign := Mux(ifu_selected, io.ifu_axi_in.bits.load_unsign, io.isu_axi_in.bits.load_unsign)

  io.ifu_axi_out.bits.rvalid := Mux(ifu_selected, Mem.io.rvalid, false.B) && (state === sBusy)
  io.isu_axi_out.bits.rvalid := Mux(isu_selected, Mem.io.rvalid, false.B) && (state === sBusy)
  Mem.io.rready := Mux(ifu_selected, io.ifu_axi_in.bits.rready, io.isu_axi_in.bits.rready) && (state === sBusy)
  io.ifu_axi_out.bits.rdata := Mem.io.rdata
  io.isu_axi_out.bits.rdata := Mem.io.rdata
  io.ifu_axi_out.bits.rresp := Mux(ifu_selected, Mem.io.rresp, false.B) && (state === sBusy)
  io.isu_axi_out.bits.rresp := Mux(isu_selected, Mem.io.rresp, false.B) && (state === sBusy)

  Mem.io.awvalid := Mux(ifu_selected, io.ifu_axi_in.bits.awvalid, io.isu_axi_in.bits.awvalid)
  io.ifu_axi_out.bits.awready := Mux(ifu_selected, Mem.io.awready, false.B) && (state === sBusy)
  io.isu_axi_out.bits.awready := Mux(isu_selected, Mem.io.awready, false.B) && (state === sBusy)
  Mem.io.awaddr := Mux(ifu_selected, io.ifu_axi_in.bits.awaddr, io.isu_axi_in.bits.awaddr)

  Mem.io.wvalid := Mux(ifu_selected, io.ifu_axi_in.bits.wvalid, io.isu_axi_in.bits.wvalid) && (state === sBusy)
  io.ifu_axi_out.bits.wready := Mux(ifu_selected, Mem.io.wready, false.B) && (state === sBusy)
  io.isu_axi_out.bits.wready := Mux(isu_selected, Mem.io.wready, false.B) && (state === sBusy)
  Mem.io.wdata := Mux(ifu_selected, io.ifu_axi_in.bits.wdata, io.isu_axi_in.bits.wdata)
  Mem.io.len := Mux(ifu_selected, io.ifu_axi_in.bits.wstrb, io.isu_axi_in.bits.wstrb)

  io.ifu_axi_out.bits.bvalid := Mux(ifu_selected, Mem.io.bvalid, false.B) && (state === sBusy)
  io.isu_axi_out.bits.bvalid := Mux(isu_selected, Mem.io.bvalid, false.B) && (state === sBusy)
  Mem.io.bready := Mux(ifu_selected, io.ifu_axi_in.bits.bready, io.isu_axi_in.bits.bready) && (state === sBusy)
  io.ifu_axi_out.bits.bresp := Mux(ifu_selected, Mem.io.bresp, false.B) && (state === sBusy)
  io.isu_axi_out.bits.bresp := Mux(isu_selected, Mem.io.bresp, false.B) && (state === sBusy)

  io.ifu_axi_in.ready := ifu_selected
  io.isu_axi_in.ready := isu_selected
  io.ifu_axi_out.valid    := (state  === sBusy && ifu_selected)
  io.isu_axi_out.valid    := (state  === sBusy && isu_selected)
}