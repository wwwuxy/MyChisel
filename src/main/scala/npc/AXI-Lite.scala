package npc

import chisel3._
import chisel3.util._

class  TO_AXI extends Bundle{
    val arvalid     = Output(Bool())
    val araddr      = Output(UInt(32.W))
    val arid        = Output(UInt(4.W))
    val arlen       = Output(UInt(8.W))
    val arsize      = Output(UInt(3.W))
    val arburst     = Output(UInt(2.W))
    val rready      = Output(Bool())
    val awvalid     = Output(Bool())
    val awaddr      = Output(UInt(32.W))
    val awid        = Output(UInt(4.W))
    val awlen       = Output(UInt(8.W))
    val awsize      = Output(UInt(3.W))
    val awburst     = Output(UInt(2.W))
    val wvalid      = Output(Bool())
    val wdata       = Output(UInt(32.W))
    val wstrb       = Output(UInt(4.W))
    val wlast       = Output(Bool())
    val bready      = Output(Bool())
}

class  FROM_AXI extends Bundle{
    val arready = Output(Bool())
    val rvalid  = Output(Bool())
    val rdata   = Output(UInt(32.W))
    val rlast   = Output(Bool())
    val rresp   = Output(UInt(2.W))
    val awready = Output(Bool())
    val wready  = Output(Bool())
    val bvalid  = Output(Bool())
    val bresp   = Output(UInt(2.W))
    val bid     = Output(UInt(4.W))
}

