package npc

import chisel3._
import chisel3.stage._
import circt.stage.ChiselStage
import chisel3.util._

class ysyx_23060192 extends Module{
    val io = IO(new Bundle {
        val interrupt = Input(Bool())

        val master_arvalid = Output(Bool())
        val master_araddr  = Output(UInt(32.W))
        val master_arid    = Output(UInt(4.W))
        val master_arlen   = Output(UInt(8.W))
        val master_arsize  = Output(UInt(3.W))
        val master_arburst = Output(UInt(2.W))
        val master_rready  = Output(Bool())
        val master_awvalid = Output(Bool())
        val master_awaddr  = Output(UInt(32.W))
        val master_awid    = Output(UInt(4.W))
        val master_awlen   = Output(UInt(8.W))
        val master_awsize  = Output(UInt(3.W))
        val master_awburst = Output(UInt(2.W))
        val master_wvalid  = Output(Bool())
        val master_wdata   = Output(UInt(32.W))
        val master_wstrb   = Output(UInt(4.W))
        val master_wlast   = Output(Bool())
        val master_bready  = Output(Bool())
        
        val master_arready = Input(Bool())
        val master_rvalid  = Input(Bool())
        val master_rdata   = Input(UInt(32.W))
        val master_rlast   = Input(Bool())
        val master_rresp   = Input(UInt(2.W))
        val master_rid     = Input(UInt(4.W))
        val master_awready = Input(Bool())
        val master_wready  = Input(Bool())
        val master_bvalid  = Input(Bool())
        val master_bresp   = Input(UInt(2.W))
        val master_bid     = Input(UInt(4.W))

        val slave_arvalid = Input(Bool())
        val slave_araddr  = Input(UInt(32.W))
        val slave_arid    = Input(UInt(4.W))
        val slave_arlen   = Input(UInt(8.W))
        val slave_arsize  = Input(UInt(3.W))
        val slave_arburst = Input(UInt(2.W))
        val slave_rready  = Input(Bool())
        val slave_awvalid = Input(Bool())
        val slave_awaddr  = Input(UInt(32.W))
        val slave_awid    = Input(UInt(4.W))
        val slave_awlen   = Input(UInt(8.W))
        val slave_awsize  = Input(UInt(3.W))
        val slave_awburst = Input(UInt(2.W))
        val slave_wvalid  = Input(Bool())
        val slave_wdata   = Input(UInt(32.W))
        val slave_wstrb   = Input(UInt(4.W))
        val slave_wlast   = Input(Bool())
        val slave_bready  = Input(Bool())

        val slave_arready = Output(Bool())
        val slave_rvalid  = Output(Bool())
        val slave_rdata   = Output(UInt(32.W))
        val slave_rlast   = Output(Bool())
        val slave_rresp   = Output(UInt(2.W))
        val slave_rid     = Output(UInt(4.W))
        val slave_awready = Output(Bool())
        val slave_wready  = Output(Bool())
        val slave_bvalid  = Output(Bool())
        val slave_bresp   = Output(UInt(2.W))
        val slave_bid     = Output(UInt(4.W))
    })

    val ifu  = Module(new IFU)
    val idu  = Module(new IDU)
    val exu  = Module(new EXU)
    val isu  = Module(new ISU)
    val wbu  = Module(new WBU)
    val pc   = Module(new PC)
    val xbar = Module(new XBAR)
    
    StageConnect(ifu.io.out, idu.io.in)
    StageConnect(ifu.io.ifu_axi_out, xbar.io.ifu_axi_in)
    StageConnect(ifu.io.ifu_axi_in, xbar.io.ifu_axi_out)
    StageConnect(idu.io.out, exu.io.in)
    StageConnect(exu.io.out, isu.io.in)
    StageConnect(isu.io.out, wbu.io.in)
    StageConnect(isu.io.isu_axi_out, xbar.io.isu_axi_in)
    StageConnect(isu.io.isu_axi_in, xbar.io.isu_axi_out)   
    StageConnect(wbu.io.out, pc.io.in)
  
    ifu.io.pc := pc.io.next_pc
    
    idu.io.alu_rsl   := wbu.io.alu_out
    idu.io.dm_out    := wbu.io.dm_out
    idu.io.wbu_valid := wbu.io.wbu_valid
    isu.io.wbu_valid := wbu.io.wbu_valid

        //AXI仲裁器
    xbar.io.ifu_valid      := ifu.io.ifu_valid
    xbar.io.isu_valid      := isu.io.isu_valid
    xbar.io.xbar_in.valid  := true.B
    xbar.io.xbar_out.ready := true.B

        //master - slave
    io.master_arvalid := xbar.io.xbar_out.bits.arvalid
        // io.master_load_unsign := xbar.io.master_load_unsign
    io.master_araddr  := xbar.io.xbar_out.bits.araddr
    io.master_arid    := xbar.io.xbar_out.bits.arid
    io.master_arlen   := xbar.io.xbar_out.bits.arlen
    io.master_arsize  := xbar.io.xbar_out.bits.arsize
    io.master_arburst := xbar.io.xbar_out.bits.arburst
    io.master_rready  := xbar.io.xbar_out.bits.rready
    io.master_awvalid := xbar.io.xbar_out.bits.awvalid
    io.master_awaddr  := xbar.io.xbar_out.bits.awaddr
    io.master_awid    := xbar.io.xbar_out.bits.awid
    io.master_awlen   := xbar.io.xbar_out.bits.awlen
    io.master_awsize  := xbar.io.xbar_out.bits.awsize
    io.master_awburst := xbar.io.xbar_out.bits.awburst
    io.master_wvalid  := xbar.io.xbar_out.bits.wvalid
    io.master_wdata   := xbar.io.xbar_out.bits.wdata
    io.master_wstrb   := xbar.io.xbar_out.bits.wstrb
    io.master_wlast   := xbar.io.xbar_out.bits.wlast
    io.master_bready  := xbar.io.xbar_out.bits.bready

        //slave - master
    xbar.io.xbar_in.bits.arready := io.master_arready
    xbar.io.xbar_in.bits.rvalid  := io.master_rvalid
    xbar.io.xbar_in.bits.rdata   := io.master_rdata
    xbar.io.xbar_in.bits.rlast   := io.master_rlast
    xbar.io.xbar_in.bits.rresp   := io.master_rresp
    xbar.io.xbar_in.bits.awready := io.master_awready
    xbar.io.xbar_in.bits.wready  := io.master_wready
    xbar.io.xbar_in.bits.bvalid  := io.master_bvalid
    xbar.io.xbar_in.bits.bresp   := io.master_bresp
    xbar.io.xbar_in.bits.bid     := io.master_bid
    
      //不使用的输出，置零
    io.slave_arready := 0.U
    io.slave_rvalid  := 0.U
    io.slave_rdata   := 0.U
    io.slave_rlast   := 0.U
    io.slave_rresp   := 0.U
    io.slave_rid     := 0.U
    io.slave_awready := 0.U
    io.slave_wready  := 0.U
    io.slave_bvalid  := 0.U
    io.slave_bresp   := 0.U
    io.slave_bid     := 0.U
}

object StageConnect {
  def apply[T <: Data](left: DecoupledIO[T], right: DecoupledIO[T]) = {
    val arch = "multi"
    if      (arch == "single")   { right.bits := left.bits }
    else if (arch == "multi")    { right <> left }
            // else if (arch == "pipeline") { right <> RegEnable(left, left.fire) }
            // else if (arch == "ooo")      { right <> Queue(left, 16) }
  }
}

